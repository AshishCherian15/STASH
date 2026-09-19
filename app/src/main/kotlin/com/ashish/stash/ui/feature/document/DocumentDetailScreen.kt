package com.ashish.stash.ui.feature.document

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ashish.stash.ui.feature.settings.SettingsViewModel
import com.ashish.stash.ui.theme.StashBlue
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentDetailScreen(
    onNavigateBack: () -> Unit,
    onViewDocument: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DocumentDetailViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showPriorityDialog by remember { mutableStateOf(false) }
    
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var folderMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleLock() }) {
                        val isLocked = uiState.documentWithMetadata?.document?.isLocked == 1
                        Icon(
                            if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = if (isLocked) "Unlock" else "Lock",
                            tint = StashBlue
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = StashBlue)
                    }
                    IconButton(onClick = {
                        val doc = uiState.documentWithMetadata?.document ?: return@IconButton
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = doc.mimeType
                            putExtra(Intent.EXTRA_STREAM, Uri.parse(doc.uri))
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Document"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = StashBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = StashBlue,
                    navigationIconContentColor = StashBlue
                )
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = StashBlue)
            }
        } else {
            val docWithMetadata = uiState.documentWithMetadata
            if (docWithMetadata != null) {
                val doc = docWithMetadata.document
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Header Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = StashBlue.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = doc.displayTitle,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    color = StashBlue
                                )
                                IconButton(onClick = { showRenameDialog = true }) {
                                    Icon(Icons.Outlined.Edit, contentDescription = "Rename", tint = StashBlue)
                                }
                            }
                            Button(
                                onClick = { onViewDocument(doc.documentId) },
                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
                            ) {
                                Icon(Icons.Default.Visibility, null)
                                Spacer(Modifier.width(8.dp))
                                Text("Open Preview")
                            }
                        }
                    }
                    
                    // Organization (Dropdown Selection)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Organization", style = MaterialTheme.typography.titleMedium, color = StashBlue)
                        
                        // Category Dropdown
                        Box {
                            DetailItem(label = "Category", value = docWithMetadata.category?.name ?: "None", onClick = { categoryMenuExpanded = true })
                            DropdownMenu(expanded = categoryMenuExpanded, onDismissRequest = { categoryMenuExpanded = false }) {
                                settingsState.categories.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            viewModel.setCategory(category.categoryId)
                                            categoryMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Folder Dropdown
                        Box {
                            DetailItem(label = "Folder", value = docWithMetadata.folder?.name ?: "Root", onClick = { folderMenuExpanded = true })
                            DropdownMenu(expanded = folderMenuExpanded, onDismissRequest = { folderMenuExpanded = false }) {
                                settingsState.folders.forEach { folder ->
                                    DropdownMenuItem(
                                        text = { Text(folder.name) },
                                        onClick = {
                                            viewModel.setFolder(folder.folderId)
                                            folderMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        DetailItem(label = "Importance", value = doc.importance, onClick = { showPriorityDialog = true })
                        DetailItem(label = "MIME Type", value = doc.mimeType)
                        DetailItem(label = "File Size", value = formatFileSize(doc.fileSize))
                    }

                    // Description (Notes) Section
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Description", style = MaterialTheme.typography.titleMedium, color = StashBlue)
                        OutlinedTextField(
                            value = doc.notes ?: "",
                            onValueChange = { viewModel.updateNotes(it) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            placeholder = { Text("Add private description...") },
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = StashBlue, focusedLabelColor = StashBlue)
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }

    if (showRenameDialog) {
        var newTitle by remember { mutableStateOf(uiState.documentWithMetadata?.document?.displayTitle ?: "") }
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename Document") },
            text = {
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("New Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.renameDocument(newTitle)
                    showRenameDialog = false
                }, colors = ButtonDefaults.buttonColors(containerColor = StashBlue)) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("Cancel", color = StashBlue)
                }
            }
        )
    }

    if (showPriorityDialog) {
        AlertDialog(
            onDismissRequest = { showPriorityDialog = false },
            title = { Text("Set Importance") },
            text = {
                Column {
                    listOf("LOW", "NORMAL", "HIGH", "CRITICAL").forEach { level ->
                        Row(
                            Modifier.fillMaxWidth().clickable { 
                                viewModel.setPriority(level)
                                showPriorityDialog = false
                            }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = uiState.documentWithMetadata?.document?.importance == level, onClick = null, colors = RadioButtonDefaults.colors(selectedColor = StashBlue))
                            Spacer(Modifier.width(12.dp))
                            Text(level)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Document") },
            text = { Text("Permanently remove this document index from Stash? The physical file will not be deleted.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteDocument()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DetailItem(label: String, value: String, onClick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = StashBlue)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            if (onClick != null) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = StashBlue)
            }
        }
        if (onClick != null) {
            HorizontalDivider(modifier = Modifier.padding(top = 4.dp), thickness = 0.5.dp, color = StashBlue.copy(alpha = 0.1f))
        }
    }
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(Locale.US, "%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}

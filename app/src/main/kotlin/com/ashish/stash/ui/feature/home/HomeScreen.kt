package com.ashish.stash.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.ui.component.rememberSafFilePickerLauncher
import com.ashish.stash.ui.component.rememberSafMultiFilePickerLauncher
import com.ashish.stash.ui.theme.InkNavy
import com.ashish.stash.ui.theme.LedgerSlate
import com.ashish.stash.ui.theme.Limestone
import com.ashish.stash.ui.theme.VaultBrass
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit,
    onNavigateToViewer: (Long) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToPriority: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val filePicker = rememberSafFilePickerLauncher(
        onFileSelected = { uri -> viewModel.importDocument(uri) }
    )

    val multiFilePicker = rememberSafMultiFilePickerLauncher(
        onFilesSelected = { uris -> viewModel.importDocuments(uris) }
    )

    LaunchedEffect(uiState.importSuccess, uiState.importError) {
        if (uiState.importSuccess) {
            snackbarHostState.showSnackbar("Document imported successfully")
            viewModel.clearImportSuccess()
        }
        uiState.importError?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearImportError()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Stash", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = onNavigateToPriority) {
                        Icon(Icons.Outlined.Star, contentDescription = "Priority mode")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = InkNavy,
                    titleContentColor = Limestone,
                    navigationIconContentColor = Limestone,
                    actionIconContentColor = Limestone
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { multiFilePicker.launch(arrayOf("*/*")) }) {
                        Icon(Icons.Default.CreateNewFolder, contentDescription = "Batch Import")
                    }
                    Text(
                        "Vault: ${uiState.stats.totalDocuments} items",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { filePicker.launch(arrayOf("*/*")) },
                        containerColor = VaultBrass,
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp) // Docked look
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Document")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.documents.isEmpty() && !uiState.isLoading) {
                EmptyHomeContent(onAddClick = { filePicker.launch(arrayOf("*/*")) })
            } else {
                DocumentListContent(uiState, onNavigateToViewer)
            }

            if (uiState.isImporting) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VaultBrass)
                }
            }
        }
    }
}

@Composable
private fun DocumentListContent(
    state: HomeUiState,
    onDocumentClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            StatStrip(
                unlockedCount = state.stats.unlockedDocuments,
                lockedCount = state.stats.lockedDocuments,
                totalSizeBytes = state.stats.totalSizeBytes
            )
        }

        items(items = state.documents, key = { it.data.document.documentId }) { docModel ->
            DocumentCard(
                documentWithMetadata = docModel.data,
                onClick = { onDocumentClick(docModel.data.document.documentId) }
            )
        }
    }
}

@Composable
private fun StatStrip(unlockedCount: Int, lockedCount: Int, totalSizeBytes: Long) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$unlockedCount docs ($lockedCount locked)", style = MaterialTheme.typography.labelMedium, color = LedgerSlate)
        Text(formatFileSize(totalSizeBytes), style = MaterialTheme.typography.labelSmall, color = LedgerSlate)
    }
}

@Composable
private fun EmptyHomeContent(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.FolderSpecial,
            contentDescription = null,
            tint = VaultBrass,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Your vault is empty",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Index documents already on your device without duplicating them.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = LedgerSlate
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = VaultBrass),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.width(8.dp))
            Text("Add Document")
        }
    }
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(Locale.US, "%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}

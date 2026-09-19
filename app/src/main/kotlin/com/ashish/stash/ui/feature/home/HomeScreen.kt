package com.ashish.stash.ui.feature.home

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.ViewQuilt
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.saf.ShareHelper
import com.ashish.stash.ui.component.rememberSafMultiFilePickerLauncher
import com.ashish.stash.ui.theme.LedgerSlate
import com.ashish.stash.ui.theme.Limestone
import com.ashish.stash.ui.theme.StashBlue
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    var viewMenuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var selectedDocIds by rememberSaveable { mutableStateOf(setOf<Long>()) }
    val isSelectionMode = selectedDocIds.isNotEmpty()

    val multiFilePicker = rememberSafMultiFilePickerLauncher(
        onFilesSelected = { uris -> viewModel.importDocuments(uris) }
    )

    BackHandler(enabled = isSelectionMode) { selectedDocIds = emptySet() }

    LaunchedEffect(uiState.importSuccess) {
        if (uiState.importSuccess) {
            snackbarHostState.showSnackbar("Documents imported successfully")
            viewModel.clearImportSuccess()
        }
    }

    Scaffold(
        topBar = {
            if (isSelectionMode) {
                TopAppBar(
                    title = { Text("${selectedDocIds.size} selected") },
                    navigationIcon = {
                        IconButton(onClick = { selectedDocIds = emptySet() }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            val docsToShare = uiState.documents
                                .filter { it.data.document.documentId in selectedDocIds }
                                .map { it.data.document }
                            val intent = ShareHelper.createShareIntent(context, docsToShare)
                            context.startActivity(Intent.createChooser(intent, "Share Documents"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                        IconButton(onClick = { 
                            selectedDocIds.forEach { viewModel.deleteDocument(it) }
                            selectedDocIds = emptySet()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                )
            } else {
                CenterAlignedTopAppBar(
                    title = { Text("Vault", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onOpenDrawer) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToSearch) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        Box {
                            IconButton(onClick = { viewMenuExpanded = true }) {
                                Icon(Icons.Default.GridView, contentDescription = "View Mode")
                            }
                            DropdownMenu(expanded = viewMenuExpanded, onDismissRequest = { viewMenuExpanded = false }) {
                                ViewMode.entries.forEach { mode ->
                                    DropdownMenuItem(
                                        text = { Text(mode.label) },
                                        onClick = { viewModel.setViewMode(mode); viewMenuExpanded = false },
                                        leadingIcon = {
                                            val icon = when(mode) {
                                                ViewMode.LIST -> Icons.AutoMirrored.Filled.List
                                                ViewMode.TILES -> Icons.AutoMirrored.Filled.ViewQuilt
                                                else -> Icons.Default.GridView
                                            }
                                            Icon(icon, null)
                                        }
                                    )
                                }
                            }
                        }
                        IconButton(onClick = onNavigateToPriority) {
                            Icon(Icons.Outlined.Star, contentDescription = "Priority mode")
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        navigationIconContentColor = MaterialTheme.colorScheme.primary,
                        actionIconContentColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                actions = {
                    Text(
                        "Vault Size: ${formatFileSize(uiState.stats.totalSizeBytes)}",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { multiFilePicker.launch(arrayOf("*/*")) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Documents")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.documents.isEmpty() && !uiState.isLoading) {
                EmptyHomeContent(onAddClick = { multiFilePicker.launch(arrayOf("*/*")) })
            } else {
                val onDocClick: (Long) -> Unit = { id ->
                    if (isSelectionMode) {
                        selectedDocIds = if (selectedDocIds.contains(id)) selectedDocIds - id else selectedDocIds + id
                    } else {
                        onNavigateToViewer(id)
                    }
                }
                val onDocLongClick: (Long) -> Unit = { id -> if (!isSelectionMode) selectedDocIds = setOf(id) }

                when (uiState.viewMode) {
                    ViewMode.LIST -> DocumentListView(uiState, selectedDocIds, onDocClick, onDocLongClick)
                    ViewMode.DETAILS -> DocumentDetailedView(uiState, selectedDocIds, onDocClick, onDocLongClick)
                    ViewMode.TILES -> DocumentTileView(uiState, selectedDocIds, onDocClick, onDocLongClick)
                    else -> DocumentGridView(uiState, selectedDocIds, onDocClick, onDocLongClick)
                }
            }

            if (uiState.isImporting) {
                Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.7f)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(16.dp))
                        Text("Processing files...", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentListView(state: HomeUiState, selectedIds: Set<Long>, onClick: (Long) -> Unit, onLongClick: (Long) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { StatStrip(stats = state.stats) }
        items(items = state.documents, key = { it.data.document.documentId }) { docModel ->
            val id = docModel.data.document.documentId
            DocumentCard(
                documentWithMetadata = docModel.data,
                onClick = { onClick(id) },
                modifier = Modifier.combinedClickable(onClick = { onClick(id) }, onLongClick = { onLongClick(id) })
                    .let { if (selectedIds.contains(id)) it.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) else it }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentDetailedView(state: HomeUiState, selectedIds: Set<Long>, onClick: (Long) -> Unit, onLongClick: (Long) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { StatStrip(stats = state.stats) }
        items(items = state.documents, key = { it.data.document.documentId }) { docModel ->
            val id = docModel.data.document.documentId
            DetailedDocumentRow(
                documentWithMetadata = docModel.data,
                onClick = { onClick(id) },
                modifier = Modifier.combinedClickable(onClick = { onClick(id) }, onLongClick = { onLongClick(id) })
                    .let { if (selectedIds.contains(id)) it.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) else it }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentTileView(state: HomeUiState, selectedIds: Set<Long>, onClick: (Long) -> Unit, onLongClick: (Long) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Adaptive(100.dp), modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item(span = { GridItemSpan(maxCurrentLineSpan) }) { StatStrip(stats = state.stats) }
        items(items = state.documents, key = { it.data.document.documentId }) { docModel ->
            val id = docModel.data.document.documentId
            DocumentTile(
                documentWithMetadata = docModel.data,
                onClick = { onClick(id) },
                modifier = Modifier.combinedClickable(onClick = { onClick(id) }, onLongClick = { onLongClick(id) })
                    .let { if (selectedIds.contains(id)) it.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) else it }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentGridView(state: HomeUiState, selectedIds: Set<Long>, onClick: (Long) -> Unit, onLongClick: (Long) -> Unit) {
    val columns = when (state.viewMode) {
        ViewMode.LARGE_GRID -> 2
        ViewMode.MEDIUM_GRID -> 3
        ViewMode.SMALL_GRID -> 4
        else -> 3
    }
    LazyVerticalGrid(columns = GridCells.Fixed(columns), modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item(span = { GridItemSpan(columns) }) { StatStrip(stats = state.stats) }
        items(items = state.documents, key = { it.data.document.documentId }) { docModel ->
            val id = docModel.data.document.documentId
            DocumentCard(
                documentWithMetadata = docModel.data,
                onClick = { onClick(id) },
                modifier = Modifier.combinedClickable(onClick = { onClick(id) }, onLongClick = { onLongClick(id) })
                    .let { if (selectedIds.contains(id)) it.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) else it }
            )
        }
    }
}

@Composable
fun DetailedDocumentRow(documentWithMetadata: DocumentWithMetadata, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val doc = documentWithMetadata.document
    Card(onClick = onClick, modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Description, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(doc.displayTitle, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("${formatFileSize(doc.fileSize)} • ${doc.mimeType.split("/").last().uppercase()}", style = MaterialTheme.typography.bodySmall)
            }
            if (doc.isLocked) Icon(Icons.Default.Lock, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun DocumentTile(documentWithMetadata: DocumentWithMetadata, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val doc = documentWithMetadata.document
    Column(
        modifier = modifier.width(100.dp).clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.medium), contentAlignment = Alignment.Center) {
            Icon(Icons.AutoMirrored.Filled.InsertDriveFile, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
            if (doc.isLocked) Icon(Icons.Default.Lock, null, modifier = Modifier.size(16.dp).align(Alignment.BottomEnd).padding(4.dp), tint = MaterialTheme.colorScheme.primary)
        }
        Text(doc.displayTitle, style = MaterialTheme.typography.labelSmall, maxLines = 2, textAlign = TextAlign.Center, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun StatStrip(stats: HomeStats) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text("${stats.unlockedDocuments} docs (${stats.lockedDocuments} locked)", style = MaterialTheme.typography.labelMedium, color = LedgerSlate)
        Text(formatFileSize(stats.totalSizeBytes), style = MaterialTheme.typography.labelSmall, color = LedgerSlate)
    }
}

@Composable
private fun EmptyHomeContent(onAddClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(40.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(imageVector = Icons.Default.Inventory2, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(120.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Text("Vault is empty", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Index documents already on your device without duplicating them.", textAlign = TextAlign.Center, color = LedgerSlate)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onAddClick) { Text("Add Documents") }
    }
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(Locale.US, "%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}

package com.ashish.stash.ui.feature.home

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashish.stash.core.saf.ShareHelper
import com.ashish.stash.ui.component.rememberSafFilePickerLauncher
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

    // Selection State (rememberSaveable for persistence across config changes)
    var selectedDocIds by rememberSaveable { mutableStateOf(setOf<Long>()) }
    val isSelectionMode = selectedDocIds.isNotEmpty()

    val multiFilePicker = rememberSafMultiFilePickerLauncher(
        onFilesSelected = { uris -> viewModel.importDocuments(uris) }
    )

    BackHandler(enabled = isSelectionMode) {
        selectedDocIds = emptySet()
    }

    LaunchedEffect(uiState.importSuccess) {
        if (uiState.importSuccess) {
            // Summary message from VM would be better, but for now we show a toast-like snackbar
            snackbarHostState.showSnackbar("Batch operation completed")
            viewModel.clearImportSuccess()
        }
    }

    uiState.importError?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(error)
            viewModel.clearImportError()
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
                            // Selection cleared only after explicit action or back
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
                    title = { },
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
                            DropdownMenu(
                                expanded = viewMenuExpanded,
                                onDismissRequest = { viewMenuExpanded = false }
                            ) {
                                ViewMode.entries.forEach { mode ->
                                    DropdownMenuItem(
                                        text = { Text(mode.label) },
                                        onClick = {
                                            viewModel.setViewMode(mode)
                                            viewMenuExpanded = false
                                        },
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
                        containerColor = Limestone,
                        navigationIconContentColor = StashBlue,
                        actionIconContentColor = StashBlue
                    )
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Limestone,
                contentColor = StashBlue,
                actions = {
                    Text(
                        "Vault: ${uiState.stats.totalDocuments} items",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { multiFilePicker.launch(arrayOf("*/*")) },
                        containerColor = StashBlue,
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Documents")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.White
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.documents.isEmpty() && !uiState.isLoading) {
                EmptyHomeContent(onAddClick = { multiFilePicker.launch(arrayOf("*/*")) })
            } else {
                val onDocClick: (Long) -> Unit = { id ->
                    if (isSelectionMode) {
                        selectedDocIds = if (selectedDocIds.contains(id)) selectedDocIds - id
                        else selectedDocIds + id
                    } else {
                        onNavigateToViewer(id)
                    }
                }
                val onDocLongClick: (Long) -> Unit = { id ->
                    if (!isSelectionMode) selectedDocIds = setOf(id)
                }

                when (uiState.viewMode) {
                    ViewMode.LIST, ViewMode.DETAILS -> DocumentList(uiState, selectedDocIds, onDocClick, onDocLongClick)
                    else -> DocumentGrid(uiState, selectedDocIds, onDocClick, onDocLongClick)
                }
            }

            if (uiState.isImporting) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = StashBlue)
                        Spacer(Modifier.height(16.dp))
                        // Progress text from VM state can be added here
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentList(
    state: HomeUiState, 
    selectedIds: Set<Long>,
    onClick: (Long) -> Unit,
    onLongClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { StatStrip(stats = state.stats) }
        items(items = state.documents, key = { it.data.document.documentId }) { docModel ->
            val docId = docModel.data.document.documentId
            DocumentCard(
                documentWithMetadata = docModel.data,
                onClick = { onClick(docId) },
                modifier = Modifier
                    .combinedClickable(
                        onClick = { onClick(docId) },
                        onLongClick = { onLongClick(docId) }
                    )
                    .let {
                        if (selectedIds.contains(docId)) it.background(StashBlue.copy(alpha = 0.1f)) else it
                    }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DocumentGrid(
    state: HomeUiState,
    selectedIds: Set<Long>,
    onClick: (Long) -> Unit,
    onLongClick: (Long) -> Unit
) {
    val columns = when (state.viewMode) {
        ViewMode.LARGE_GRID -> 2
        ViewMode.MEDIUM_GRID -> 3
        ViewMode.SMALL_GRID -> 4
        else -> 2
    }
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(columns) }) {
            StatStrip(stats = state.stats) 
        }
        items(items = state.documents, key = { it.data.document.documentId }) { docModel ->
            val docId = docModel.data.document.documentId
            DocumentCard(
                documentWithMetadata = docModel.data,
                onClick = { onClick(docId) },
                modifier = Modifier
                    .combinedClickable(
                        onClick = { onClick(docId) },
                        onLongClick = { onLongClick(docId) }
                    )
                    .let {
                        if (selectedIds.contains(docId)) it.background(StashBlue.copy(alpha = 0.1f)) else it
                    }
            )
        }
    }
}

@Composable
private fun StatStrip(stats: HomeStats) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("${stats.unlockedDocuments} docs (${stats.lockedDocuments} locked)", style = MaterialTheme.typography.labelMedium, color = LedgerSlate)
        Text(formatFileSize(stats.totalSizeBytes), style = MaterialTheme.typography.labelSmall, color = LedgerSlate)
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
            imageVector = Icons.Default.Inventory2,
            contentDescription = null,
            tint = StashBlue,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text("Your vault is empty", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Index documents already on your device without duplicating them.", textAlign = TextAlign.Center, color = LedgerSlate)
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onAddClick, colors = ButtonDefaults.buttonColors(containerColor = StashBlue)) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.width(8.dp))
            Text("Add Documents")
        }
    }
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(Locale.US, "%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}

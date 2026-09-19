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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.ViewQuilt
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.saf.ShareHelper
import com.ashish.stash.ui.component.rememberSafMultiFilePickerLauncher
import com.ashish.stash.ui.feature.settings.SettingsViewModel
import com.ashish.stash.ui.theme.LedgerSlate
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
    viewModel: HomeViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val settingsState by settingsViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var showSortMenu by remember { mutableStateOf(false) }

    var selectedDocIds by rememberSaveable { mutableStateOf(setOf<Long>()) }
    val isSelectionMode = selectedDocIds.isNotEmpty()

    val multiFilePicker = rememberSafMultiFilePickerLauncher { uris -> 
        viewModel.importDocuments(uris) 
    }

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
                            val docs = uiState.documents.filter { it.data.document.documentId in selectedDocIds }.map { it.data.document }
                            context.startActivity(Intent.createChooser(ShareHelper.createShareIntent(context, docs), "Share"))
                        }) { Icon(Icons.Default.Share, "Share") }
                        IconButton(onClick = { 
                            selectedDocIds.forEach { viewModel.deleteDocument(it) }
                            selectedDocIds = emptySet()
                        }) { Icon(Icons.Default.Delete, "Delete") }
                    }
                )
            } else {
                LargeTopAppBar(
                    title = {
                        Column {
                            Text("Vault", fontWeight = FontWeight.Bold)
                            Text(
                                "Total: ${uiState.stats.totalDocuments} items",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onOpenDrawer) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = onNavigateToSearch) { Icon(Icons.Default.Search, "Search") }
                        Box {
                            IconButton(onClick = { showSortMenu = true }) { Icon(Icons.AutoMirrored.Filled.Sort, "Sort") }
                            DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                                SortOption.entries.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.label) },
                                        onClick = { viewModel.setSortOption(option); showSortMenu = false }
                                    )
                                }
                            }
                        }
                        IconButton(onClick = onNavigateToPriority) { Icon(Icons.Outlined.Star, "Priority") }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { multiFilePicker.launch(arrayOf("*/*")) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Add Documents")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            CategoryTabRow(
                categories = settingsState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategoryClick = { id -> viewModel.setCategoryFilter(id) }
            )

            Box(modifier = Modifier.weight(1f)) {
                if (uiState.documents.isEmpty() && !uiState.isLoading) {
                    EmptyHomeContent()
                } else {
                    val onDocClick: (Long) -> Unit = { id ->
                        if (isSelectionMode) selectedDocIds = if (selectedDocIds.contains(id)) selectedDocIds - id else selectedDocIds + id
                        else onNavigateToViewer(id)
                    }
                    val onDocLongClick: (Long) -> Unit = { id -> if (!isSelectionMode) selectedDocIds = setOf(id) }

                    when (uiState.viewMode) {
                        ViewMode.LIST -> DocumentListView(uiState, selectedIds = selectedDocIds, onClick = onDocClick, onLongClick = onDocLongClick)
                        ViewMode.DETAILS -> DocumentDetailedView(uiState, selectedIds = selectedDocIds, onClick = onDocClick, onLongClick = onDocLongClick)
                        ViewMode.TILES -> DocumentTileView(uiState, selectedIds = selectedDocIds, onClick = onDocClick, onLongClick = onDocLongClick)
                        else -> DocumentGridView(uiState, selectedIds = selectedDocIds, onClick = onDocClick, onLongClick = onDocLongClick)
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
}

@Composable
fun CategoryTabRow(
    categories: List<CategoryEntity>,
    selectedCategoryId: Long?,
    onCategoryClick: (Long?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategoryId == null,
                onClick = { onCategoryClick(null) },
                label = { Text("All Files") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
        }
        items(categories) { category ->
            FilterChip(
                selected = selectedCategoryId == category.categoryId,
                onClick = { onCategoryClick(category.categoryId) },
                label = { Text(category.name) },
                leadingIcon = {
                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color(android.graphics.Color.parseColor(category.color))))
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.primary
                )
            )
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
private fun EmptyHomeContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Icon(Icons.Default.Inventory2, null, modifier = Modifier.size(120.dp), tint = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Your vault is empty", style = MaterialTheme.typography.headlineMedium)
            Text("Select the + button to add files or chose a folder to index.", textAlign = TextAlign.Center)
        }
    }
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return String.format(Locale.US, "%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
}

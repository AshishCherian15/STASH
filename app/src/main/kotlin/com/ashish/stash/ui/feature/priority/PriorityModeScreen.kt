package com.ashish.stash.ui.feature.priority

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.ui.feature.home.DocumentCard
import com.ashish.stash.ui.theme.Limestone
import com.ashish.stash.ui.theme.SignalRust
import com.ashish.stash.ui.theme.StashBlue
import com.ashish.stash.ui.theme.VaultBrass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityModeScreen(
    onOpenDrawer: () -> Unit,
    onNavigateToViewer: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PriorityModeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Priority Mode", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onOpenDrawer() }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Limestone,
                    titleContentColor = StashBlue,
                    navigationIconContentColor = StashBlue
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = StashBlue)
                }
            } else if (uiState.documents.isEmpty()) {
                PriorityEmptyState()
            } else {
                PriorityContent(
                    documents = uiState.documents,
                    onDocumentClick = onNavigateToViewer
                )
            }
        }
    }
}

@Composable
private fun PriorityContent(
    documents: List<DocumentWithMetadata>,
    onDocumentClick: (Long) -> Unit
) {
    val criticalDocs = documents.filter { it.document.importance == Importance.CRITICAL }
    val highDocs = documents.filter { it.document.importance == Importance.HIGH }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (criticalDocs.isNotEmpty()) {
            item {
                PriorityHeader("Critical Attention", SignalRust)
            }
            items(items = criticalDocs, key = { it.document.documentId }) { docWithMetadata ->
                DocumentCard(
                    documentWithMetadata = docWithMetadata,
                    onClick = { onDocumentClick(docWithMetadata.document.documentId) }
                )
            }
        }

        if (highDocs.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                PriorityHeader("High Priority", VaultBrass)
            }
            items(items = highDocs, key = { it.document.documentId }) { docWithMetadata ->
                DocumentCard(
                    documentWithMetadata = docWithMetadata,
                    onClick = { onDocumentClick(docWithMetadata.document.documentId) }
                )
            }
        }
    }
}

@Composable
private fun PriorityHeader(title: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Box(modifier = Modifier.size(12.dp).background(color, shape = CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PriorityEmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Priority Mode is clear",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Mark a document as HIGH or CRITICAL from its detail screen to see it here.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

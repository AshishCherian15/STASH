package com.ashish.stash.ui.feature.viewer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.document?.displayTitle ?: "Viewer") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { uiState.document?.let { onNavigateToDetails(it.documentId) } }) {
                        Icon(Icons.Default.Info, contentDescription = "Details")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val doc = uiState.document
                if (doc != null) {
                    when {
                        doc.mimeType.contains("pdf", ignoreCase = true) -> {
                            PdfViewer(uri = doc.uri)
                        }
                        doc.mimeType.startsWith("image/", ignoreCase = true) -> {
                            ImageViewer(uri = doc.uri)
                        }
                        doc.mimeType.startsWith("text/", ignoreCase = true) || doc.mimeType == "application/json" -> {
                            TextViewer(uri = doc.uri)
                        }
                        else -> {
                            Text("Unsupported file type: ${doc.mimeType}", modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }
}

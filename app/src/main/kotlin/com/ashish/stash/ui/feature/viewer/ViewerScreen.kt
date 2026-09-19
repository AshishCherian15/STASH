package com.ashish.stash.ui.feature.viewer

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashish.stash.core.security.SecuritySessionManager
import com.ashish.stash.ui.theme.StashBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Using a simple state check for now. Full re-auth would involve nav back to Lock screen.
    val isAppLocked by remember { mutableStateOf(false) } // Placeholder

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.document?.displayTitle ?: "Viewer") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { uiState.document?.let { onNavigateToDetails(it.documentId) } }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit Details")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = StashBlue,
                    navigationIconContentColor = StashBlue,
                    actionIconContentColor = StashBlue
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = StashBlue)
            } else {
                val doc = uiState.document
                if (doc != null) {
                    if (doc.isLocked == 1 && isAppLocked) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Lock, null, modifier = Modifier.size(64.dp), tint = StashBlue)
                            Text("This document is locked", style = MaterialTheme.typography.titleMedium)
                        }
                    } else {
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
}

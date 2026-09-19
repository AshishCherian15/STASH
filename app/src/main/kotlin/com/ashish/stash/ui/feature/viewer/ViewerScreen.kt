package com.ashish.stash.ui.feature.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
                    if (!uiState.isAccessDenied) {
                        IconButton(onClick = { uiState.document?.let { onNavigateToDetails(it.documentId) } }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Edit Details")
                        }
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
            } else if (uiState.isAccessDenied) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Lock, null, modifier = Modifier.size(80.dp), tint = StashBlue)
                    Spacer(Modifier.height(24.dp))
                    Text("Vault Security Active", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text("This document is part of your secure vault. Please unlock your vault items from the main dashboard to view it.", 
                        textAlign = TextAlign.Center)
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = onNavigateBack) { Text("Go Back") }
                }
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
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Preview not available for ${doc.mimeType}")
                                Spacer(Modifier.height(16.dp))
                                Button(onClick = { /* Could add share/open with here */ }) {
                                    Text("Open with external app")
                                }
                            }
                        }
                    }
                } else {
                    Text("Document not found", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

package com.ashish.stash.ui.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashish.stash.ui.theme.StashBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpFaqScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & FAQ") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = StashBlue,
                    navigationIconContentColor = StashBlue
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            HelpItem(
                icon = Icons.Default.QuestionMark,
                title = "What is Stash?",
                content = "Stash is a high-performance document vault that indexes your existing device storage in-place. It uses AI to read text inside images (OCR) and provides desktop-class search speeds."
            )

            HelpItem(
                icon = Icons.Default.Search,
                title = "Deep Search Intelligence",
                content = "You can search for documents not just by filename, but by the text *inside* them. Our smart ranking engine prioritizes documents based on relevance, your importance flags, and recent usage."
            )

            HelpItem(
                icon = Icons.Default.Security,
                title = "Is my data safe?",
                content = "Stash is 100% offline. We do not have internet permission and cannot upload your data. Documents are secured behind Biometric or PIN locks, and sensitive metadata is stored in an encrypted sandbox."
            )

            HelpItem(
                icon = Icons.Default.FolderZip,
                title = "In-Place Indexing",
                content = "Unlike other apps, Stash does not copy or move your files. It creates a lightweight index, meaning it takes up virtually zero extra storage on your device."
            )

            HelpItem(
                icon = Icons.Default.TipsAndUpdates,
                title = "Pro Tip: Multi-Select",
                content = "Long-press any document on the Home screen to enter selection mode. You can then bulk-share case files or delete multiple indices at once."
            )

            Surface(
                color = StashBlue.copy(alpha = 0.05f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Need more help?", style = MaterialTheme.typography.titleSmall, color = StashBlue)
                    Text("Contact the developer at ashishcherian15@gmail.com for technical support.", style = MaterialTheme.typography.bodySmall)
                }
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HelpItem(icon: ImageVector, title: String, content: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Icon(icon, null, tint = StashBlue, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

package com.ashish.stash.ui.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashish.stash.ui.theme.VaultBrass

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
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Welcome to Stash! Here is a guide on how to navigate and use the app features effectively.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HelpSection(
                title = "1. Navigation Guide",
                items = listOf(
                    "Home Dashboard: The main screen where you see all your unlocked documents and vault stats.",
                    "Sidebar Menu: Tap the 'Hamburger' icon (top-left) to access Home, Search, Priority Mode, and Settings.",
                    "Settings Hub: A vertical menu to manage Categories, Folders, Labels, Appearance, and Security."
                )
            )

            HelpSection(
                title = "2. Adding Documents",
                items = listOf(
                    "Single Import: Tap the '+' button on the Home screen to pick a file from your device.",
                    "Batch Import: Tap the 'Folder+' icon in the Top Bar to index an entire directory at once.",
                    "Quick Add: Share any file from another app (like WhatsApp or File Manager) directly to Stash."
                )
            )

            HelpSection(
                title = "3. Smart Search & OCR",
                items = listOf(
                    "Instant Search: Use the Search screen to find files by title, notes, or category.",
                    "Automatic OCR: Stash reads text from images/scans automatically. You can search for text *inside* images!",
                    "Offline-First: All search and OCR processing happens locally on your device for maximum privacy."
                )
            )

            HelpSection(
                title = "4. Security & Vault",
                items = listOf(
                    "Biometric Lock: Secure the app or individual files behind your fingerprint or face unlock.",
                    "Private Mode: Locked files are hidden from search and the main list until you authenticate.",
                    "Privacy Guard: Enable 'Prevent Screenshots' in Settings to hide your vault from recent apps previews."
                )
            )

            HelpSection(
                title = "5. Organization & Edits",
                items = listOf(
                    "Categories: Group files with custom colors. You can now EDIT category names and colors in Settings.",
                    "Folders: Manage virtual folders to mirror your physical storage hierarchy.",
                    "Labels: Add and manage searchable tags for detailed organization.",
                    "Priority Mode: Focus on high-importance files (HIGH/CRITICAL) in a dedicated dashboard."
                )
            )

            HelpSection(
                title = "6. Document Previews",
                items = listOf(
                    "Direct Viewing: Tap any document on the Home screen to open the built-in high-quality viewer.",
                    "Details: View full metadata, rename, or delete the file from the detail screen inside the app."
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Privacy Tip",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Stash indexes files in-place. We do not move or copy your files. If you delete the original file, Stash will show an 'Access Lost' status.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HelpSection(title: String, items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        items.forEach { item ->
            Row(verticalAlignment = Alignment.Top) {
                Text("• ", fontWeight = FontWeight.Bold, color = VaultBrass)
                Text(text = item, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

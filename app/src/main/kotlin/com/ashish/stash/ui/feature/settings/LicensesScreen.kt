package com.ashish.stash.ui.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashish.stash.ui.theme.StashBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesScreen(onNavigateBack: () -> Unit) {
    val libraries = listOf(
        LicenseItem("Jetpack Compose", "Android's modern toolkit for native UI.", Icons.Default.Layers, "Apache 2.0"),
        LicenseItem("Room Database", "Fluent SQLite database access.", Icons.Default.Storage, "Apache 2.0"),
        LicenseItem("Dagger Hilt", "Dependency injection for Android.", Icons.Default.Extension, "Apache 2.0"),
        LicenseItem("Google ML Kit", "On-device machine learning for OCR.", Icons.Default.AutoAwesome, "Proprietary"),
        LicenseItem("Kotlin Coroutines", "Asynchronous programming simplified.", Icons.Default.Bolt, "Apache 2.0"),
        LicenseItem("Coil", "Image loading for Android backed by Coroutines.", Icons.Default.Image, "Apache 2.0"),
        LicenseItem("WorkManager", "Persistent background processing.", Icons.Default.Sync, "Apache 2.0"),
        LicenseItem("DataStore", "Modern data storage solution.", Icons.Default.Dataset, "Apache 2.0")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Open Source Licenses") },
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
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Stash is built using the following high-quality open source libraries:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(libraries) { library ->
                LicenseCard(library)
            }
        }
    }
}

data class LicenseItem(val name: String, val description: String, val icon: ImageVector, val license: String)

@Composable
private fun LicenseCard(item: LicenseItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StashBlue.copy(alpha = 0.05f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(item.icon, contentDescription = null, tint = StashBlue, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Badge(containerColor = StashBlue.copy(alpha = 0.1f), contentColor = StashBlue) {
                Text(item.license, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

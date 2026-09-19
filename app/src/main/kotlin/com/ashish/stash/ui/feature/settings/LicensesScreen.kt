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
    val licenses = listOf(
        LicenseItem("Jetpack Compose", "Android UI Toolkit", Icons.Default.Layers),
        LicenseItem("Kotlin Coroutines", "Asynchronous programming", Icons.Default.Terminal),
        LicenseItem("Hilt", "Dependency Injection", Icons.Default.Extension),
        LicenseItem("Room", "Local Database", Icons.Default.Storage),
        LicenseItem("ML Kit", "On-device Machine Learning", Icons.Default.AutoAwesome),
        LicenseItem("Coil", "Image Loading Library", Icons.Default.Image),
        LicenseItem("DataStore", "User Preferences", Icons.Default.Settings),
        LicenseItem("WorkManager", "Background Processing", Icons.Default.Sync)
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
                    "Stash is built using the following open source libraries:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(licenses) { license ->
                LicenseCard(license)
            }
        }
    }
}

data class LicenseItem(val name: String, val description: String, val icon: ImageVector)

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
            Column {
                Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

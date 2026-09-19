package com.ashish.stash.ui.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.GppGood
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ashish.stash.ui.theme.StashBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.GppGood,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = StashBlue
            )

            Text(
                text = "Privacy is our Foundation",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = StashBlue
            )

            Text(
                text = "Stash is a completely offline document vault. We believe your sensitive data should never leave your device.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider()

            PrivacySection(
                icon = Icons.Default.CloudOff,
                title = "100% Offline",
                description = "Stash does not have internet access. No documents, metadata, or search queries are ever uploaded to a cloud or external server. No analytics, no tracking."
            )

            PrivacySection(
                icon = Icons.Default.Storage,
                title = "Local Intelligence",
                description = "OCR and search indexing happen entirely on your device. We use on-device AI models to read text inside your files."
            )

            PrivacySection(
                icon = Icons.Default.Fingerprint,
                title = "Hardware Security",
                description = "Your vault is protected by PBKDF2 hashing and native Android Biometrics. We use secure internal storage to keep your database private from other apps."
            )

            Surface(
                color = StashBlue.copy(alpha = 0.05f),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Responsibility",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StashBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Because Stash is 100% offline, we cannot recover your PIN or your data if the app is uninstalled. Please use our manual Backup feature in Settings to keep your metadata safe.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun PrivacySection(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = StashBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

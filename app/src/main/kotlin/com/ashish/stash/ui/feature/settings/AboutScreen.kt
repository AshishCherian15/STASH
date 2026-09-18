package com.ashish.stash.ui.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ashish.stash.ui.component.StashLogo
import com.ashish.stash.ui.theme.StashBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    onLicensesClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onHelpFaqClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StashLogo(modifier = Modifier.size(120.dp))
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Stash",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = StashBlue
            )
            
            Text(
                text = "Private Document Vault v2.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Developed by",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline
            )
            
            Text(
                text = "Ashish Cherian",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Developer Links
            DeveloperLinkItem(
                icon = Icons.Default.Code,
                label = "GitHub",
                value = "github.com/AshishCherian15",
                onClick = { uriHandler.openUri("https://github.com/AshishCherian15/") }
            )

            DeveloperLinkItem(
                icon = Icons.Default.Email,
                label = "Email",
                value = "ashishcherian15@gmail.com",
                onClick = { uriHandler.openUri("mailto:ashishcherian15@gmail.com") }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Stash is an offline-first document indexer designed for students, professionals, and researchers who value privacy and speed.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Secondary Actions
            OutlinedButton(
                onClick = onLicensesClick, 
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = StashBlue)
            ) {
                Text("Open Source Licenses")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onPrivacyClick, 
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = StashBlue)
            ) {
                Text("Privacy Policy")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onHelpFaqClick, 
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = StashBlue)
            ) {
                Text("Help & FAQ")
            }

            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "© 2026 Stash Project",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun DeveloperLinkItem(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = StashBlue.copy(alpha = 0.05f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = StashBlue)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelMedium, color = StashBlue)
                Text(value, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

package com.ashish.stash.ui.feature.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ashish.stash.BuildConfig
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
    val context = LocalContext.current
    val repoUrl = "https://github.com/AshishCherian15/STASH/"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About Stash") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
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
            Spacer(modifier = Modifier.height(16.dp))
            Text("STASH", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = StashBlue)
            Text("Version ${BuildConfig.VERSION_NAME}", style = MaterialTheme.typography.bodyMedium)
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                "Stash is a high-performance document vault for professional indexing and local intelligence. It provides desktop-class search for your mobile device.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repoUrl))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
            ) {
                Icon(Icons.Default.Code, null)
                Spacer(Modifier.width(8.dp))
                Text("Source Code & Updates")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onLicensesClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Source Licenses")
            }

            Spacer(modifier = Modifier.height(48.dp))
            
            Text("Created with ❤️ by Ashish Cherian", style = MaterialTheme.typography.labelLarge)
            Text("Secure. Offline. Local AI.", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}

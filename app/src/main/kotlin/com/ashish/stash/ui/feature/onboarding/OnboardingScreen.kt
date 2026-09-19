package com.ashish.stash.ui.feature.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ashish.stash.ui.component.rememberSafFolderPickerLauncher
import com.ashish.stash.ui.theme.StashBlue
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 6 })
    val scope = rememberCoroutineScope()
    var selectedFolderUri by remember { mutableStateOf<Uri?>(null) }

    val folderPicker = rememberSafFolderPickerLauncher { uri ->
        selectedFolderUri = uri
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { onComplete() }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            Column {
                Row(
                    Modifier.height(40.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(6) { iteration ->
                        val color = if (pagerState.currentPage == iteration) StashBlue else StashBlue.copy(alpha = 0.2f)
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { viewModel.completeOnboarding(null) },
                        colors = ButtonDefaults.textButtonColors(contentColor = StashBlue)
                    ) {
                        Text("Skip")
                    }

                    Button(
                        onClick = {
                            if (pagerState.currentPage < 5) {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            } else {
                                viewModel.completeOnboarding(selectedFolderUri)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
                    ) {
                        Text(if (pagerState.currentPage < 5) "Next" else "Finish")
                    }
                }
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    title = "Secure Your Documents",
                    description = "STASH is a professional vault for your sensitive files. Built with a focus on privacy and speed.",
                    icon = Icons.Default.Lock
                )
                1 -> OnboardingPage(
                    title = "Cloud-Ready Distribution",
                    description = "Check for updates and fetch the latest build directly from GitHub Releases.",
                    icon = Icons.Default.CloudSync
                )
                2 -> OnboardingPage(
                    title = "Intelligent Indexing",
                    description = "Index files in place. Search text inside images and PDFs using local AI.",
                    icon = Icons.Default.AutoAwesome
                )
                3 -> OnboardingPage(
                    title = "Storage Permissions",
                    description = "STASH requires 'All Files Access' to scan and index documents across your device.",
                    icon = Icons.Default.Storage,
                    isAction = true,
                    actionText = "Grant Full Access",
                    onAction = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                            context.startActivity(intent)
                        }
                    }
                )
                4 -> OnboardingPage(
                    title = "Choose Vault Folder",
                    description = "Select a folder on your device where STASH should look for documents.",
                    icon = Icons.Default.FolderOpen,
                    isAction = true,
                    actionText = if (selectedFolderUri == null) "Select Folder" else "Folder Selected ✅",
                    onAction = { folderPicker.launch(null) }
                )
                5 -> OnboardingPage(
                    title = "Ready to Begin",
                    description = "Your privacy is our priority. STASH will now set up your local database.",
                    icon = Icons.Default.VerifiedUser
                )
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    title: String,
    description: String,
    icon: ImageVector,
    isAction: Boolean = false,
    actionText: String = "",
    onAction: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(140.dp),
            tint = StashBlue
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = StashBlue
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (isAction) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
            ) {
                Text(actionText)
            }
        }
    }
}

package com.ashish.stash.ui.feature.onboarding

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.ashish.stash.ui.theme.StashBlue
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { 5 })
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            Column {
                // Page Indicator
                Row(
                    Modifier.height(40.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(5) { iteration ->
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
                        onClick = { onComplete() },
                        colors = ButtonDefaults.textButtonColors(contentColor = StashBlue)
                    ) {
                        Text("Skip")
                    }

                    Button(
                        onClick = {
                            if (pagerState.currentPage < 4) {
                                scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                            } else {
                                onComplete()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
                    ) {
                        Text(if (pagerState.currentPage < 4) "Next" else "Get Started")
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
                    description = "Stash is a private, offline vault for your sensitive files. No cloud, no tracking.",
                    icon = Icons.Default.Lock
                )
                1 -> OnboardingPage(
                    title = "Privacy First",
                    description = "We promise never to upload your data. Your files stay exactly where they are on your device.",
                    icon = Icons.Default.Shield
                )
                2 -> OnboardingPage(
                    title = "Smart Indexing",
                    description = "Stash indexes your documents in place. Search by content, labels, and categories instantly.",
                    icon = Icons.Default.Inventory
                )
                3 -> OnboardingPage(
                    title = "Full Storage Access",
                    description = "To search across all your documents instantly, Stash needs permission to scan your device storage.",
                    icon = Icons.Default.Storage,
                    isPermissionRequest = true,
                    onActionClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (!Environment.isExternalStorageManager()) {
                                try {
                                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                                    intent.addCategory("android.intent.category.DEFAULT")
                                    intent.data = Uri.parse("package:${context.packageName}")
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    val intent = Intent()
                                    intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                                    context.startActivity(intent)
                                }
                            }
                        }
                    }
                )
                4 -> OnboardingPage(
                    title = "Organized Workflow",
                    description = "Group files with professional colors. Categorize by Legal, Financial, or Medical records.",
                    icon = Icons.Default.Category,
                    isExplainer = true
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
    isExplainer: Boolean = false,
    isPermissionRequest: Boolean = false,
    onActionClick: () -> Unit = {}
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
        if (isExplainer) {
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                color = StashBlue.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Professional Grade Organization.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = StashBlue
                )
            }
        }
        if (isPermissionRequest) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
            ) {
                Text("Grant Full Access")
            }
        }
    }
}

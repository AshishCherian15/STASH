package com.ashish.stash.ui.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashish.stash.ui.component.StashLogo
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            StashLogo(modifier = Modifier.size(180.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "STASH",
                style = MaterialTheme.typography.displayMedium,
                color = StashBlue,
                fontWeight = FontWeight.Bold,
                letterSpacing = 8.sp
            )
            Text(
                text = "Intelligence Document Vault",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray,
                letterSpacing = 1.sp
            )
        }
    }
}

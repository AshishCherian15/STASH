package com.ashish.stash.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun PinKeypad(
    onDigitClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
    onBiometricClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val rows = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9)
        )

        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                row.forEach { digit ->
                    KeypadButton(text = digit.toString()) { onDigitClick(digit) }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            // Left action (Biometric)
            if (onBiometricClick != null) {
                IconButton(
                    onClick = onBiometricClick,
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = "Biometric", tint = StashBlue, modifier = Modifier.size(32.dp))
                }
            } else {
                Spacer(modifier = Modifier.size(72.dp))
            }

            KeypadButton(text = "0") { onDigitClick(0) }

            // Right action (Backspace)
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(72.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun KeypadButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.size(72.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp)
            )
        }
    }
}

@Composable
fun PinIndicators(
    pinLength: Int,
    maxDigits: Int = 4,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(maxDigits) { index ->
            val filled = index < pinLength
            val color = when {
                isError -> MaterialTheme.colorScheme.error
                filled -> StashBlue
                else -> MaterialTheme.colorScheme.outlineVariant
            }
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color, CircleShape)
            )
        }
    }
}

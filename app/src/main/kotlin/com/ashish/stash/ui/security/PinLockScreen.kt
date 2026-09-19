package com.ashish.stash.ui.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ashish.stash.core.security.PinFlow
import com.ashish.stash.ui.component.PinIndicators
import com.ashish.stash.ui.component.PinKeypad
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun PinLockScreen(
    onCorrectPin: () -> Unit,
    onBiometricRequest: () -> Unit,
    viewModel: SecurityViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val title = when (uiState.flow) {
        PinFlow.UNLOCK -> "Vault Locked"
        PinFlow.SETUP -> "Setup Vault PIN"
        PinFlow.CHANGE -> "Change Vault PIN"
    }

    val subtitle = when {
        uiState.lockoutSeconds > 0 -> "Locked for ${uiState.lockoutSeconds}s"
        uiState.isError -> uiState.errorMessage ?: "Error"
        uiState.isConfirming -> "Confirm your 4-digit PIN"
        uiState.flow == PinFlow.UNLOCK -> "Enter your PIN to access Stash"
        else -> "Create a 4-digit PIN"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = StashBlue
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = StashBlue
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = if (uiState.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        PinIndicators(
            pinLength = uiState.enteredPin.length,
            isError = uiState.isError,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        PinKeypad(
            onDigitClick = viewModel::onDigit,
            onDeleteClick = viewModel::onDelete,
            onBiometricClick = if (uiState.flow == PinFlow.UNLOCK) onBiometricRequest else null,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}

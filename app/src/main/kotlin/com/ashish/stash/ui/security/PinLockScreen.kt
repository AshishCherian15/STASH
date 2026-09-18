package com.ashish.stash.ui.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun PinLockScreen(
    onCorrectPin: () -> Unit,
    savedPin: String
) {
    var pinInput by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            text = "Vault Locked",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter your PIN to access Stash",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = pinInput,
            onValueChange = {
                if (it.length <= 4) {
                    pinInput = it
                    isError = false
                    if (it == savedPin) {
                        onCorrectPin()
                    }
                }
            },
            label = { Text("Enter 4-digit PIN") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            isError = isError,
            singleLine = true,
            modifier = Modifier.width(200.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = StashBlue,
                focusedLabelColor = StashBlue
            )
        )

        if (isError) {
            Text(
                "Incorrect PIN",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

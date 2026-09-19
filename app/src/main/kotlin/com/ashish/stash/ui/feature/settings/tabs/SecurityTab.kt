package com.ashish.stash.ui.feature.settings.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun SecurityTab(
    isPinSet: Boolean,
    biometricEnabled: Boolean,
    preventScreenshots: Boolean,
    autoLockTimeout: Long,
    onUpdatePin: (String?, String) -> Unit,
    onDisablePin: (String) -> Unit,
    onBiometricToggle: (Boolean) -> Unit,
    onPreventScreenshotsChange: (Boolean) -> Unit,
    onTimeoutChange: (Long) -> Unit
) {
    var showPinDialog by remember { mutableStateOf(false) }
    var showTimeoutDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Vault Security", style = MaterialTheme.typography.titleMedium, color = StashBlue)
        Spacer(Modifier.height(16.dp))
        
        ListItem(
            headlineContent = { Text("PIN Lock") },
            supportingContent = { Text(if (isPinSet) "PIN configured and active" else "Setup a 4-digit PIN for access") },
            trailingContent = {
                Button(
                    onClick = { showPinDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
                ) {
                    Text(if (isPinSet) "Change" else "Setup")
                }
            }
        )
        
        if (isPinSet) {
            ListItem(
                headlineContent = { Text("Remove Lock") },
                trailingContent = {
                    var showDisableDialog by remember { mutableStateOf(false) }
                    TextButton(onClick = { showDisableDialog = true }) {
                        Text("Disable", color = Color.Red)
                    }
                    if (showDisableDialog) {
                        var pinInput by remember { mutableStateOf("") }
                        AlertDialog(
                            onDismissRequest = { showDisableDialog = false },
                            title = { Text("Disable PIN") },
                            text = {
                                OutlinedTextField(
                                    value = pinInput,
                                    onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) pinInput = it },
                                    label = { Text("Confirm current PIN") }
                                )
                            },
                            confirmButton = {
                                Button(onClick = { onDisablePin(pinInput); showDisableDialog = false }, enabled = pinInput.length == 4) {
                                    Text("Disable")
                                }
                            },
                            dismissButton = { TextButton(onClick = { showDisableDialog = false }) { Text("Cancel") } }
                        )
                    }
                }
            )
        }

        ListItem(
            headlineContent = { Text("Biometric Unlock") },
            supportingContent = { Text("Use fingerprint or face if available") },
            trailingContent = { Switch(checked = biometricEnabled, onCheckedChange = onBiometricToggle, colors = SwitchDefaults.colors(checkedTrackColor = StashBlue)) }
        )

        ListItem(
            headlineContent = { Text("Auto-Lock Timeout") },
            supportingContent = { 
                val label = when(autoLockTimeout) {
                    0L -> "Immediately"
                    15000L -> "15 seconds"
                    30000L -> "30 seconds"
                    60000L -> "1 minute"
                    300000L -> "5 minutes"
                    else -> "Custom"
                }
                Text("Lock app after $label") 
            },
            modifier = Modifier.clickable { showTimeoutDialog = true }
        )

        ListItem(
            headlineContent = { Text("Prevent Screenshots") },
            supportingContent = { Text("Hide app content in Recent Apps and recordings") },
            trailingContent = { Switch(checked = preventScreenshots, onCheckedChange = onPreventScreenshotsChange, colors = SwitchDefaults.colors(checkedTrackColor = StashBlue)) }
        )
    }

    if (showPinDialog) {
        var oldPinInput by remember { mutableStateOf("") }
        var newPinInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text(if (isPinSet) "Change PIN" else "Setup PIN") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (isPinSet) {
                        OutlinedTextField(
                            value = oldPinInput,
                            onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) oldPinInput = it },
                            label = { Text("Current PIN") }
                        )
                    }
                    OutlinedTextField(
                        value = newPinInput,
                        onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) newPinInput = it },
                        label = { Text("New 4-digit PIN") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdatePin(if (isPinSet) oldPinInput else null, newPinInput)
                        showPinDialog = false
                    },
                    enabled = newPinInput.length == 4 && (!isPinSet || oldPinInput.length == 4),
                    colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showTimeoutDialog) {
        val options = listOf(
            0L to "Immediately",
            15000L to "15 seconds",
            30000L to "30 seconds",
            60000L to "1 minute",
            300000L to "5 minutes"
        )
        AlertDialog(
            onDismissRequest = { showTimeoutDialog = false },
            title = { Text("Auto-Lock Timeout") },
            text = {
                Column {
                    options.forEach { (time, label) ->
                        Row(
                            Modifier.fillMaxWidth().clickable { 
                                onTimeoutChange(time)
                                showTimeoutDialog = false
                            }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = autoLockTimeout == time, onClick = null, colors = RadioButtonDefaults.colors(selectedColor = StashBlue))
                            Spacer(Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}

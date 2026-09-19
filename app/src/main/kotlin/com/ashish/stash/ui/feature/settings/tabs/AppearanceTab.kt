package com.ashish.stash.ui.feature.settings.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun AppearanceTab(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    themeColor: String,
    fontFamily: String,
    fontSizeScale: Float,
    onDarkThemeChange: (Boolean) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onThemeColorChange: (String) -> Unit,
    onFontFamilyChange: (String) -> Unit,
    onFontSizeChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Visual Preference", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))
        
        ListItem(
            headlineContent = { Text("Dark Mode") },
            supportingContent = { Text("High-contrast premium black theme") },
            trailingContent = { Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange) }
        )
        ListItem(
            headlineContent = { Text("Dynamic Color") },
            supportingContent = { Text("Match your Android system colors") },
            trailingContent = { Switch(checked = dynamicColor, onCheckedChange = onDynamicColorChange) }
        )

        Spacer(Modifier.height(24.dp))
        Text("App Accent Color", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))
        val accentColors = listOf(
            "BLUE" to StashBlue, 
            "GREEN" to Color(0xFF4CAF50), 
            "RED" to Color(0xFFF44336), 
            "ORANGE" to Color(0xFFFF9800),
            "PURPLE" to Color(0xFF9C27B0)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            accentColors.forEach { (name, color) ->
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(color, CircleShape)
                        .clickable { onThemeColorChange(name) },
                    contentAlignment = Alignment.Center
                ) {
                    if (themeColor == name) {
                        Icon(Icons.Default.Check, null, tint = Color.White)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Typography", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        val fonts = listOf(
            "SANS_SERIF" to "Inter (Professional)", 
            "SERIF" to "Fraunces (Classic)", 
            "MONOSPACE" to "JetBrains (Code)"
        )
        fonts.forEach { (id, name) ->
            Row(
                Modifier.fillMaxWidth().clickable { onFontFamilyChange(id) }.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = fontFamily == id, onClick = null)
                Spacer(Modifier.width(12.dp))
                Text(name)
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Text Scale: ${(fontSizeScale * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Slider(
            value = fontSizeScale,
            onValueChange = onFontSizeChange,
            valueRange = 0.8f..1.5f,
            steps = 6
        )
        
        Spacer(Modifier.height(40.dp))
    }
}

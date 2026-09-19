package com.ashish.stash.ui.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.entity.FolderEntity
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.ui.component.rememberSafFilePickerLauncher
import com.ashish.stash.ui.feature.settings.tabs.CategoriesTab
import com.ashish.stash.ui.feature.settings.tabs.FoldersTab
import com.ashish.stash.ui.feature.settings.tabs.LabelsTab
import com.ashish.stash.ui.theme.VaultBrass
import com.ashish.stash.ui.theme.StashBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToLicenses: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var currentSubScreen by remember { mutableStateOf<SubScreen?>(null) }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    val backupPicker = rememberSafFilePickerLauncher(
        onFileSelected = { uri ->
            val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: ""
            viewModel.importBackup(jsonString)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentSubScreen?.title ?: "Settings") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentSubScreen != null) {
                            currentSubScreen = null
                        } else {
                            onNavigateBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = StashBlue,
                    navigationIconContentColor = StashBlue
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (currentSubScreen == null) {
                SettingsMainMenu(
                    onNavigateToSubScreen = { currentSubScreen = it },
                    onNavigateToAbout = onNavigateToAbout,
                    onNavigateToPrivacy = onNavigateToPrivacy,
                    onNavigateToHelp = onNavigateToHelp,
                    onNavigateToLicenses = onNavigateToLicenses
                )
            } else {
                when (currentSubScreen) {
                    SubScreen.CATEGORIES -> CategoriesTab(uiState.categories, viewModel::addCategory, viewModel::updateCategory, viewModel::deleteCategory)
                    SubScreen.FOLDERS -> FoldersTab(uiState.folders, viewModel::addFolder, viewModel::updateFolder, viewModel::deleteFolder)
                    SubScreen.LABELS -> LabelsTab(uiState.labels, viewModel::addLabel, viewModel::updateLabel, viewModel::deleteLabel)
                    SubScreen.APPEARANCE -> AppearanceTab(
                        darkTheme = uiState.darkTheme,
                        dynamicColor = uiState.dynamicColor,
                        themeColor = uiState.themeColor,
                        fontFamily = uiState.fontFamily,
                        fontSizeScale = uiState.fontSizeScale,
                        onDarkThemeChange = viewModel::setDarkTheme,
                        onDynamicColorChange = viewModel::setDynamicColor,
                        onThemeColorChange = viewModel::setThemeColor,
                        onFontFamilyChange = viewModel::setFontFamily,
                        onFontSizeChange = viewModel::setFontSizeScale
                    )
                    SubScreen.SECURITY -> SecurityTab(
                        vaultPin = uiState.vaultPin,
                        biometricEnabled = uiState.biometricEnabled,
                        preventScreenshots = uiState.preventScreenshots,
                        autoLockTimeout = uiState.autoLockTimeoutMillis,
                        onVaultPinChange = viewModel::setVaultPin,
                        onBiometricToggle = viewModel::setBiometricEnabled,
                        onPreventScreenshotsChange = viewModel::setPreventScreenshots,
                        onTimeoutChange = viewModel::setAutoLockTimeoutMillis
                    )
                    SubScreen.BACKUP -> BackupTab(
                        onExport = viewModel::exportBackup,
                        onImport = { backupPicker.launch(arrayOf("application/json")) }
                    )
                    null -> {}
                }
            }
        }
    }
}

enum class SubScreen(val title: String) {
    CATEGORIES("Categories"),
    FOLDERS("Folders"),
    LABELS("Labels"),
    APPEARANCE("Appearance"),
    SECURITY("Security"),
    BACKUP("Backup")
}

@Composable
fun SettingsMainMenu(
    onNavigateToSubScreen: (SubScreen) -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToLicenses: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { SettingsItem("Categories", "Manage document categories", Icons.Default.Category) { onNavigateToSubScreen(SubScreen.CATEGORIES) } }
        item { SettingsItem("Folders", "Manage document folders", Icons.Default.Folder) { onNavigateToSubScreen(SubScreen.FOLDERS) } }
        item { SettingsItem("Labels", "Manage document labels", Icons.Default.Label) { onNavigateToSubScreen(SubScreen.LABELS) } }
        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        item { SettingsItem("Appearance", "Theme and colors", Icons.Default.Palette) { onNavigateToSubScreen(SubScreen.APPEARANCE) } }
        item { SettingsItem("Security", "Vault lock and privacy", Icons.Default.Lock) { onNavigateToSubScreen(SubScreen.SECURITY) } }
        item { SettingsItem("Backup", "Export and restore data", Icons.Default.Backup) { onNavigateToSubScreen(SubScreen.BACKUP) } }
        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        item { SettingsItem("Help & FAQ", "How to use the app", Icons.Default.Help) { onNavigateToHelp() } }
        item { SettingsItem("Privacy Policy", "Your data safety", Icons.Default.PrivacyTip) { onNavigateToPrivacy() } }
        item { SettingsItem("Licenses", "Open source libraries", Icons.Default.Description) { onNavigateToLicenses() } }
        item { SettingsItem("About Stash", "App info and developer", Icons.Default.Info) { onNavigateToAbout() } }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, contentDescription = null, tint = StashBlue) },
        trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp)) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

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
        Text("Theme Settings", style = MaterialTheme.typography.titleMedium, color = StashBlue)
        Spacer(Modifier.height(16.dp))
        
        ListItem(
            headlineContent = { Text("Dark Mode") },
            supportingContent = { Text("Force high-contrast black theme") },
            trailingContent = { Switch(checked = darkTheme, onCheckedChange = onDarkThemeChange, colors = SwitchDefaults.colors(checkedTrackColor = StashBlue)) }
        )
        ListItem(
            headlineContent = { Text("Dynamic Color") },
            supportingContent = { Text("Sync with Android system palette") },
            trailingContent = { Switch(checked = dynamicColor, onCheckedChange = onDynamicColorChange, colors = SwitchDefaults.colors(checkedTrackColor = StashBlue)) }
        )

        Spacer(Modifier.height(24.dp))
        Text("Primary Color", style = MaterialTheme.typography.labelLarge, color = StashBlue)
        Spacer(Modifier.height(8.dp))
        val colors = listOf("BLUE" to StashBlue, "GREEN" to Color(0xFF4CAF50), "RED" to Color(0xFFF44336), "ORANGE" to Color(0xFFFF9800))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            colors.forEach { (name, color) ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, CircleShape)
                        .clickable { onThemeColorChange(name) }
                        .let { if (themeColor == name) it.background(Color.Black.copy(alpha = 0.2f)) else it }
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Font Family", style = MaterialTheme.typography.labelLarge, color = StashBlue)
        val fonts = listOf("SANS_SERIF" to "Inter (Sans)", "SERIF" to "Fraunces (Serif)", "MONOSPACE" to "JetBrains (Mono)")
        fonts.forEach { (id, name) ->
            Row(
                Modifier.fillMaxWidth().clickable { onFontFamilyChange(id) }.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(selected = fontFamily == id, onClick = null, colors = RadioButtonDefaults.colors(selectedColor = StashBlue))
                Spacer(Modifier.width(12.dp))
                Text(name)
            }
        }

        Spacer(Modifier.height(24.dp))
        Text("Font Size: ${(fontSizeScale * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = StashBlue)
        Slider(
            value = fontSizeScale,
            onValueChange = onFontSizeChange,
            valueRange = 0.8f..1.5f,
            steps = 6,
            colors = SliderDefaults.colors(thumbColor = StashBlue, activeTrackColor = StashBlue)
        )
    }
}

@Composable
fun SecurityTab(
    vaultPin: String?,
    biometricEnabled: Boolean,
    preventScreenshots: Boolean,
    autoLockTimeout: Long,
    onVaultPinChange: (String?) -> Unit,
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
            supportingContent = { Text(if (vaultPin != null) "PIN configured and active" else "Setup a 4-digit PIN for access") },
            trailingContent = {
                Button(
                    onClick = { showPinDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
                ) {
                    Text(if (vaultPin != null) "Change" else "Setup")
                }
            }
        )
        
        if (vaultPin != null) {
            ListItem(
                headlineContent = { Text("Remove Lock") },
                trailingContent = {
                    TextButton(onClick = { onVaultPinChange(null) }) {
                        Text("Disable", color = Color.Red)
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
        var pinInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            title = { Text("Vault PIN") },
            text = {
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) pinInput = it },
                    label = { Text("Enter 4-digit PIN") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = StashBlue)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (pinInput.length == 4) {
                            onVaultPinChange(pinInput)
                            showPinDialog = false
                        }
                    },
                    enabled = pinInput.length == 4,
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

@Composable
fun BackupTab(
    onExport: () -> Unit,
    onImport: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Data Management", style = MaterialTheme.typography.titleMedium, color = StashBlue)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onExport, 
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
        ) {
            Text("Export Index Metadata")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = onImport, 
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = StashBlue)
        ) {
            Text("Restore from Backup")
        }
    }
}

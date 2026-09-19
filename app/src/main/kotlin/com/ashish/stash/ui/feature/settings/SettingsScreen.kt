package com.ashish.stash.ui.feature.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ashish.stash.core.github.GitHubManager
import com.ashish.stash.ui.component.rememberSafFilePickerLauncher
import com.ashish.stash.ui.feature.settings.tabs.*
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
    val releaseInfo by viewModel.releaseInfo.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var currentSubScreen by remember { mutableStateOf<SubScreen?>(null) }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    val backupPicker = rememberSafFilePickerLauncher { uri ->
        val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() } ?: ""
        viewModel.importBackup(jsonString)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentSubScreen?.title ?: "Settings") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentSubScreen != null) currentSubScreen = null else onNavigateBack()
                    }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = StashBlue,
                    navigationIconContentColor = StashBlue
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                        isPinSet = uiState.isPinSet,
                        biometricEnabled = uiState.biometricEnabled,
                        preventScreenshots = uiState.preventScreenshots,
                        autoLockTimeout = uiState.autoLockTimeoutMillis,
                        onUpdatePin = viewModel::updatePin,
                        onDisablePin = viewModel::disablePin,
                        onBiometricToggle = viewModel::setBiometricEnabled,
                        onPreventScreenshotsChange = viewModel::setPreventScreenshots,
                        onTimeoutChange = viewModel::setAutoLockTimeoutMillis
                    )
                    SubScreen.STORAGE -> StorageTab()
                    SubScreen.BACKUP -> BackupTab(
                        onExport = viewModel::exportBackup,
                        onImport = { backupPicker.launch(arrayOf("application/json")) }
                    )
                    SubScreen.FEEDBACK -> FeedbackTab(onSubmit = viewModel::submitFeedback)
                    null -> {}
                }
            }
        }
    }

    // Update Dialog
    releaseInfo?.let { info ->
        if (info.isNewer) {
            AlertDialog(
                onDismissRequest = { viewModel.checkUpdates() },
                title = { Text("Update Available") },
                text = { Text("A new version (${info.tagName}) is available on GitHub. Would you like to download the APK?") },
                confirmButton = {
                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(info.downloadUrl))
                        context.startActivity(intent)
                    }) { Text("Download") }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.checkUpdates() }) { Text("Later") }
                }
            )
        }
    }
}

enum class SubScreen(val title: String) {
    CATEGORIES("Categories"),
    FOLDERS("Folders"),
    LABELS("Labels"),
    APPEARANCE("Appearance"),
    SECURITY("Security"),
    STORAGE("Storage"),
    BACKUP("Backup"),
    FEEDBACK("Feedback")
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
        item { SettingsItem("Storage", "File access and scanning", Icons.Default.Storage) { onNavigateToSubScreen(SubScreen.STORAGE) } }
        item { SettingsItem("Backup", "Export and restore data", Icons.Default.Backup) { onNavigateToSubScreen(SubScreen.BACKUP) } }
        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }
        item { SettingsItem("Feedback", "Send issues to GitHub", Icons.Default.Feedback) { onNavigateToSubScreen(SubScreen.FEEDBACK) } }
        item { SettingsItem("Help & FAQ", "How to use the app", Icons.Default.Help) { onNavigateToHelp() } }
        item { SettingsItem("Privacy Policy", "Your data safety", Icons.Default.PrivacyTip) { onNavigateToPrivacy() } }
        item { SettingsItem("Licenses", "Open source libraries", Icons.Default.Description) { onNavigateToLicenses() } }
        item { SettingsItem("About Stash", "App version and developer", Icons.Default.Info) { onNavigateToAbout() } }
    }
}

@Composable
fun SettingsItem(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        leadingContent = { Icon(icon, null, tint = StashBlue) },
        trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, modifier = Modifier.size(16.dp)) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun StorageTab() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasFullAccess by remember { mutableStateOf(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Environment.isExternalStorageManager() else true) }
    
    // Refresh status when returning to foreground
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasFullAccess = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Environment.isExternalStorageManager() else true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Storage Permissions", style = MaterialTheme.typography.titleMedium, color = StashBlue)
        Spacer(Modifier.height(16.dp))
        
        ListItem(
            headlineContent = { Text("All Files Access") },
            supportingContent = { Text("Required for deep-search and background indexing across your entire device storage.") },
            trailingContent = {
                Switch(checked = hasFullAccess, onCheckedChange = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                            data = Uri.parse("package:${context.packageName}")
                        }
                        context.startActivity(intent)
                    }
                })
            }
        )
        
        Spacer(Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (hasFullAccess) Color(0xFFE8F5E9) else Color(0xFFFDECEA))
        ) {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    if (hasFullAccess) Icons.Default.CheckCircle else Icons.Default.Warning,
                    null,
                    tint = if (hasFullAccess) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (hasFullAccess) "Deep indexing is active." else "Full storage access needed for deep-search features.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (hasFullAccess) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
        }
    }
}

@Composable
fun FeedbackTab(onSubmit: (String, Int) -> Unit) {
    var text by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(5) }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Your Feedback", style = MaterialTheme.typography.titleMedium, color = StashBlue)
        Spacer(Modifier.height(16.dp))
        Text("How is your experience with STASH?", style = MaterialTheme.typography.labelLarge)
        Row {
            repeat(5) { index ->
                IconButton(onClick = { rating = index + 1 }) {
                    Icon(
                        if (index < rating) Icons.Default.Star else Icons.Default.StarOutline,
                        null,
                        tint = if (index < rating) StashBlue else Color.Gray
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Describe your issue or suggestion") },
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = StashBlue)
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onSubmit(text, rating); text = "" },
            enabled = text.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = StashBlue)
        ) { Text("Submit to GitHub Issues") }
    }
}

@Composable
fun BackupTab(onExport: () -> Unit, onImport: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Data Management", style = MaterialTheme.typography.titleMedium, color = StashBlue)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onExport, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = StashBlue)) {
            Icon(Icons.Default.Backup, null)
            Spacer(Modifier.width(8.dp))
            Text("Export Index Metadata")
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = onImport, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.Restore, null)
            Spacer(Modifier.width(8.dp))
            Text("Restore from Backup")
        }
    }
}

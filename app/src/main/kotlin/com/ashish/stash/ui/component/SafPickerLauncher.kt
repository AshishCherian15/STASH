package com.ashish.stash.ui.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun rememberSafFilePickerLauncher(
    onFileSelected: (Uri) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocument(),
    onResult = { uri -> uri?.let(onFileSelected) }
)

@Composable
fun rememberSafMultiFilePickerLauncher(
    onFilesSelected: (List<Uri>) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenMultipleDocuments(),
    onResult = onFilesSelected
)

@Composable
fun rememberSafFolderPickerLauncher(
    onFolderSelected: (Uri) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.OpenDocumentTree(),
    onResult = { uri -> uri?.let(onFolderSelected) }
)

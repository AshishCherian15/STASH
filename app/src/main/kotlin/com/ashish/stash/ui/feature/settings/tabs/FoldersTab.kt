package com.ashish.stash.ui.feature.settings.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashish.stash.core.database.entity.FolderEntity

@Composable
fun FoldersTab(
    folders: List<FolderEntity>,
    onAdd: (String) -> Unit,
    onUpdate: (FolderEntity) -> Unit,
    onDelete: (FolderEntity) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingFolder by remember { mutableStateOf<FolderEntity?>(null) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        ListItem(
            headlineContent = { Text("Manage Folders") },
            trailingContent = {
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.CreateNewFolder, contentDescription = "Add Folder")
                }
            }
        )
        HorizontalDivider()
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(folders) { folder ->
                ListItem(
                    headlineContent = { Text(folder.name) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { editingFolder = folder }) {
                                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = { onDelete(folder) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Folder") },
            text = {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Folder Name") })
            },
            confirmButton = {
                Button(onClick = {
                    onAdd(name)
                    showAddDialog = false
                }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
    
    editingFolder?.let { folder ->
        var name by remember { mutableStateOf(folder.name) }
        AlertDialog(
            onDismissRequest = { editingFolder = null },
            title = { Text("Edit Folder") },
            text = {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Folder Name") })
            },
            confirmButton = {
                Button(onClick = {
                    onUpdate(folder.copy(name = name))
                    editingFolder = null
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { editingFolder = null }) { Text("Cancel") }
            }
        )
    }
}

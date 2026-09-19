package com.ashish.stash.ui.feature.settings.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ashish.stash.core.database.entity.FolderEntity
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun FoldersTab(
    folders: List<FolderEntity>,
    onAddFolder: (String, String) -> Unit,
    onUpdateFolder: (FolderEntity) -> Unit,
    onDeleteFolder: (FolderEntity) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var folderToEdit by remember { mutableStateOf<FolderEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Manage Folders", style = MaterialTheme.typography.titleMedium, color = StashBlue)
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Add Folder", tint = StashBlue)
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(folders) { folder ->
                FolderItem(
                    folder = folder,
                    onEdit = { folderToEdit = it },
                    onDelete = { onDeleteFolder(it) }
                )
            }
        }
    }

    if (showAddDialog) {
        FolderEditDialog(
            title = "Add Folder",
            onDismiss = { showAddDialog = false },
            onConfirm = { name, color ->
                onAddFolder(name, color)
                showAddDialog = false
            }
        )
    }

    if (folderToEdit != null) {
        FolderEditDialog(
            title = "Edit Folder",
            initialName = folderToEdit!!.name,
            initialColor = folderToEdit!!.color,
            onDismiss = { folderToEdit = null },
            onConfirm = { name, color ->
                onUpdateFolder(folderToEdit!!.copy(name = name, color = color))
                folderToEdit = null
            }
        )
    }
}

@Composable
fun FolderItem(
    folder: FolderEntity,
    onEdit: (FolderEntity) -> Unit,
    onDelete: (FolderEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Color(android.graphics.Color.parseColor(folder.color)))
            )
            Spacer(Modifier.width(12.dp))
            Icon(Icons.Default.Folder, null, tint = Color(android.graphics.Color.parseColor(folder.color)))
            Spacer(Modifier.width(12.dp))
            Text(folder.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            IconButton(onClick = { onEdit(folder) }) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(20.dp)) }
            IconButton(onClick = { onDelete(folder) }) { Icon(Icons.Default.Delete, null, modifier = Modifier.size(20.dp), tint = Color.Red.copy(alpha = 0.7f)) }
        }
    }
}

@Composable
fun FolderEditDialog(
    title: String,
    initialName: String = "",
    initialColor: String = "#246EE9",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var selectedColor by remember { mutableStateOf(initialColor) }
    
    val colors = listOf("#246EE9", "#4CAF50", "#F44336", "#FF9800", "#9C27B0", "#748393")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Folder Name") },
                    singleLine = true
                )
                
                Text("Theme Color", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colors.forEach { colorHex ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(colorHex)))
                                .clickable { selectedColor = colorHex }
                                .let { 
                                    if (selectedColor == colorHex) it.padding(2.dp).background(Color.Black, CircleShape).padding(2.dp).background(Color(android.graphics.Color.parseColor(colorHex)), CircleShape) 
                                    else it 
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, selectedColor) }, enabled = name.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

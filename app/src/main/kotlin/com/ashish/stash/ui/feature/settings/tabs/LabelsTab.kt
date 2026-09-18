package com.ashish.stash.ui.feature.settings.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.ashish.stash.core.database.entity.LabelEntity

@Composable
fun LabelsTab(
    labels: List<LabelEntity>,
    onAdd: (String) -> Unit,
    onUpdate: (LabelEntity) -> Unit,
    onDelete: (LabelEntity) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingLabel by remember { mutableStateOf<LabelEntity?>(null) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        ListItem(
            headlineContent = { Text("Manage Labels") },
            trailingContent = {
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        )
        HorizontalDivider()
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(labels) { label ->
                ListItem(
                    headlineContent = { Text(label.name) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { editingLabel = label }) {
                                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = { onDelete(label) }) {
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
            title = { Text("Add Label") },
            text = {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Label Name") })
            },
            confirmButton = {
                Button(onClick = {
                    onAdd(name)
                    showAddDialog = false
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
    
    editingLabel?.let { label ->
        var name by remember { mutableStateOf(label.name) }
        AlertDialog(
            onDismissRequest = { editingLabel = null },
            title = { Text("Edit Label") },
            text = {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Label Name") })
            },
            confirmButton = {
                Button(onClick = {
                    onUpdate(label.copy(name = name))
                    editingLabel = null
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { editingLabel = null }) { Text("Cancel") }
            }
        )
    }
}

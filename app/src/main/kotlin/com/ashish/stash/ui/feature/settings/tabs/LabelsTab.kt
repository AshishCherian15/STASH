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
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.ui.theme.StashBlue

@Composable
fun LabelsTab(
    labels: List<LabelEntity>,
    onAddLabel: (String, String) -> Unit,
    onUpdateLabel: (LabelEntity) -> Unit,
    onDeleteLabel: (LabelEntity) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var labelToEdit by remember { mutableStateOf<LabelEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Manage Labels", style = MaterialTheme.typography.titleMedium, color = StashBlue)
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, "Add Label", tint = StashBlue)
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(labels) { label ->
                LabelItem(
                    label = label,
                    onEdit = { labelToEdit = it },
                    onDelete = { onDeleteLabel(it) }
                )
            }
        }
    }

    if (showAddDialog) {
        LabelEditDialog(
            title = "Add Label",
            onDismiss = { showAddDialog = false },
            onConfirm = { name, color ->
                onAddLabel(name, color)
                showAddDialog = false
            }
        )
    }

    if (labelToEdit != null) {
        LabelEditDialog(
            title = "Edit Label",
            initialName = labelToEdit!!.name,
            initialColor = labelToEdit!!.color,
            onDismiss = { labelToEdit = null },
            onConfirm = { name, color ->
                onUpdateLabel(labelToEdit!!.copy(name = name, color = color))
                labelToEdit = null
            }
        )
    }
}

@Composable
fun LabelItem(
    label: LabelEntity,
    onEdit: (LabelEntity) -> Unit,
    onDelete: (LabelEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Label, null, tint = Color(android.graphics.Color.parseColor(label.color)), modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Text(label.name, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            IconButton(onClick = { onEdit(label) }) { Icon(Icons.Default.Edit, null, modifier = Modifier.size(20.dp)) }
            IconButton(onClick = { onDelete(label) }) { Icon(Icons.Default.Delete, null, modifier = Modifier.size(20.dp), tint = Color.Red.copy(alpha = 0.7f)) }
        }
    }
}

@Composable
fun LabelEditDialog(
    title: String,
    initialName: String = "",
    initialColor: String = "#748393",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var selectedColor by remember { mutableStateOf(initialColor) }
    
    val colors = listOf("#748393", "#246EE9", "#4CAF50", "#F44336", "#FF9800", "#9C27B0")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Label Name") },
                    singleLine = true
                )
                
                Text("Tag Color", style = MaterialTheme.typography.labelMedium)
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

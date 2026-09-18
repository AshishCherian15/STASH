package com.ashish.stash.ui.feature.settings.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.ui.theme.VaultBrass

@Composable
fun CategoriesTab(
    categories: List<CategoryEntity>,
    onAdd: (String, String) -> Unit,
    onUpdate: (CategoryEntity) -> Unit,
    onDelete: (CategoryEntity) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CategoryEntity?>(null) }
    
    Column(modifier = Modifier.fillMaxSize()) {
        ListItem(
            headlineContent = { Text("Manage Categories") },
            trailingContent = {
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        )
        HorizontalDivider()
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(categories) { category ->
                ListItem(
                    headlineContent = { Text(category.name) },
                    leadingContent = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(parseColor(category.color), shape = MaterialTheme.shapes.small)
                        )
                    },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { editingCategory = category }) {
                                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = { onDelete(category) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        CategoryDialog(
            title = "Add Category",
            onDismiss = { showAddDialog = false },
            onConfirm = { name, color ->
                onAdd(name, color)
                showAddDialog = false
            }
        )
    }
    
    editingCategory?.let { category ->
        CategoryDialog(
            title = "Edit Category",
            initialName = category.name,
            initialColor = category.color,
            onDismiss = { editingCategory = null },
            onConfirm = { name, color ->
                onUpdate(category.copy(name = name, color = color))
                editingCategory = null
            }
        )
    }
}

@Composable
private fun CategoryDialog(
    title: String,
    initialName: String = "",
    initialColor: String = "#B8873B",
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var color by remember { mutableStateOf(initialColor) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Text("Pick a color")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("#B8873B", "#1C2733", "#5C6B73", "#A6432C", "#6B8068", "#FF0000", "#00FF00", "#0000FF").forEach { hex ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(parseColor(hex), shape = MaterialTheme.shapes.small)
                                .clickable { color = hex }
                                .let { if (color == hex) it.padding(2.dp).background(Color.White) else it }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, color) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        VaultBrass
    }
}

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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ashish.stash.core.database.entity.CategoryEntity
import com.ashish.stash.core.database.entity.StashPalette
import com.ashish.stash.ui.theme.StashBlue

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
                                .clip(CircleShape)
                                .background(parseColor(category.color))
                        )
                    },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { editingCategory = category }) {
                                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = { onDelete(category) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CategoryDialog(
    title: String,
    initialName: String = "",
    initialColor: String = StashPalette.ACTIVE_CASE,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var color by remember { mutableStateOf(initialColor) }
    
    val professionalColors = listOf(
        StashPalette.ACTIVE_CASE, StashPalette.ARCHIVED, StashPalette.PENDING,
        StashPalette.EXTERNAL, StashPalette.INTERNAL, StashPalette.FINANCIAL,
        StashPalette.LEGAL, StashPalette.PERSONNEL, StashPalette.MEDICAL, StashPalette.MISC
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = name, 
                    onValueChange = { name = it }, 
                    label = { Text("Category Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = StashBlue, focusedLabelColor = StashBlue)
                )
                Text("Select Tone", style = MaterialTheme.typography.labelLarge, color = StashBlue)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    professionalColors.forEach { hex ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(parseColor(hex))
                                .clickable { color = hex }
                                .let { 
                                    if (color == hex) it.padding(2.dp).background(Color.Black, CircleShape).padding(2.dp).background(parseColor(hex), CircleShape) 
                                    else it 
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, color) }, enabled = name.isNotBlank(), colors = ButtonDefaults.buttonColors(containerColor = StashBlue)) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = StashBlue) }
        }
    )
}

private fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        StashBlue
    }
}

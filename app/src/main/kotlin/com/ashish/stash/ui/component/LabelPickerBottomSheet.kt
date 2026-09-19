package com.ashish.stash.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashish.stash.core.database.entity.LabelEntity
import com.ashish.stash.ui.theme.StashBlue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LabelPickerBottomSheet(
    allLabels: List<LabelEntity>,
    selectedLabelIds: Set<Long>,
    onToggleLabel: (Long) -> Unit,
    onCreateLabel: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredLabels = allLabels.filter { it.name.contains(searchQuery, ignoreCase = true) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Text(
                "Manage Labels",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search or create label...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (searchQuery.isNotBlank() && !allLabels.any { it.name.equals(searchQuery, true) }) {
                        IconButton(onClick = { 
                            onCreateLabel(searchQuery)
                            searchQuery = ""
                        }) {
                            Icon(Icons.Default.Add, "Create")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(filteredLabels) { label ->
                    val isSelected = selectedLabelIds.contains(label.labelId)
                    ListItem(
                        headlineContent = { Text(label.name) },
                        trailingContent = {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { onToggleLabel(label.labelId) },
                                colors = CheckboxDefaults.colors(checkedColor = StashBlue)
                            )
                        },
                        modifier = Modifier.clickable { onToggleLabel(label.labelId) }
                    )
                }
                
                if (filteredLabels.isEmpty() && searchQuery.isNotBlank()) {
                    item {
                        TextButton(
                            onClick = { 
                                onCreateLabel(searchQuery)
                                searchQuery = ""
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(Icons.Default.Add, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Create label \"$searchQuery\"")
                        }
                    }
                }
            }
        }
    }
}

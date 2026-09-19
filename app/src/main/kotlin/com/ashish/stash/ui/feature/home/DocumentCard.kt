package com.ashish.stash.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ashish.stash.core.database.entity.DocumentWithMetadata
import com.ashish.stash.core.database.entity.Importance
import com.ashish.stash.core.database.entity.OcrStatus
import com.ashish.stash.ui.theme.StashBlue
import com.ashish.stash.ui.theme.VaultBrass

@Composable
fun DocumentCard(
    documentWithMetadata: DocumentWithMetadata,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    searchQuery: String = ""
) {
    val document = documentWithMetadata.document
    val category = documentWithMetadata.category
    val folder = documentWithMetadata.folder
    val labels = documentWithMetadata.labels
    val resourceLinks = documentWithMetadata.resourceLinks
    
    val isIndexing = document.ocrStatus == OcrStatus.PENDING

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            val notchColor = category?.color?.let { parseColor(it) } ?: StashBlue
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(6.dp)
                    .background(notchColor)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (category != null) {
                            Text(
                                text = category.name.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = notchColor,
                                fontWeight = FontWeight.Bold
                            )
                            Text(" • ", style = MaterialTheme.typography.labelSmall)
                        }
                        if (folder != null) {
                            Text(
                                text = folder.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(" • ", style = MaterialTheme.typography.labelSmall)
                        }
                        Text(
                            text = document.mimeType.split("/").last().uppercase(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    
                    if (document.importance == Importance.HIGH || document.importance == Importance.CRITICAL) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = VaultBrass
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = highlightText(document.displayTitle, searchQuery),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (resourceLinks.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Outlined.Link, null, modifier = Modifier.size(12.dp), tint = StashBlue)
                        Text(
                            text = "${resourceLinks.size} links",
                            style = MaterialTheme.typography.labelSmall,
                            color = StashBlue
                        )
                    }
                }

                if (labels.isNotEmpty() || isIndexing) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isIndexing) {
                            Icon(
                                Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = StashBlue
                            )
                            Text("Indexing...", style = MaterialTheme.typography.labelSmall, color = StashBlue)
                        }
                        
                        labels.take(2).forEach { label ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(label.name, style = MaterialTheme.typography.labelSmall) },
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                }
            }

            if (document.isLocked) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Locked",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterVertically),
                    tint = StashBlue
                )
            }
        }
    }
}

@Composable
fun highlightText(text: String, query: String): AnnotatedString {
    if (query.isEmpty() || !text.contains(query, ignoreCase = true)) {
        return AnnotatedString(text)
    }

    return buildAnnotatedString {
        var start = 0
        while (start < text.length) {
            val index = text.indexOf(query, start, ignoreCase = true)
            if (index == -1) {
                append(text.substring(start))
                break
            }
            append(text.substring(start, index))
            withStyle(SpanStyle(background = StashBlue.copy(alpha = 0.2f), fontWeight = FontWeight.Bold, color = StashBlue)) {
                append(text.substring(index, index + query.length))
            }
            start = index + query.length
        }
    }
}

private fun parseColor(colorString: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(colorString))
    } catch (e: Exception) {
        StashBlue
    }
}

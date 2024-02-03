/*
 *abiola 2024
 */

package com.mshdabiola.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun NoteUi(
    noteUiState: MainNoteUiState,
    onNoteClick: (Long) -> Unit = {},
) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onNoteClick(noteUiState.id) },
        headlineContent = {
            Text(
                text = noteUiState.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        supportingContent = {
            if (noteUiState.content != null) {
                Text(
                    text = noteUiState.content,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        overlineContent = {
            Row(horizontalArrangement = Arrangement.Center) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "time",
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(text = noteUiState.createAt)
                Spacer(modifier = Modifier.width(8.dp))
                if (noteUiState.checkFraction != null) {
                    Icon(
                        modifier = Modifier.size(16.dp),

                        imageVector = Icons.Default.Checklist,
                        contentDescription = "checklist",
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = noteUiState.checkFraction,
                    )
                }
            }
        },
        trailingContent = {
            if (noteUiState.path != null) {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .size(64.dp),
                    model = noteUiState.path,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }
        },
        colors = ListItemDefaults
            .colors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shadowElevation = 8.dp,
        tonalElevation = 8.dp,
    )
}

@Preview
@Composable
private fun NoteUiPreview() {
    NoteUi(
        noteUiState = MainNoteUiState(
            id = 3011L,
            title = "Liam",
            content = null,
            checkFraction = null,
            path = null,
            createAt = "Roslyn",
        ),
    )
}

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
import com.mshdabiola.model.Type
import kotlinx.collections.immutable.toImmutableList

@Composable
fun NoteUi(
    noteUiState: NoteUiState,
    onNoteClick: (Long) -> Unit = {},
) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onNoteClick(noteUiState.id) },
        headlineContent = {
            Text(
                text = noteUiState.title.ifBlank { noteUiState.contents[0].content },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        supportingContent = {
            if (noteUiState.title.isNotBlank()) {
                Text(
                    text = noteUiState.contents.getOrNull(0)?.content ?: "",
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

                Text(text = noteUiState.createdAtStr)
                Spacer(modifier = Modifier.width(8.dp))
                if (noteUiState.contents.any { it.type == Type.CHECK }) {
                    Icon(
                        modifier = Modifier.size(16.dp),

                        imageVector = Icons.Default.Checklist,
                        contentDescription = "checklist",
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${
                            noteUiState.contents.filter { it.type == Type.CHECK && it.isCheck }
                                .count()
                        }/${noteUiState.contents.filter { it.type == Type.CHECK }.count()}",
                    )
                }
            }
        },
        trailingContent = {
            if (noteUiState.contents.any { it.type == Type.IMAGE }) {
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .size(64.dp),
                    model = noteUiState.contents.first { it.type == Type.IMAGE }.content,
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

//    SwipeToDismiss(state = rememberDismissState(),
//        directions = setOf(DismissDirection.EndToStart),
//        background = {
//            Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
//        }, dismissContent ={
//            ListItem(
//                colors = ListItemDefaults.colors(containerColor = Color.Red),
//                headlineContent = { Text(text = "Background") })
//        } )
//
}

@Preview
@Composable
private fun NoteUiPreview() {
    NoteUi(
        noteUiState = NoteUiState(
            id = 4,
            title = "Geovanni",
            contents = listOf(
                NoteItemUiState(
                    content = "abiola",
                    type = Type.TEXT,
                ),
                NoteItemUiState(
                    content = "monkshood",
                    type = Type.TEXT,
                ),
                NoteItemUiState(
                    content = "Dyan",
                    type = Type.CHECK,
                ),
            ).toImmutableList(),
            createdAtStr = "Friday",
        ),
    )
}

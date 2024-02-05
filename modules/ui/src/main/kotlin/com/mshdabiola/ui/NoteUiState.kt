/*
 *abiola 2024
 */

package com.mshdabiola.ui

import com.mshdabiola.model.Note
import com.mshdabiola.model.NoteItem
import com.mshdabiola.model.Type
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class NoteUiState(
    val id: Long = 0,
    val title: String = "",
    val contents: ImmutableList<NoteItemUiState> = emptyList<NoteItemUiState>().toImmutableList(),
    val createdAt: Long = 1,
    val createdAtStr: String = "",
)

data class NoteItemUiState(val content: String = "", val type: Type = Type.TEXT, val isCheck: Boolean = false, val isFocus: Boolean = false)

fun NoteUiState.toNote(): Note {
    return Note(
        id = id,
        title = title,
        contents = contents.map { it.asNoteItem() },
        createdAt = createdAt,
        createdAtStr,
    )
}

fun Note.asNoteUiState(): NoteUiState {
    return NoteUiState(
        id = id ?: 0,
        title = title,
        contents = contents.map { it.asNoteItemUiState() }.toImmutableList(),
        createdAt = createdAt,
        createdAtStr = createdAtStrng,
    )
}

fun NoteItemUiState.asNoteItem() = NoteItem(content, type, isCheck)

fun NoteItem.asNoteItemUiState() = NoteItemUiState(content, type, isCheck, false)

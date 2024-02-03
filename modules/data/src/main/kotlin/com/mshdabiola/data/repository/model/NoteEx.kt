/*
 *abiola 2024
 */

package com.mshdabiola.data.repository.model

import com.mshdabiola.database.model.NoteEntity
import com.mshdabiola.model.Note
import com.mshdabiola.model.NoteItem

fun Note.asNoteEntity(toString: (contents: List<NoteItem>) -> String) = NoteEntity(id, title, toString(contents), createdAt)
fun NoteEntity.asNote(toContents: (content: String) -> List<NoteItem>, changeDate: (Long) -> String) = Note(id, title, toContents(content), createdAt, changeDate(createdAt))

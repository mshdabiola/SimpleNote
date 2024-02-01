/*
 *abiola 2024
 */

package com.mshdabiola.data.repository.model

import com.mshdabiola.database.model.NoteEntity
import com.mshdabiola.model.Note

fun Note.asNoteEntity() = NoteEntity(id, title, content)

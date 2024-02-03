/*
 *abiola 2024
 */

package com.mshdabiola.data.repository

import com.mshdabiola.common.network.Dispatcher
import com.mshdabiola.common.network.NiaDispatchers
import com.mshdabiola.data.repository.model.ContentItemSer
import com.mshdabiola.data.repository.model.asContentSer
import com.mshdabiola.data.repository.model.asContents
import com.mshdabiola.data.repository.model.asNote
import com.mshdabiola.data.repository.model.asNoteEntity
import com.mshdabiola.database.dao.NoteDao
import com.mshdabiola.model.Note
import com.mshdabiola.model.NoteItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DefaultNoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : NoteRepository {

    private val json = Json
    override suspend fun upsert(note: Note): Long {
        return withContext(ioDispatcher) {
            noteDao.upsert(note.asNoteEntity(::toString))
        }
    }

    override fun getAll(): Flow<List<Note>> {
        return noteDao
            .getAll()
            .map { noteEntities -> noteEntities.map { it.asNote(::toList, ::convertDateToText) } }
            .flowOn(ioDispatcher)
    }

    override fun getOne(id: Long): Flow<Note?> {
        return noteDao
            .getOne(id)
            .map { it?.asNote(::toList, ::convertDateToText) }
            .flowOn(ioDispatcher)
    }

    override suspend fun delete(id: Long) {
        withContext(ioDispatcher) {
            noteDao.delete(id)
        }
    }

    private fun toString(contents: List<NoteItem>): String {
        val contentsList = contents.map { it.asContentSer() }
        return json.encodeToString(ListSerializer(ContentItemSer.serializer()), contentsList)
    }
    private fun toList(contents: String): List<NoteItem> {
        val contentsList = json.decodeFromString(ListSerializer(ContentItemSer.serializer()), contents)
        return contentsList.map { it.asContents() }
    }

    private fun convertDateToText(date: Long): String {
        val dateTime = Instant.fromEpochMilliseconds(date)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val period = now.date.minus(dateTime.date)

        val months = period.months
        val days = period.days

        return when {
            months > 0 -> "${dateTime.dayOfMonth}/${dateTime.monthNumber}/${dateTime.year}"
            days > 0 -> dateTime.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
            else -> "Today"
        }
    }
}

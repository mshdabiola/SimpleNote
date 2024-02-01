/*
 *abiola 2024
 */

package com.mshdabiola.data.repository

import com.mshdabiola.common.network.Dispatcher
import com.mshdabiola.common.network.NiaDispatchers
import com.mshdabiola.data.repository.model.asNoteEntity
import com.mshdabiola.database.dao.NoteDao
import com.mshdabiola.database.model.asExternalNote
import com.mshdabiola.model.Note
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultNoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : NoteRepository {
    override suspend fun upsert(note: Note): Long {
        return withContext(ioDispatcher) {
            noteDao.upsert(note.asNoteEntity())
        }
    }

    override fun getAll(): Flow<List<Note>> {
        return noteDao
            .getAll()
            .map { noteEntities -> noteEntities.map { it.asExternalNote() } }
            .flowOn(ioDispatcher)
    }

    override fun getOne(id: Long): Flow<Note?> {
        return noteDao
            .getOne(id)
            .map { it?.asExternalNote() }
            .flowOn(ioDispatcher)
    }

    override suspend fun delete(id: Long) {
        withContext(ioDispatcher) {
            noteDao.delete(id)
        }
    }
}

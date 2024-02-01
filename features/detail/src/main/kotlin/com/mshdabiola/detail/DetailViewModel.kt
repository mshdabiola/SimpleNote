/*
 *abiola 2022
 */

package com.mshdabiola.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.NoteRepository
import com.mshdabiola.detail.navigation.DetailArgs
import com.mshdabiola.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
) : ViewModel() {
    private val topicArgs: DetailArgs = DetailArgs(savedStateHandle)

    private val topicId = topicArgs.id

    private var _noteState = mutableStateOf(Note())
    val noteState: State<Note> = _noteState

    init {
        viewModelScope.launch {
            if (topicId > 0) {
                val note = noteRepository.getOne(topicId)
                    .first()
                if (note != null) {
                    _noteState.value = note
                }
            }
        }

        viewModelScope.launch {
            snapshotFlow { noteState.value }
                .collectLatest {
                    if (it.id != null) {
                        noteRepository.upsert(it)
                    }
                }
        }
    }

    var job: Job? = null
    fun onTitleChange(text: String) {
        _noteState.value = noteState.value.copy(title = text)
        if (noteState.value.id == null) {
            job?.cancel()
            job = viewModelScope.launch {
                val id = getId()
                _noteState.value = noteState.value.copy(id = id)
            }
        }
    }

    fun onContentChange(text: String) {
        _noteState.value = noteState.value.copy(content = text)
        if (noteState.value.id == null) {
            job?.cancel()
            job = viewModelScope.launch {
                val id = getId()
                _noteState.value = noteState.value.copy(id = id)
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            noteState.value.id?.let { noteRepository.delete(it) }
        }
    }

    suspend fun getId(): Long {
        return noteRepository.upsert(Note())
    }
}

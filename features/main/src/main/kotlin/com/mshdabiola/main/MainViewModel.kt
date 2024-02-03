/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.NoteRepository
import com.mshdabiola.data.repository.UserDataRepository
import com.mshdabiola.ui.NoteUiState
import com.mshdabiola.ui.asNoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {

    var shouldDisplayUndoBookmark by mutableStateOf(false)
    private var lastRemovedBookmarkId: String? = null

    val mainUiState: StateFlow<MainUiState> =
        noteRepository.getAll()
            .map { notes ->
                MainUiState.Success(
                    notes
                        .filter { it.title.isNotBlank() && it.contents.all { it.content.isNotBlank() } }
                        .map { it.asNoteUiState() }
                        .toImmutableList(),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainUiState.Loading,
            )

    private val _searchNote = MutableStateFlow(emptyList<NoteUiState>().toImmutableList())
    val searchState = _searchNote.asStateFlow()



    var job: Job? = null
    fun onSearch(text: String) {

        if (text.isBlank()){
            _searchNote.update {
                emptyList<NoteUiState>().toImmutableList()
            }

        }else{
            job?.cancel()
            job = viewModelScope.launch {
                val allNotes = noteRepository
                    .getAll()
                    .first()
                    .map { it.asNoteUiState () }

                val notes = if (text.isNotBlank()) {
                    allNotes
                        .filter {
                            it.title.contains(text, true) || it.contents.any { it.content.contains(text,true) } //
                        }
                } else {
                    allNotes
                }


                _searchNote.update {
                    notes.toImmutableList()
                }

            }

        }


    }
}

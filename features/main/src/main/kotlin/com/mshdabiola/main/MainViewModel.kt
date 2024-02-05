/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.NoteRepository
import com.mshdabiola.data.repository.UserDataRepository
import com.mshdabiola.model.DarkThemeConfig
import com.mshdabiola.model.Note
import com.mshdabiola.model.Type
import com.mshdabiola.ui.MainNoteUiState
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

    val mainUiState: StateFlow<MainUiState> =
        noteRepository.getAll()
            .map { notes ->
                MainUiState.Success(
                    notes
                        .filter { note -> note.title.isNotBlank() || note.contents.any { it.content.isNotBlank() } }
                        .map { processNote(it) }
                        .toImmutableList(),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainUiState.Loading,
            )
    val isDarkMode = userDataRepository
        .userData
        .map { it.darkThemeConfig == DarkThemeConfig.DARK }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    private val searchNote = MutableStateFlow(emptyList<MainNoteUiState>().toImmutableList())
    val searchState = searchNote.asStateFlow()

    var job: Job? = null
    fun onSearch(text: String) {
        if (text.isBlank()) {
            searchNote.update {
                emptyList<MainNoteUiState>().toImmutableList()
            }
        } else {
            job?.cancel()
            job = viewModelScope.launch {
                val allNotes = noteRepository
                    .getAll()
                    .first()

                val notes = if (text.isNotBlank()) {
                    allNotes
                        .filter {
                            it.title.contains(text, true) || it.contents.any {
                                it.content.contains(
                                    text,
                                    true,
                                )
                            } //
                        }
                } else {
                    allNotes
                }
                    .map { processNote(it) }

                searchNote.update {
                    notes.toImmutableList()
                }
            }
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            if (isDarkMode.value) {
                userDataRepository.setDarkThemeConfig(DarkThemeConfig.LIGHT)
            } else {
                userDataRepository.setDarkThemeConfig(DarkThemeConfig.DARK)
            }
        }
    }

    private fun processNote(note: Note): MainNoteUiState {
        val title = when {
            note.title.isNotBlank() -> note.title
            note.contents.any { it.type != Type.IMAGE && it.content.isNotBlank() } -> {
                note.contents.first { it.content.isNotBlank() }.content
            }

            else -> "Picture note"
        }

        val content = when {
            note.title.isBlank() -> null
            note.contents.any { it.type != Type.IMAGE && it.content.isNotBlank() } -> {
                note.contents.first { it.content.isNotBlank() }.content
            }

            else -> null
        }
        val checkNote = if (note.contents.any { it.type == Type.CHECK }) {
            val check = note.contents.count { it.type == Type.CHECK && it.isCheck }
            val all = note.contents.count { it.type == Type.CHECK }
            "$check/$all"
        } else {
            null
        }

        return MainNoteUiState(
            id = note.id ?: 0,
            title = title,
            content = content,
            checkFraction = checkNote,
            path = note.contents.firstOrNull { it.type == Type.IMAGE }?.content,
            createAt = note.createdAtStrng,
        )
    }
}

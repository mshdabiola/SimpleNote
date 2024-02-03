/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.NoteRepository
import com.mshdabiola.data.repository.UserDataRepository
import com.mshdabiola.model.DarkThemeConfig
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

    val mainUiState: StateFlow<MainUiState> =
        noteRepository.getAll()
            .map { notes ->
                MainUiState.Success(
                    notes
                        .filter { it.title.isNotBlank() || it.contents.all { it.content.isNotBlank() } }
                        .map { it.asNoteUiState() }
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

    private val searchNote = MutableStateFlow(emptyList<NoteUiState>().toImmutableList())
    val searchState = searchNote.asStateFlow()

    var job: Job? = null
    fun onSearch(text: String) {
        if (text.isBlank()) {
            searchNote.update {
                emptyList<NoteUiState>().toImmutableList()
            }
        } else {
            job?.cancel()
            job = viewModelScope.launch {
                val allNotes = noteRepository
                    .getAll()
                    .first()
                    .map { it.asNoteUiState() }

                val notes = if (text.isNotBlank()) {
                    allNotes
                        .filter {
                            it.title.contains(text, true) || it.contents.any { it.content.contains(text, true) } //
                        }
                } else {
                    allNotes
                }

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
}

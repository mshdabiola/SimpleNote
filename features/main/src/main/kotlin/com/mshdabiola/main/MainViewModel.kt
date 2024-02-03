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
import com.mshdabiola.ui.asNoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
}

/*
 *abiola 2022
 */

package com.mshdabiola.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.NoteRepository
import com.mshdabiola.data.repository.UserDataRepository
import com.mshdabiola.ui.MainState
import com.mshdabiola.ui.NoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val feedUiMainState: StateFlow<MainState> =
        noteRepository.getAll()
            .map { notes -> MainState.Success(notes.map { NoteUiState(it.id!!, it.title, it.content) }) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainState.Loading,
            )
}

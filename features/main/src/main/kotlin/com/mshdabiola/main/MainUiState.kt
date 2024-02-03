/*
 *abiola 2024
 */

package com.mshdabiola.main

import com.mshdabiola.ui.NoteUiState
import kotlinx.collections.immutable.ImmutableList

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(
        val noteUiStates: ImmutableList<NoteUiState>,
    ) : MainUiState
}

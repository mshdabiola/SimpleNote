/*
 *abiola 2022
 */

package com.mshdabiola.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import com.mshdabiola.analytics.LocalAnalyticsHelper

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.noteItem(
    notes: List<MainNoteUiState>,
    onClick: (Long) -> Unit = {},
) {
    items(
        items = notes,
        key = { it.id },
        contentType = { "newsFeedItem" },
    ) { note ->
        val analyticsHelper = LocalAnalyticsHelper.current

        NoteUi(noteUiState = note, onNoteClick = {
            onClick(it)
            analyticsHelper.logNoteOpened(it.toString())
        })
    }
}

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(
        val notes: List<NoteUiState>,
    ) : MainUiState
}

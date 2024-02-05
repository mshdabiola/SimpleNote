/*
 *abiola 2024
 */

package com.mshdabiola.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteSearchBar(
    active:Boolean=false,
    onChangeActive : (Boolean)->Unit={},
    notes: ImmutableList<MainNoteUiState>,
    onSearch: (String) -> Unit = {},
    onNoteClick: (Long) -> Unit = {},
) {
    var query by remember {
        mutableStateOf("")
    }


    val focus = remember {

        FocusRequester()
    }
    LaunchedEffect(key1 = Unit) {
        focus.requestFocus()
    }

    SearchBar(
        modifier = Modifier.focusRequester(focus),
        query = query,
        onQueryChange = {
            query = it
            onSearch(query)
        },
        placeholder = { Text(text = "Search Note") },
        onSearch = {
            onSearch(query)
        },
        active = active,
        onActiveChange = {
           onChangeActive(it)

        },
        leadingIcon = {
            IconButton(
                onClick = {
                    onChangeActive(false)
                    query = ""
                    onSearch(query)
                },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back",
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    query = ""
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear",
                )
            }
        },
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("main:search"),
        ) {
            noteItem(
                notes = notes,
                onClick = onNoteClick,
            )
            item {
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}

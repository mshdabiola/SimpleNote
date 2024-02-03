/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mshdabiola.designsystem.component.NoteLoadingWheel
import com.mshdabiola.designsystem.theme.LocalTintTheme
import com.mshdabiola.designsystem.theme.SimpleNoteTheme
import com.mshdabiola.ui.MainNoteUiState
import com.mshdabiola.ui.TrackScreenViewEvent
import com.mshdabiola.ui.TrackScrollJank
import com.mshdabiola.ui.noteItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun MainRoute(
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val notes by viewModel.mainUiState.collectAsStateWithLifecycle()
    val searchNote by viewModel.searchState.collectAsStateWithLifecycle()

    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    MainScreen(
        mainState = notes,
        searchNotes = searchNote,
        isDarkMode = isDarkMode,
        modifier = modifier,
        onClick = onClick,
        onSearch = viewModel::onSearch,
        toggleDarkMode = viewModel::toggleDarkMode,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    mainState: MainUiState,
    searchNotes: ImmutableList<MainNoteUiState>,
    isDarkMode: Boolean = false,
    onClick: (Long) -> Unit = {},
    onSearch: (String) -> Unit = {},
    toggleDarkMode: () -> Unit = {},
) {
    when (mainState) {
        MainUiState.Loading -> LoadingState(modifier)
        is MainUiState.Success -> if (mainState.noteUiStates.isNotEmpty()) {
            MainList(
                notes = mainState.noteUiStates,
                searchNotes = searchNotes,
                onSearch = onSearch,
                toggleDarkMode = toggleDarkMode,
                onNoteClick = onClick,
                isDarkMode = isDarkMode,
                modifier = modifier,
            )
        } else {
            EmptyState(modifier)
        }
    }

    TrackScreenViewEvent(screenName = "Main")
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize()) {
        NoteLoadingWheel(
            modifier = modifier
                .align(Alignment.Center)
                .testTag("main:loading"),
            contentDesc = stringResource(id = R.string.features_main_loading),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainList(
    modifier: Modifier = Modifier,
    notes: ImmutableList<MainNoteUiState>,
    searchNotes: ImmutableList<MainNoteUiState>,
    isDarkMode: Boolean = false,
    onNoteClick: (Long) -> Unit = {},
    onSearch: (String) -> Unit = {},
    toggleDarkMode: () -> Unit = {},
) {
    val scrollableState = rememberLazyListState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "main:list")
    var query by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(false)
    }
    val state = remember {
        MutableInteractionSource()
    }
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (active) {
                SearchBar(
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
                        active = it
                    },
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                active = false
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
                    interactionSource = state,
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        state = scrollableState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .testTag("main:search"),
                    ) {
                        noteItem(
                            notes = searchNotes,
                            onClick = onNoteClick,
                        )
                        item {
                            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                        }
                    }
                }
            } else {
                MediumTopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent, scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer),
                    scrollBehavior = scrollBehavior,
                    title = {
                        Text(
                            text = stringResource(id = R.string.features_main_app_name),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                active = true
                                coroutineScope.launch {
                                    delay(1000)
                                    state.emit(PressInteraction.Press(Offset(200f, 10f)))
                                }
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "search",
                            )
                        }
                        IconButton(
                            onClick = toggleDarkMode,
                        ) {
                            if (isDarkMode) {
                                Icon(
                                    imageVector = Icons.Default.LightMode,
                                    contentDescription = "light mode",
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.DarkMode,
                                    contentDescription = "dark mode",
                                )
                            }
                        }
                    },
                )
            }
        },
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = scrollableState,
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .testTag("main:list"),
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

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .testTag("main:empty"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.empty_note),
            colorFilter = if (iconTint != Color.Unspecified) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(id = R.string.features_main_empty_error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.features_main_empty_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun LoadingStatePreview() {
    SimpleNoteTheme {
        LoadingState()
    }
}

@Preview
@Composable
private fun MainListPreview() {
    SimpleNoteTheme {
        MainList(
            notes = listOf(
                MainNoteUiState(
                    id = 9045L,
                    title = "Torrell",
                    content = null,
                    checkFraction = null,
                    path = null,
                    createAt = "Petrina",
                ),

            ).toImmutableList(),
            searchNotes = listOf(
                MainNoteUiState(
                    id = 7645L,
                    title = "Solange",
                    content = null,
                    checkFraction = null,
                    path = null,
                    createAt = "Cliffton",
                ),

            ).toImmutableList(),
        )
    }
}

@Preview
@Composable
private fun EmptyStatePreview() {
    SimpleNoteTheme {
        EmptyState()
    }
}

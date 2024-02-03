/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mshdabiola.designsystem.component.SkLoadingWheel
import com.mshdabiola.designsystem.theme.LocalTintTheme
import com.mshdabiola.designsystem.theme.SimpleNoteTheme
import com.mshdabiola.ui.NoteUiState
import com.mshdabiola.ui.TrackScreenViewEvent
import com.mshdabiola.ui.TrackScrollJank
import com.mshdabiola.ui.noteItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun MainRoute(
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val feedState by viewModel.mainUiState.collectAsStateWithLifecycle()
    MainScreen(
        mainState = feedState,
        modifier = modifier,
        onClick = onClick,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun MainScreen(
    mainState: MainUiState,
    onClick: (Long) -> Unit = {},
    onSearch: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    when (mainState) {
        MainUiState.Loading -> LoadingState(modifier)
        is MainUiState.Success -> if (mainState.noteUiStates.isNotEmpty()) {
            MainList(
                mainState = mainState.noteUiStates,
                onSearch = onSearch,
                onNoteClick = onClick,
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
    SkLoadingWheel(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .testTag("main:loading"),
        contentDesc = stringResource(id = R.string.features_main_loading),
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun MainList(
    mainState: ImmutableList<NoteUiState>,
    onNoteClick: (Long) -> Unit = {},
    onSearch: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val scrollableState = rememberLazyListState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "main:list")
    var query by remember {
        mutableStateOf("")
    }
    val containerColor = MaterialTheme.colorScheme.background
    val contentColor = Color.DarkGray.copy(alpha = 0.6f)
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        MediumTopAppBar(
            colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
            title = {
                Text(
                    text = stringResource(id = R.string.features_main_app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            actions = {
            },
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = scrollableState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .testTag("main:list"),
        ) {
//            stickyHeader {
//                TextField(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                    value = query,
//                    onValueChange = {
//                        query = it
//                        onSearch(query)
//                    },
//                    placeholder = {
//                        Text(text = "Search Notes", color = contentColor)
//                    },
//                    shape = RoundedCornerShape(24.dp),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = containerColor,
//                        unfocusedContainerColor = containerColor,
//                        disabledContainerColor = containerColor,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                    ),
//                    leadingIcon = {
//                        IconButton(onClick = {}) {
//                            Icon(
//                                imageVector = Icons.Default.Search,
//                                contentDescription = "search",
//                                tint = contentColor
//                            )
//                        }
//                    }
//
//
//                )
//            }

            noteItem(
                notes = mainState,
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
            painter = painterResource(id = R.drawable.features_main_img_empty_bookmarks),
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
            listOf(
                NoteUiState(),

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

/*
 *abiola 2022
 */

package com.mshdabiola.detail

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mshdabiola.designsystem.component.DetailTopAppBar
import com.mshdabiola.model.Type
import com.mshdabiola.ui.DeleteDialog
import com.mshdabiola.ui.ImageViewer
import com.mshdabiola.ui.NoteItemUiState
import com.mshdabiola.ui.NoteUiState
import com.mshdabiola.ui.TrackScreenViewEvent
import com.mshdabiola.ui.TrackScrollJank
import com.mshdabiola.ui.noteUiEdit
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun DetailRoute(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    var showImageDialog by remember {
        mutableStateOf(false)
    }

    DetailScreen(
        modifier = modifier,
        back = onBack,
        currentNoteUiState = viewModel.currentNote.value,
        onContentChange = viewModel::onContentChange,
        onTitleChange = viewModel::onTitleChange,
        onCheckChange = viewModel::onCheckChange,
        changeToCheck = viewModel::changeToCheck,
        onDeleteNote = {
            viewModel.onDeleteButtonClick()
            onBack()
        },
        onFocusChange = viewModel::onFocusChange,
        addNewItem = viewModel::addNewItem,
        showImage = { showImageDialog = true },
        deleteImage = viewModel::deleteImage,
    )

    ImageDialog(
        show = showImageDialog,
        onDismissRequest = { showImageDialog = false },
        addImage = viewModel::addImage,
        savePhoto = viewModel::savePhoto,
        getUri = viewModel::getPhotoUri,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DetailScreen(
    modifier: Modifier = Modifier,
    back: () -> Unit = {},
    currentNoteUiState: NoteUiState,
    onTitleChange: (String) -> Unit = {},
    onContentChange: (String, Int) -> Unit = { _, _ -> },
    onCheckChange: (Boolean, Int) -> Unit = { _, _ -> },
    changeToCheck: () -> Unit = {},
    onDeleteNote: () -> Unit = {},
    onFocusChange: (Boolean, Int) -> Unit = { _, _ -> },
    addNewItem: (Int) -> Unit = {},
    showImage: () -> Unit = {},
    deleteImage: (Int) -> Unit = {},
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    val scrollableState = rememberLazyListState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "main:detail")
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var imagePath by remember {
        mutableStateOf<String?>(null)
    }
        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            topBar = {
                DetailTopAppBar(
                    onBack = back,
                    scrollBehavior = scrollBehavior,
                    onDeleteClick = {
                        showDialog = true
                    },
                    onAddImageClick = showImage,
                    onChangeCheckClick = changeToCheck,
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                state = scrollableState
            ) {
                noteUiEdit(
                    noteUiState = currentNoteUiState,
                    onTitleChange = onTitleChange,
                    onContentChange = onContentChange,
                    onCheckChange = onCheckChange,
                    onFocusChange = onFocusChange,
                    addNewItem = addNewItem,
                    deleteImage = deleteImage,
                    onImageClick = { imagePath = it },

                )
                item {
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }


    if (showDialog) {
        DeleteDialog(onDismiss = { showDialog = false }, onDelete = onDeleteNote)
    }
    ImageViewer(imagePath) {
        imagePath = null
    }
    TrackScreenViewEvent(screenName = "Detail")
}

@Preview
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        currentNoteUiState = NoteUiState(
            id = 7329L,
            title = "Janise",
            contents = listOf(
                NoteItemUiState(
                    content = "abiola",
                    type = Type.TEXT,
                ),
                NoteItemUiState(
                    content = "monkshood",
                    type = Type.TEXT,
                ),
                NoteItemUiState(
                    content = "Dyan",
                    type = Type.CHECK,
                ),
            ).toImmutableList(),
            createdAtStr = "Yara",
        ),
    )
}

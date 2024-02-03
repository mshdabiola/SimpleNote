/*
 *abiola 2022
 */

package com.mshdabiola.detail

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mshdabiola.model.Type
import com.mshdabiola.ui.NoteItemUiState
import com.mshdabiola.ui.NoteUiState
import com.mshdabiola.ui.TrackScreenViewEvent
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
internal fun DetailRoute(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    var showImageDialog by remember {
        mutableStateOf(false)
    }
    var photoId by remember {
        mutableLongStateOf(0L)
    }
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                showImageDialog = false
                val time = System.currentTimeMillis()
                viewModel.savePhoto(it, time)
                viewModel.addImage(time)
                //  navigateToEdit(-3, "image text", time)
            }
        },
    )

    val snapPictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            if (it) {
                showImageDialog = false
                viewModel.addImage(photoId)
                // navigateToEdit(-3, "image text", photoId)
            }
        },
    )
    DetailScreen(
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
    )

    ImageDialog(
        show = showImageDialog,
        onDismissRequest = { showImageDialog = false },
        onChooseImage = {
            imageLauncher.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
        },
        onSnapImage = {
            photoId = System.currentTimeMillis()
            snapPictureLauncher.launch(viewModel.getPhotoUri(photoId))
        },
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
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    Column(modifier) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = back) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
            },
            title = {
            },
            actions = {
                IconButton(onClick = showImage) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "add image",
                    )
                }
                IconButton(onClick = changeToCheck) {
                    Icon(
                        imageVector = Icons.Default.Checklist,
                        contentDescription = "add checkList",
                    )
                }
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "back",
                        tint = Color.Red,
                    )
                }
            },
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            item {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = currentNoteUiState.title,
                    onValueChange = onTitleChange,
                    placeholder = {
                        Text(text = "Title")
                    },
                    textStyle = MaterialTheme.typography.titleLarge,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        autoCorrect = true,
                        imeAction = ImeAction.Next,
                    ),
                )
            }
            itemsIndexed(
                currentNoteUiState.contents,
                key = { index, _ -> index },
                contentType = { _, item -> item.type },
            ) { index, item ->
                NoteItemUi(
                    modifier = Modifier
                        .onFocusChanged {
                            onFocusChange(it.isFocused, index)
                        },
                    noteItemUiState = item,
                    index = index,
                    onContentChange = onContentChange,
                    onCheckChange = onCheckChange,
                    addNewItem = addNewItem,
                )
            }
        }
    }

    if (showDialog) {
        DeleteDialog(onDismiss = { showDialog = false }, onDelete = onDeleteNote)
    }
    TrackScreenViewEvent(screenName = "Detail")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItemUi(
    index: Int,
    modifier: Modifier = Modifier,
    noteItemUiState: NoteItemUiState,
    onContentChange: (String, Int) -> Unit,
    onCheckChange: (Boolean, Int) -> Unit,
    addNewItem: (Int) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }

//    val focusRequester2 = remember {
//        BringIntoViewRequester()
//    }
    LaunchedEffect(key1 = noteItemUiState, block = {
        if (noteItemUiState.isFocus) {
            focusRequester.requestFocus()
            //  focusRequester2.bringIntoView()
        }
    })
    when (noteItemUiState.type) {
        Type.TEXT -> {
            TextField(
                modifier = modifier
//                    .bringIntoViewRequester(focusRequester2)
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),

                value = noteItemUiState.content,
                onValueChange = { onContentChange(it, index) },
                placeholder = {
                    Text(text = "Content")
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),

                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions { addNewItem(index) },

                maxLines = 1,
            )
        }

        Type.CHECK -> {
            TextField(
                modifier = modifier
                    .focusRequester(focusRequester)
//                    .bringIntoViewRequester(focusRequester2)
                    .fillMaxWidth(),

                value = noteItemUiState.content,
                onValueChange = { onContentChange(it, index) },
                placeholder = {
                    Text(text = "Content")
                },
                leadingIcon = {
                    IconButton(onClick = { onCheckChange(!noteItemUiState.isCheck, index) }) {
                        Icon(
                            imageVector =
                            if (noteItemUiState.isCheck) {
                                Icons.Outlined.CheckCircleOutline
                            } else {
                                Icons.Outlined.RadioButtonUnchecked
                            },
                            contentDescription = "circle",
                            tint = MaterialTheme.colorScheme.primary,

                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions { addNewItem(index) },
                maxLines = 1,
            )
        }

        Type.IMAGE -> {
            AsyncImage(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                model = noteItemUiState.content,
                contentDescription = "",
                contentScale = ContentScale.Fit,
            )
        }
    }
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

@Composable
fun DeleteDialog(
    onDismiss: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDelete) {
                Text(text = "Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        },
        title = {
            Text(text = "Delete Note")
        },
        icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = "delete") },
        text = {
            Text(text = "Are you sure you want to delete this note?")
        },
    )
}

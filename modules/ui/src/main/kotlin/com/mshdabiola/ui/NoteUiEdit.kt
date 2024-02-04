/*
 *abiola 2024
 */

package com.mshdabiola.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mshdabiola.designsystem.component.NoteTextField
import com.mshdabiola.model.Type

@Composable
fun NoteItemUi(
    index: Int,
    modifier: Modifier = Modifier,
    noteItemUiState: NoteItemUiState,
    onContentChange: (String, Int) -> Unit,
    onCheckChange: (Boolean, Int) -> Unit,
    addNewItem: (Int) -> Unit = {},
    onImageClick: (String) -> Unit = {},
    deleteImage: (Int) -> Unit = {},
) {
    val focusRequester = remember {
        FocusRequester()
    }

//    val focusRequester2 = remember {
//        BringIntoViewRequester()
//    }
    LaunchedEffect(
        key1 = noteItemUiState,
        block = {
            if (noteItemUiState.isFocus) {
                focusRequester.requestFocus()
                //  focusRequester2.bringIntoView()
            }
        },
    )
    when (noteItemUiState.type) {
        Type.TEXT -> {
            NoteTextField(
                modifier = modifier

                    .focusRequester(focusRequester)
//                    .bringIntoViewRequester(focusRequester2)
                    .fillMaxWidth(),
                value = noteItemUiState.content,
                onValueChange ={ onContentChange(it, index) },
                placeholder = "Content",
                imeAction = ImeAction.Next,
                keyboardAction = { addNewItem(index) }
                )
        }

        Type.CHECK -> {

            NoteTextField(
                modifier = modifier
                    .focusRequester(focusRequester)
//                    .bringIntoViewRequester(focusRequester2)
                    .fillMaxWidth(),
                value = noteItemUiState.content,
                onValueChange ={ onContentChange(it, index) },
                placeholder = "Content",
                imeAction = ImeAction.Next,
                keyboardAction = { addNewItem(index) },
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
                }
            )

        }

        Type.IMAGE -> {
            Box(modifier.padding(horizontal = 16.dp)) {
                AsyncImage(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .height(128.dp)
                        .fillMaxWidth()
                        .clickable { onImageClick(noteItemUiState.content) },
                    model = noteItemUiState.content,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )

                FilledTonalIconButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = { deleteImage(index) },
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                }
            }
        }
    }
}

fun LazyListScope.noteUiEdit(
    noteUiState: NoteUiState,
    onTitleChange: (String) -> Unit = {},
    onContentChange: (String, Int) -> Unit = { _, _ -> },
    onCheckChange: (Boolean, Int) -> Unit = { _, _ -> },
    onFocusChange: (Boolean, Int) -> Unit = { _, _ -> },
    addNewItem: (Int) -> Unit = {},
    onImageClick: (String) -> Unit = {},
    deleteImage: (Int) -> Unit = {},
) {
    item {
        NoteTextField(
            modifier = Modifier.testTag("detail:title"),
            value = noteUiState.title,
            onValueChange = onTitleChange,
            placeholder = "Title",
            textStyle = MaterialTheme.typography.titleLarge,
            imeAction = ImeAction.Next,
        )
    }
    itemsIndexed(
        noteUiState.contents,
        key = { index, _ -> index },
        contentType = { _, item -> item.type },
    ) { index, item ->
        NoteItemUi(
            modifier = Modifier
                .testTag("detail:content:$index")
                .onFocusChanged {
                    onFocusChange(it.isFocused, index)
                },
            noteItemUiState = item,
            index = index,
            onContentChange = onContentChange,
            onCheckChange = onCheckChange,
            addNewItem = addNewItem,
            onImageClick = onImageClick,
            deleteImage = deleteImage,
        )
    }
}

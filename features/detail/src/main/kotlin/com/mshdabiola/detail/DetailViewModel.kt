/*
 *abiola 2022
 */

package com.mshdabiola.detail

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.NoteRepository
import com.mshdabiola.detail.navigation.DETAIL_ID_ARG
import com.mshdabiola.detail.navigation.DetailArgs
import com.mshdabiola.model.Note
import com.mshdabiola.model.NoteItem
import com.mshdabiola.model.Type
import com.mshdabiola.ui.NoteItemUiState
import com.mshdabiola.ui.NoteUiState
import com.mshdabiola.ui.asNoteUiState
import com.mshdabiola.ui.toNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.minus
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val noteRepository: NoteRepository,
    private val contentManager: ContentManager,
) : ViewModel() {
    private val noteIdArgs: DetailArgs = DetailArgs(savedStateHandle)

    private val noteId = noteIdArgs.id

    private val _mainState = MutableStateFlow(NoteUiState())
    val mainState = _mainState.asStateFlow()
    private var currentIndex: Int? = null

    private var _currentNote = mutableStateOf(
        NoteUiState(
            id = 0,
            title = "",
            createdAtStr = "",
        ),
    )
    val currentNote: State<NoteUiState> = _currentNote

    init {

        viewModelScope.launch {
            initNote()
        }
        viewModelScope.launch {
            snapshotFlow {
                _currentNote.value
            }
                .collectLatest {
                    if (it.id != 0L) {
                        noteRepository.upsert(it.toNote())
                    }
                }
        }
    }

    private suspend fun initNote() {
        if (noteId == 0L) {
            val note = Note(
                id = null,
                title = "",
                contents = listOf(NoteItem()),
                createdAt = System.currentTimeMillis(),
                createdAtStrng = "Today",
            )
            val id = noteRepository.upsert(note)

            savedStateHandle[DETAIL_ID_ARG] = id
            _currentNote.value = note.copy(id = id).asNoteUiState()
        } else {
            noteRepository
                .getOne(noteId)
                .first()
                ?.let {
                    _currentNote.value = it.asNoteUiState()
                }
        }
    }

    fun onDeleteButtonClick() {
        val id = savedStateHandle.get<Long>(DETAIL_ID_ARG)
        id?.let {
            deleteNote(it)
        }
    }

    private fun deleteNote(id: Long) {
        viewModelScope.launch {
            noteRepository.delete(id)
        }
    }

    // TOdo delete bug

    fun onTitleChange(text: String) {
        _currentNote.value =
            _currentNote.value.copy(title = text, createdAt = System.currentTimeMillis())
    }

    fun onContentChange(text: String, index: Int) {
        var notes = currentNote.value
        val contents = notes.contents
            .map { it.copy(isFocus = false) }
            .toMutableList()
        val note = contents[index]
        when {
            text.isBlank() && note.type == Type.CHECK -> {
                contents[index] = note.copy(content = text, type = Type.TEXT, isFocus = true)
            }

            text.isBlank() && note.type == Type.TEXT -> {
                val lastNote = contents[index - 1]
                if (lastNote.type != Type.IMAGE) {
                    contents.removeAt(index)
                    contents[index - 1] = contents[index - 1].copy(isFocus = true)
                } else {
                    contents[index] = contents[index].copy(content = text)
                }
            }

            else -> {
                contents[index] = contents[index].copy(content = text)
            }
        }
        notes = notes.copy(contents = contents.toImmutableList())

        _currentNote.value = notes
    }

    fun onCheckChange(isCheck: Boolean, index: Int) {
        var notes = currentNote.value
        val contents = notes.contents
            .map { it.copy(isFocus = false) }
            .toMutableList()
        contents[index] = contents[index].copy(isCheck = isCheck, isFocus = true)
        notes = notes.copy(contents = contents.toImmutableList())

        _currentNote.value = notes
    }

    fun onFocusChange(isFocus: Boolean, index: Int) {
        currentIndex = if (isFocus) index else null
        Timber.d("focus index $currentIndex")
    }

    fun changeToCheck() {
        if (currentIndex != null) {
            var notes = currentNote.value
            val contents = notes.contents
                .map { it.copy(isFocus = false) }
                .toMutableList()
            val item = contents[currentIndex!!]
            contents[currentIndex!!] = item.copy(
                type = if (item.type == Type.CHECK) Type.TEXT else Type.CHECK,
                isCheck = false,
                isFocus = true,
            )
            notes = notes.copy(contents = contents.toImmutableList())

            _currentNote.value = notes
        }
    }

    fun addImage(id: Long) {
        var notes = currentNote.value
        val contents = notes.contents
            .map { it.copy(isFocus = false) }
            .toMutableList()

        contents.add(
            NoteItemUiState(
                type = Type.IMAGE,
                content = contentManager.getImagePath(id),
            ),
        ) // = contents[index].copy(content = text)
        contents.add(NoteItemUiState(type = Type.TEXT)) // = contents[index].copy(content = text)

        notes = notes.copy(contents = contents.toImmutableList())

        _currentNote.value = notes
    }

    fun addNewItem(index: Int) {
        var notes = currentNote.value
        val contents = notes.contents
            .map { it.copy(isFocus = false) }
            .toMutableList()

        val lastNote = contents.last()
        val lastIndex = contents.lastIndex
        if (index == lastIndex) {
            contents.add(
                NoteItemUiState(
                    type = lastNote.type,
                    isFocus = true,
                ),
            ) // = contents[index].copy(content = text)
        } else {
            val nextId = if (contents[index + 1].type == Type.IMAGE)2 else 1
            contents[index + nextId] = contents[index + nextId].copy(isFocus = true)
        }
        notes = notes.copy(contents = contents.toImmutableList())

        _currentNote.value = notes
    }

    fun savePhoto(uri: Uri, id: Long) {
        viewModelScope.launch {
            contentManager.saveImage(uri, id)
        }
    }

    fun deleteImage(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            var notes = currentNote.value
            val contents = notes.contents
                .map { it.copy(isFocus = false) }
                .toMutableList()

            val note = contents.removeAt(index)

            notes = notes.copy(contents = contents.toImmutableList())
            _currentNote.value = notes

            contentManager.deleteImage(note.content)
        }
    }

    fun getPhotoUri(id: Long): Uri {
        return contentManager.pictureUri(id)
    }
}

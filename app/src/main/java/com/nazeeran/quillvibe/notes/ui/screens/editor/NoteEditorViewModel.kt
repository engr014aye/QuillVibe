package com.nazeeran.quillvibe.notes.ui.screens.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nazeeran.quillvibe.notes.data.local.entity.NoteEntity
import com.nazeeran.quillvibe.notes.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class NoteEditorUiState(
    val noteId: Long = 0L,
    val title: String = "",
    val content: String = "",
    val category: String = "General",
    val colorIndex: Int = 0,
    val isPinned: Boolean = false,
    val wordCount: Int = 0,
    val charCount: Int = 0,
    val isLoaded: Boolean = false
)

class NoteEditorViewModel(
    private val noteId: Long,
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState(noteId = noteId))
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    private var initialTitle: String = ""
    private var initialContent: String = ""
    private var initialCategory: String = "General"
    private var initialColorIndex: Int = 0
    private var initialIsPinned: Boolean = false

    init {
        if (noteId > 0L) {
            viewModelScope.launch {
                val existing = repository.getNoteById(noteId).firstOrNull()
                existing?.let { item ->
                    val note = item.note
                    initialTitle = note.title
                    initialContent = note.content
                    initialCategory = note.category
                    initialColorIndex = note.colorIndex
                    initialIsPinned = note.isPinned

                    val words = calculateWordCount(note.content)
                    _uiState.value = NoteEditorUiState(
                        noteId = note.id,
                        title = note.title,
                        content = note.content,
                        category = note.category,
                        colorIndex = note.colorIndex,
                        isPinned = note.isPinned,
                        wordCount = words,
                        charCount = note.content.length,
                        isLoaded = true
                    )
                }
            }
        } else {
            _uiState.value = _uiState.value.copy(isLoaded = true)
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle)
    }

    fun onContentChange(newContent: String) {
        val words = calculateWordCount(newContent)
        _uiState.value = _uiState.value.copy(
            content = newContent,
            wordCount = words,
            charCount = newContent.length
        )
    }

    fun onCategoryChange(newCategory: String) {
        _uiState.value = _uiState.value.copy(category = newCategory)
    }

    fun onColorIndexChange(newIndex: Int) {
        _uiState.value = _uiState.value.copy(colorIndex = newIndex)
    }

    fun togglePin() {
        _uiState.value = _uiState.value.copy(isPinned = !_uiState.value.isPinned)
    }

    fun saveNote(onSaved: (() -> Unit)? = null) {
        val state = _uiState.value
        if (state.title.isNotBlank() || state.content.isNotBlank()) {
            viewModelScope.launch {
                val entity = NoteEntity(
                    id = state.noteId,
                    title = state.title.trim(),
                    content = state.content.trim(),
                    category = state.category,
                    colorIndex = state.colorIndex,
                    isPinned = state.isPinned,
                    isChecklist = false,
                    isDeleted = false,
                    updatedAt = System.currentTimeMillis()
                )
                val newId = repository.saveNote(entity)
                _uiState.value = _uiState.value.copy(noteId = newId)
                onSaved?.invoke()
            }
        } else {
            onSaved?.invoke()
        }
    }

    fun deleteNote(onDeleted: () -> Unit) {
        if (_uiState.value.noteId > 0L) {
            viewModelScope.launch {
                repository.softDeleteNote(_uiState.value.noteId)
                onDeleted()
            }
        } else {
            onDeleted()
        }
    }

    private fun calculateWordCount(text: String): Int {
        if (text.isBlank()) return 0
        return text.trim().split("\\s+".toRegex()).size
    }

    class Factory(
        private val noteId: Long,
        private val repository: NoteRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NoteEditorViewModel(noteId, repository) as T
        }
    }
}

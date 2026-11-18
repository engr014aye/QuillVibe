package com.nazeeran.quillvibe.notes.ui.screens.checklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nazeeran.quillvibe.notes.data.local.entity.ChecklistItemEntity
import com.nazeeran.quillvibe.notes.data.local.entity.NoteEntity
import com.nazeeran.quillvibe.notes.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class ChecklistEditorUiState(
    val noteId: Long = 0L,
    val title: String = "",
    val category: String = "Personal",
    val colorIndex: Int = 0,
    val isPinned: Boolean = false,
    val items: List<ChecklistItemEntity> = emptyList(),
    val isLoaded: Boolean = false
) {
    val totalCount: Int get() = items.size
    val doneCount: Int get() = items.count { it.isDone }
    val progress: Float get() = if (totalCount == 0) 0f else doneCount.toFloat() / totalCount
    val progressSummary: String get() = if (totalCount == 0) "No items yet" else "$doneCount of $totalCount completed (${(progress * 100).toInt()}%)"
}

class ChecklistEditorViewModel(
    private val noteId: Long,
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChecklistEditorUiState(noteId = noteId))
    val uiState: StateFlow<ChecklistEditorUiState> = _uiState.asStateFlow()

    init {
        if (noteId > 0L) {
            viewModelScope.launch {
                val existing = repository.getNoteById(noteId).firstOrNull()
                existing?.let { item ->
                    _uiState.value = ChecklistEditorUiState(
                        noteId = item.note.id,
                        title = item.note.title,
                        category = item.note.category,
                        colorIndex = item.note.colorIndex,
                        isPinned = item.note.isPinned,
                        items = item.sortedChecklistItems,
                        isLoaded = true
                    )
                }
            }
        } else {
            // New checklist: start with 1 empty item
            _uiState.value = _uiState.value.copy(
                items = listOf(ChecklistItemEntity(text = "", position = 0)),
                isLoaded = true
            )
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle)
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

    fun onItemTextChange(index: Int, newText: String) {
        val currentList = _uiState.value.items.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(text = newText)
            _uiState.value = _uiState.value.copy(items = currentList)
        }
    }

    fun onItemToggle(index: Int) {
        val currentList = _uiState.value.items.toMutableList()
        if (index in currentList.indices) {
            currentList[index] = currentList[index].copy(isDone = !currentList[index].isDone)
            _uiState.value = _uiState.value.copy(items = currentList)
        }
    }

    fun addNewItem(): Int {
        val currentList = _uiState.value.items.toMutableList()
        val newItem = ChecklistItemEntity(
            text = "",
            isDone = false,
            position = currentList.size
        )
        currentList.add(newItem)
        _uiState.value = _uiState.value.copy(items = currentList)
        return currentList.size - 1
    }

    fun removeItem(index: Int) {
        val currentList = _uiState.value.items.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            // Re-index positions
            val reindexed = currentList.mapIndexed { idx, itm -> itm.copy(position = idx) }
            _uiState.value = _uiState.value.copy(items = reindexed)
        }
    }

    fun saveChecklist(onSaved: (() -> Unit)? = null) {
        val state = _uiState.value
        val validItems = state.items.filter { it.text.isNotBlank() }

        if (state.title.isNotBlank() || validItems.isNotEmpty()) {
            viewModelScope.launch {
                val entity = NoteEntity(
                    id = state.noteId,
                    title = state.title.trim(),
                    content = "",
                    category = state.category,
                    colorIndex = state.colorIndex,
                    isPinned = state.isPinned,
                    isChecklist = true,
                    isDeleted = false,
                    updatedAt = System.currentTimeMillis()
                )
                val newId = repository.saveChecklist(entity, validItems)
                _uiState.value = _uiState.value.copy(noteId = newId)
                onSaved?.invoke()
            }
        } else {
            onSaved?.invoke()
        }
    }

    fun deleteChecklist(onDeleted: () -> Unit) {
        if (_uiState.value.noteId > 0L) {
            viewModelScope.launch {
                repository.softDeleteNote(_uiState.value.noteId)
                onDeleted()
            }
        } else {
            onDeleted()
        }
    }

    class Factory(
        private val noteId: Long,
        private val repository: NoteRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChecklistEditorViewModel(noteId, repository) as T
        }
    }
}

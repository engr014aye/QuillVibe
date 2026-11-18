package com.nazeeran.quillvibe.notes.ui.screens.trash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nazeeran.quillvibe.notes.data.local.entity.NoteWithChecklist
import com.nazeeran.quillvibe.notes.data.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrashViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    val trashItems: StateFlow<List<NoteWithChecklist>> = repository.getTrashNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun restore(noteId: Long) {
        viewModelScope.launch {
            repository.restoreNote(noteId)
        }
    }

    fun deleteForever(noteId: Long) {
        viewModelScope.launch {
            repository.hardDeleteNote(noteId)
        }
    }

    fun emptyTrash() {
        viewModelScope.launch {
            repository.emptyTrash()
        }
    }

    class Factory(
        private val repository: NoteRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TrashViewModel(repository) as T
        }
    }
}

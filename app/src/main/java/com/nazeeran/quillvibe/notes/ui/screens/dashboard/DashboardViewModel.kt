package com.nazeeran.quillvibe.notes.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nazeeran.quillvibe.notes.data.local.PreferencesManager
import com.nazeeran.quillvibe.notes.data.local.entity.ChecklistItemEntity
import com.nazeeran.quillvibe.notes.data.local.entity.NoteWithChecklist
import com.nazeeran.quillvibe.notes.data.model.AppThemeMode
import com.nazeeran.quillvibe.notes.data.repository.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardUiState(
    val items: List<NoteWithChecklist> = emptyList(),
    val pinnedItems: List<NoteWithChecklist> = emptyList(),
    val otherItems: List<NoteWithChecklist> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val isGridLayout: Boolean = true,
    val themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    val hapticsEnabled: Boolean = true,
    val isLoading: Boolean = false
)

private data class FilterState(
    val query: String,
    val category: String
)

private data class PrefsState(
    val isGrid: Boolean,
    val theme: AppThemeMode,
    val haptics: Boolean
)

class DashboardViewModel(
    private val repository: NoteRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val filterState = combine(_searchQuery, _selectedCategory) { query, category ->
        FilterState(query, category)
    }

    private val prefsState = combine(
        preferencesManager.isGridLayout,
        preferencesManager.themeMode,
        preferencesManager.hapticsEnabled
    ) { isGrid, theme, haptics ->
        PrefsState(isGrid, theme, haptics)
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.getActiveNotes(),
        filterState,
        prefsState
    ) { allNotes, filter, prefs ->
        val filtered = allNotes.filter { item ->
            // Filter by search query across title, content, and subtask text
            val matchesQuery = if (filter.query.isBlank()) {
                true
            } else {
                val q = filter.query.trim().lowercase()
                item.note.title.lowercase().contains(q) ||
                        item.note.content.lowercase().contains(q) ||
                        item.checklistItems.any { it.text.lowercase().contains(q) }
            }

            // Filter by category or type
            val matchesCategory = when (filter.category.lowercase()) {
                "all" -> true
                "pinned" -> item.note.isPinned
                "notes" -> !item.note.isChecklist
                "checklists" -> item.note.isChecklist
                else -> item.note.category.equals(filter.category, ignoreCase = true)
            }

            matchesQuery && matchesCategory
        }

        val pinned = filtered.filter { it.note.isPinned }
        val other = filtered.filter { !it.note.isPinned }

        DashboardUiState(
            items = filtered,
            pinnedItems = pinned,
            otherItems = other,
            searchQuery = filter.query,
            selectedCategory = filter.category,
            isGridLayout = prefs.isGrid,
            themeMode = prefs.theme,
            hapticsEnabled = prefs.haptics,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
    }

    fun toggleLayoutMode() {
        preferencesManager.setGridLayout(!uiState.value.isGridLayout)
    }

    fun cycleTheme() {
        val nextTheme = when (uiState.value.themeMode) {
            AppThemeMode.SYSTEM -> AppThemeMode.LIGHT
            AppThemeMode.LIGHT -> AppThemeMode.DARK
            AppThemeMode.DARK -> AppThemeMode.SYSTEM
        }
        preferencesManager.setThemeMode(nextTheme)
    }

    fun togglePin(item: NoteWithChecklist) {
        viewModelScope.launch {
            repository.setPinStatus(item.note.id, !item.note.isPinned)
        }
    }

    fun toggleChecklistItem(item: ChecklistItemEntity) {
        viewModelScope.launch {
            repository.toggleChecklistItem(item.id, !item.isDone)
        }
    }

    fun moveToTrash(noteId: Long) {
        viewModelScope.launch {
            repository.softDeleteNote(noteId)
        }
    }

    class Factory(
        private val repository: NoteRepository,
        private val preferencesManager: PreferencesManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DashboardViewModel(repository, preferencesManager) as T
        }
    }
}

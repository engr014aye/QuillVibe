package com.nazeeran.quillvibe.notes.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nazeeran.quillvibe.notes.data.local.BackupManager
import com.nazeeran.quillvibe.notes.data.local.PreferencesManager
import com.nazeeran.quillvibe.notes.data.model.AppThemeMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class SettingsEvent {
    data class ShowToast(val message: String) : SettingsEvent()
}

class SettingsViewModel(
    private val preferencesManager: PreferencesManager,
    private val backupManager: BackupManager
) : ViewModel() {

    val themeMode: StateFlow<AppThemeMode> = preferencesManager.themeMode
    val hapticsEnabled: StateFlow<Boolean> = preferencesManager.hapticsEnabled

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events: SharedFlow<SettingsEvent> = _events.asSharedFlow()

    fun setThemeMode(mode: AppThemeMode) {
        preferencesManager.setThemeMode(mode)
    }

    fun setHapticsEnabled(enabled: Boolean) {
        preferencesManager.setHapticsEnabled(enabled)
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            val result = backupManager.exportBackup(uri)
            result.onSuccess { count ->
                _events.emit(SettingsEvent.ShowToast("Successfully exported $count notes & checklists!"))
            }.onFailure { error ->
                _events.emit(SettingsEvent.ShowToast("Export failed: ${error.localizedMessage}"))
            }
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            val result = backupManager.importBackup(uri)
            result.onSuccess { count ->
                _events.emit(SettingsEvent.ShowToast("Successfully restored $count notes & checklists!"))
            }.onFailure { error ->
                _events.emit(SettingsEvent.ShowToast("Import failed: ${error.localizedMessage}"))
            }
        }
    }

    class Factory(
        private val preferencesManager: PreferencesManager,
        private val backupManager: BackupManager
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(preferencesManager, backupManager) as T
        }
    }
}

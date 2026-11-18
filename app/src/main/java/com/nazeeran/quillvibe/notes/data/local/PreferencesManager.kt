package com.nazeeran.quillvibe.notes.data.local

import android.content.Context
import android.content.SharedPreferences
import com.nazeeran.quillvibe.notes.data.model.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _hapticsEnabled = MutableStateFlow(loadHapticsEnabled())
    val hapticsEnabled: StateFlow<Boolean> = _hapticsEnabled.asStateFlow()

    private val _isGridLayout = MutableStateFlow(loadGridLayout())
    val isGridLayout: StateFlow<Boolean> = _isGridLayout.asStateFlow()

    private fun loadThemeMode(): AppThemeMode {
        val name = prefs.getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.name)
        return AppThemeMode.fromString(name)
    }

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _themeMode.value = mode
    }

    private fun loadHapticsEnabled(): Boolean {
        return prefs.getBoolean(KEY_HAPTICS_ENABLED, true)
    }

    fun setHapticsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTICS_ENABLED, enabled).apply()
        _hapticsEnabled.value = enabled
    }

    private fun loadGridLayout(): Boolean {
        return prefs.getBoolean(KEY_IS_GRID_LAYOUT, true)
    }

    fun setGridLayout(isGrid: Boolean) {
        prefs.edit().putBoolean(KEY_IS_GRID_LAYOUT, isGrid).apply()
        _isGridLayout.value = isGrid
    }

    companion object {
        private const val PREFS_NAME = "quillvibe_prefs"
        private const val KEY_THEME_MODE = "key_theme_mode"
        private const val KEY_HAPTICS_ENABLED = "key_haptics_enabled"
        private const val KEY_IS_GRID_LAYOUT = "key_is_grid_layout"
    }
}

package com.nazeeran.quillvibe.notes.data.model

enum class AppThemeMode(val title: String) {
    SYSTEM("System Default"),
    LIGHT("Pure Light"),
    DARK("OLED Dark");

    companion object {
        fun fromString(name: String?): AppThemeMode {
            return entries.find { it.name.equals(name, ignoreCase = true) } ?: SYSTEM
        }
    }
}

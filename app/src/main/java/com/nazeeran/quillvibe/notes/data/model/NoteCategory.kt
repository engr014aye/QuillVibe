package com.nazeeran.quillvibe.notes.data.model

enum class NoteCategory(val displayName: String) {
    ALL("All"),
    PERSONAL("Personal"),
    WORK("Work"),
    IDEAS("Ideas"),
    GENERAL("General");

    companion object {
        fun fromString(value: String): NoteCategory {
            return entries.find { it.displayName.equals(value, ignoreCase = true) } ?: GENERAL
        }
    }
}

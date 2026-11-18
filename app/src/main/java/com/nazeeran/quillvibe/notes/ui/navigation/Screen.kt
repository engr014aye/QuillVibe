package com.nazeeran.quillvibe.notes.ui.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    
    data object NoteEditor : Screen("note_editor?noteId={noteId}") {
        fun createRoute(noteId: Long = 0L): String = "note_editor?noteId=$noteId"
    }

    data object ChecklistEditor : Screen("checklist_editor?noteId={noteId}") {
        fun createRoute(noteId: Long = 0L): String = "checklist_editor?noteId=$noteId"
    }

    data object Trash : Screen("trash")
    data object Settings : Screen("settings")
}

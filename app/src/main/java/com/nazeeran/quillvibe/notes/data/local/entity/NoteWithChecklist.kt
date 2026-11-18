package com.nazeeran.quillvibe.notes.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithChecklist(
    @Embedded
    val note: NoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val checklistItems: List<ChecklistItemEntity> = emptyList()
) {
    val sortedChecklistItems: List<ChecklistItemEntity>
        get() = checklistItems.sortedBy { it.position }

    val totalCount: Int
        get() = checklistItems.size

    val doneCount: Int
        get() = checklistItems.count { it.isDone }

    val progress: Float
        get() = if (totalCount == 0) 0f else doneCount.toFloat() / totalCount

    val progressText: String
        get() = if (totalCount == 0) "0 items" else "$doneCount/$totalCount Done"

    val isAllDone: Boolean
        get() = totalCount > 0 && doneCount == totalCount
}

package com.nazeeran.quillvibe.notes.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.nazeeran.quillvibe.notes.data.local.entity.ChecklistItemEntity
import com.nazeeran.quillvibe.notes.data.local.entity.NoteEntity
import com.nazeeran.quillvibe.notes.data.local.entity.NoteWithChecklist
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM notes WHERE isDeleted = 0 ORDER BY isPinned DESC, updatedAt DESC")
    fun getActiveNotesWithChecklists(): Flow<List<NoteWithChecklist>>

    @Transaction
    @Query("SELECT * FROM notes WHERE isDeleted = 1 ORDER BY updatedAt DESC")
    fun getTrashNotesWithChecklists(): Flow<List<NoteWithChecklist>>

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    fun getNoteById(id: Long): Flow<NoteWithChecklist?>

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteByIdDirect(id: Long): NoteWithChecklist?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("UPDATE notes SET isDeleted = 1, isPinned = 0, updatedAt = :updatedAt WHERE id = :id")
    suspend fun softDeleteNote(id: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE notes SET isDeleted = 0, updatedAt = :updatedAt WHERE id = :id")
    suspend fun restoreNote(id: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun hardDeleteNote(id: Long)

    @Query("DELETE FROM notes WHERE isDeleted = 1")
    suspend fun emptyTrash()

    @Query("UPDATE notes SET isPinned = :isPinned, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setPinStatus(id: Long, isPinned: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItems(items: List<ChecklistItemEntity>)

    @Query("DELETE FROM checklist_items WHERE noteId = :noteId")
    suspend fun deleteChecklistItemsForNote(noteId: Long)

    @Update
    suspend fun updateChecklistItem(item: ChecklistItemEntity)

    @Query("UPDATE checklist_items SET isDone = :isDone WHERE id = :id")
    suspend fun setChecklistItemDone(id: Long, isDone: Boolean)

    @Transaction
    @Query("SELECT * FROM notes ORDER BY id ASC")
    suspend fun getAllNotesWithChecklistsDirect(): List<NoteWithChecklist>

    @Query("DELETE FROM notes")
    suspend fun clearAllNotes()

    @Transaction
    suspend fun replaceChecklistItems(noteId: Long, items: List<ChecklistItemEntity>) {
        deleteChecklistItemsForNote(noteId)
        if (items.isNotEmpty()) {
            val mappedItems = items.mapIndexed { index, item ->
                item.copy(id = 0L, noteId = noteId, position = index)
            }
            insertChecklistItems(mappedItems)
        }
    }
}

package com.nazeeran.quillvibe.notes.data.repository

import com.nazeeran.quillvibe.notes.data.local.dao.NoteDao
import com.nazeeran.quillvibe.notes.data.local.entity.ChecklistItemEntity
import com.nazeeran.quillvibe.notes.data.local.entity.NoteEntity
import com.nazeeran.quillvibe.notes.data.local.entity.NoteWithChecklist
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getActiveNotes(): Flow<List<NoteWithChecklist>>
    fun getTrashNotes(): Flow<List<NoteWithChecklist>>
    fun getNoteById(id: Long): Flow<NoteWithChecklist?>
    suspend fun getNoteByIdDirect(id: Long): NoteWithChecklist?
    suspend fun saveNote(note: NoteEntity): Long
    suspend fun saveChecklist(note: NoteEntity, items: List<ChecklistItemEntity>): Long
    suspend fun toggleChecklistItem(id: Long, isDone: Boolean)
    suspend fun setPinStatus(id: Long, isPinned: Boolean)
    suspend fun softDeleteNote(id: Long)
    suspend fun restoreNote(id: Long)
    suspend fun hardDeleteNote(id: Long)
    suspend fun emptyTrash()
}

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getActiveNotes(): Flow<List<NoteWithChecklist>> {
        return noteDao.getActiveNotesWithChecklists()
    }

    override fun getTrashNotes(): Flow<List<NoteWithChecklist>> {
        return noteDao.getTrashNotesWithChecklists()
    }

    override fun getNoteById(id: Long): Flow<NoteWithChecklist?> {
        return noteDao.getNoteById(id)
    }

    override suspend fun getNoteByIdDirect(id: Long): NoteWithChecklist? {
        return noteDao.getNoteByIdDirect(id)
    }

    override suspend fun saveNote(note: NoteEntity): Long {
        val updatedNote = note.copy(updatedAt = System.currentTimeMillis())
        return if (note.id == 0L) {
            noteDao.insertNote(updatedNote)
        } else {
            noteDao.updateNote(updatedNote)
            note.id
        }
    }

    override suspend fun saveChecklist(note: NoteEntity, items: List<ChecklistItemEntity>): Long {
        val updatedNote = note.copy(isChecklist = true, updatedAt = System.currentTimeMillis())
        val noteId = if (note.id == 0L) {
            noteDao.insertNote(updatedNote)
        } else {
            noteDao.updateNote(updatedNote)
            note.id
        }
        noteDao.replaceChecklistItems(noteId, items)
        return noteId
    }

    override suspend fun toggleChecklistItem(id: Long, isDone: Boolean) {
        noteDao.setChecklistItemDone(id, isDone)
    }

    override suspend fun setPinStatus(id: Long, isPinned: Boolean) {
        noteDao.setPinStatus(id, isPinned)
    }

    override suspend fun softDeleteNote(id: Long) {
        noteDao.softDeleteNote(id)
    }

    override suspend fun restoreNote(id: Long) {
        noteDao.restoreNote(id)
    }

    override suspend fun hardDeleteNote(id: Long) {
        noteDao.hardDeleteNote(id)
    }

    override suspend fun emptyTrash() {
        noteDao.emptyTrash()
    }
}

package com.nazeeran.quillvibe.notes.data.local

import android.content.Context
import android.net.Uri
import com.nazeeran.quillvibe.notes.data.local.dao.NoteDao
import com.nazeeran.quillvibe.notes.data.local.entity.ChecklistItemEntity
import com.nazeeran.quillvibe.notes.data.local.entity.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class BackupManager(
    private val context: Context,
    private val noteDao: NoteDao
) {

    suspend fun exportBackup(destinationUri: Uri): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val allNotes = noteDao.getAllNotesWithChecklistsDirect()
            val root = JSONObject()
            root.put("app", "QuillVibe")
            root.put("version", 1)
            root.put("exportedAt", System.currentTimeMillis())

            val notesArray = JSONArray()
            for (item in allNotes) {
                val nObj = JSONObject()
                nObj.put("title", item.note.title)
                nObj.put("content", item.note.content)
                nObj.put("category", item.note.category)
                nObj.put("colorIndex", item.note.colorIndex)
                nObj.put("isPinned", item.note.isPinned)
                nObj.put("isChecklist", item.note.isChecklist)
                nObj.put("isDeleted", item.note.isDeleted)
                nObj.put("createdAt", item.note.createdAt)
                nObj.put("updatedAt", item.note.updatedAt)

                if (item.checklistItems.isNotEmpty()) {
                    val itemsArray = JSONArray()
                    for (chk in item.checklistItems) {
                        val cObj = JSONObject()
                        cObj.put("text", chk.text)
                        cObj.put("isDone", chk.isDone)
                        cObj.put("position", chk.position)
                        itemsArray.put(cObj)
                    }
                    nObj.put("checklistItems", itemsArray)
                }
                notesArray.put(nObj)
            }
            root.put("notes", notesArray)

            context.contentResolver.openOutputStream(destinationUri)?.use { stream ->
                OutputStreamWriter(stream, Charsets.UTF_8).use { writer ->
                    writer.write(root.toString(2))
                    writer.flush()
                }
            } ?: return@withContext Result.failure(Exception("Cannot open destination file"))

            Result.success(allNotes.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun importBackup(sourceUri: Uri): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.contentResolver.openInputStream(sourceUri)?.use { stream ->
                BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                    reader.readText()
                }
            } ?: return@withContext Result.failure(Exception("Cannot open selected backup file"))

            val root = JSONObject(jsonString)
            if (!root.has("notes")) {
                return@withContext Result.failure(Exception("Invalid QuillVibe backup file format"))
            }

            val notesArray = root.getJSONArray("notes")
            var importedCount = 0

            for (i in 0 until notesArray.length()) {
                val nObj = notesArray.getJSONObject(i)
                val note = NoteEntity(
                    title = nObj.optString("title", ""),
                    content = nObj.optString("content", ""),
                    category = nObj.optString("category", "General"),
                    colorIndex = nObj.optInt("colorIndex", 0),
                    isPinned = nObj.optBoolean("isPinned", false),
                    isChecklist = nObj.optBoolean("isChecklist", false),
                    isDeleted = nObj.optBoolean("isDeleted", false),
                    createdAt = nObj.optLong("createdAt", System.currentTimeMillis()),
                    updatedAt = nObj.optLong("updatedAt", System.currentTimeMillis())
                )

                val noteId = noteDao.insertNote(note)

                if (nObj.has("checklistItems")) {
                    val itemsArray = nObj.getJSONArray("checklistItems")
                    val itemsList = mutableListOf<ChecklistItemEntity>()
                    for (j in 0 until itemsArray.length()) {
                        val cObj = itemsArray.getJSONObject(j)
                        itemsList.add(
                            ChecklistItemEntity(
                                noteId = noteId,
                                text = cObj.optString("text", ""),
                                isDone = cObj.optBoolean("isDone", false),
                                position = cObj.optInt("position", j)
                            )
                        )
                    }
                    if (itemsList.isNotEmpty()) {
                        noteDao.insertChecklistItems(itemsList)
                    }
                }
                importedCount++
            }

            Result.success(importedCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

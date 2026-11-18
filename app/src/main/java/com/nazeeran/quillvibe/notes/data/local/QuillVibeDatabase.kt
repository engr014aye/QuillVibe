package com.nazeeran.quillvibe.notes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nazeeran.quillvibe.notes.data.local.dao.NoteDao
import com.nazeeran.quillvibe.notes.data.local.entity.ChecklistItemEntity
import com.nazeeran.quillvibe.notes.data.local.entity.NoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [NoteEntity::class, ChecklistItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuillVibeDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: QuillVibeDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): QuillVibeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuillVibeDatabase::class.java,
                    "quillvibe_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.noteDao())
                    }
                }
            }
        }

        private suspend fun populateInitialData(noteDao: NoteDao) {
            val now = System.currentTimeMillis()

            // 1. Welcome Note (Pinned, Emerald Tint)
            val welcomeNote = NoteEntity(
                title = "Welcome to QuillVibe 🪶",
                content = """
                    QuillVibe is your private, distraction-free sanctuary for thoughts, ideas, and daily to-dos.
                    
                    🛡️ 100% Offline & Private:
                    • Zero internet access or tracking permissions.
                    • Your notes remain exclusively on your device.
                    • Safe local backup and restore in Settings.
                    
                    🎨 Craft & Fluidity:
                    • Modern design with pure light and OLED dark themes.
                    • Fluid animations and pleasant tactile feedback.
                    • Switch seamlessly between 2-column masonry grid and clean list view.
                    
                    Tap any note to edit or use the action button below to create your next idea!
                """.trimIndent(),
                category = "General",
                colorIndex = 1, // Sage / Mint
                isPinned = true,
                isChecklist = false,
                isDeleted = false,
                createdAt = now - 60000,
                updatedAt = now - 60000
            )
            noteDao.insertNote(welcomeNote)

            // 2. Interactive Getting Started Checklist
            val checklistNote = NoteEntity(
                title = "🚀 Quick Start Checklist",
                content = "",
                category = "Personal",
                colorIndex = 4, // Sky Ocean
                isPinned = true,
                isChecklist = true,
                isDeleted = false,
                createdAt = now - 30000,
                updatedAt = now - 30000
            )
            val checklistId = noteDao.insertNote(checklistNote)

            val checklistItems = listOf(
                ChecklistItemEntity(noteId = checklistId, text = "Explore pastel note card tints", isDone = true, position = 0),
                ChecklistItemEntity(noteId = checklistId, text = "Toggle between 2-Column Grid and List View", isDone = true, position = 1),
                ChecklistItemEntity(noteId = checklistId, text = "Try OLED Dark Mode in Settings", isDone = false, position = 2),
                ChecklistItemEntity(noteId = checklistId, text = "Create a new note with live word counter", isDone = false, position = 3),
                ChecklistItemEntity(noteId = checklistId, text = "Test local JSON backup in Settings", isDone = false, position = 4)
            )
            noteDao.insertChecklistItems(checklistItems)

            // 3. Creative Ideas Note (Peach Tint)
            val ideasNote = NoteEntity(
                title = "💡 Creative Thoughts & Sparks",
                content = """
                    "Simplicity is prerequisite for reliability." — Edsger W. Dijkstra
                    
                    Key principles to remember:
                    1. Focus on essential craftsmanship.
                    2. Maintain friction-free workflows.
                    3. Honor user privacy unconditionally.
                """.trimIndent(),
                category = "Ideas",
                colorIndex = 3, // Peach Amber
                isPinned = false,
                isChecklist = false,
                isDeleted = false,
                createdAt = now,
                updatedAt = now
            )
            noteDao.insertNote(ideasNote)
        }
    }
}

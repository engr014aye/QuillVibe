package com.nazeeran.quillvibe.notes

import android.app.Application
import com.nazeeran.quillvibe.notes.data.local.BackupManager
import com.nazeeran.quillvibe.notes.data.local.PreferencesManager
import com.nazeeran.quillvibe.notes.data.local.QuillVibeDatabase
import com.nazeeran.quillvibe.notes.data.repository.NoteRepository
import com.nazeeran.quillvibe.notes.data.repository.NoteRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class QuillVibeApp : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    val database by lazy { QuillVibeDatabase.getDatabase(this, applicationScope) }
    val repository: NoteRepository by lazy { NoteRepositoryImpl(database.noteDao()) }
    val preferencesManager by lazy { PreferencesManager(this) }
    val backupManager by lazy { BackupManager(this, database.noteDao()) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: QuillVibeApp
            private set
    }
}

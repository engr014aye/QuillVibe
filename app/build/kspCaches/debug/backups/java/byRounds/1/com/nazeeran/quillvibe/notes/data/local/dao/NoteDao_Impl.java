package com.nazeeran.quillvibe.notes.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nazeeran.quillvibe.notes.data.local.entity.ChecklistItemEntity;
import com.nazeeran.quillvibe.notes.data.local.entity.NoteEntity;
import com.nazeeran.quillvibe.notes.data.local.entity.NoteWithChecklist;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NoteDao_Impl implements NoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<NoteEntity> __insertionAdapterOfNoteEntity;

  private final EntityInsertionAdapter<ChecklistItemEntity> __insertionAdapterOfChecklistItemEntity;

  private final EntityDeletionOrUpdateAdapter<NoteEntity> __updateAdapterOfNoteEntity;

  private final EntityDeletionOrUpdateAdapter<ChecklistItemEntity> __updateAdapterOfChecklistItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfSoftDeleteNote;

  private final SharedSQLiteStatement __preparedStmtOfRestoreNote;

  private final SharedSQLiteStatement __preparedStmtOfHardDeleteNote;

  private final SharedSQLiteStatement __preparedStmtOfEmptyTrash;

  private final SharedSQLiteStatement __preparedStmtOfSetPinStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteChecklistItemsForNote;

  private final SharedSQLiteStatement __preparedStmtOfSetChecklistItemDone;

  private final SharedSQLiteStatement __preparedStmtOfClearAllNotes;

  public NoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNoteEntity = new EntityInsertionAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `notes` (`id`,`title`,`content`,`category`,`colorIndex`,`isPinned`,`isChecklist`,`isDeleted`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getContent());
        statement.bindString(4, entity.getCategory());
        statement.bindLong(5, entity.getColorIndex());
        final int _tmp = entity.isPinned() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.isChecklist() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        final int _tmp_2 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfChecklistItemEntity = new EntityInsertionAdapter<ChecklistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `checklist_items` (`id`,`noteId`,`text`,`isDone`,`position`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChecklistItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getNoteId());
        statement.bindString(3, entity.getText());
        final int _tmp = entity.isDone() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getPosition());
      }
    };
    this.__updateAdapterOfNoteEntity = new EntityDeletionOrUpdateAdapter<NoteEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `notes` SET `id` = ?,`title` = ?,`content` = ?,`category` = ?,`colorIndex` = ?,`isPinned` = ?,`isChecklist` = ?,`isDeleted` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final NoteEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getContent());
        statement.bindString(4, entity.getCategory());
        statement.bindLong(5, entity.getColorIndex());
        final int _tmp = entity.isPinned() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.isChecklist() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        final int _tmp_2 = entity.isDeleted() ? 1 : 0;
        statement.bindLong(8, _tmp_2);
        statement.bindLong(9, entity.getCreatedAt());
        statement.bindLong(10, entity.getUpdatedAt());
        statement.bindLong(11, entity.getId());
      }
    };
    this.__updateAdapterOfChecklistItemEntity = new EntityDeletionOrUpdateAdapter<ChecklistItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `checklist_items` SET `id` = ?,`noteId` = ?,`text` = ?,`isDone` = ?,`position` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ChecklistItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getNoteId());
        statement.bindString(3, entity.getText());
        final int _tmp = entity.isDone() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getPosition());
        statement.bindLong(6, entity.getId());
      }
    };
    this.__preparedStmtOfSoftDeleteNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET isDeleted = 1, isPinned = 0, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRestoreNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET isDeleted = 0, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfHardDeleteNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfEmptyTrash = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes WHERE isDeleted = 1";
        return _query;
      }
    };
    this.__preparedStmtOfSetPinStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET isPinned = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteChecklistItemsForNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM checklist_items WHERE noteId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetChecklistItemDone = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE checklist_items SET isDone = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAllNotes = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes";
        return _query;
      }
    };
  }

  @Override
  public Object insertNote(final NoteEntity note, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfNoteEntity.insertAndReturnId(note);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertChecklistItems(final List<ChecklistItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChecklistItemEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNote(final NoteEntity note, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfNoteEntity.handle(note);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateChecklistItem(final ChecklistItemEntity item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfChecklistItemEntity.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object replaceChecklistItems(final long noteId, final List<ChecklistItemEntity> items,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> NoteDao.DefaultImpls.replaceChecklistItems(NoteDao_Impl.this, noteId, items, __cont), $completion);
  }

  @Override
  public Object softDeleteNote(final long id, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSoftDeleteNote.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSoftDeleteNote.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object restoreNote(final long id, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRestoreNote.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRestoreNote.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object hardDeleteNote(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfHardDeleteNote.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfHardDeleteNote.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object emptyTrash(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfEmptyTrash.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfEmptyTrash.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setPinStatus(final long id, final boolean isPinned, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetPinStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isPinned ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetPinStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteChecklistItemsForNote(final long noteId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteChecklistItemsForNote.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, noteId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteChecklistItemsForNote.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setChecklistItemDone(final long id, final boolean isDone,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetChecklistItemDone.acquire();
        int _argIndex = 1;
        final int _tmp = isDone ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetChecklistItemDone.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAllNotes(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAllNotes.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAllNotes.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<NoteWithChecklist>> getActiveNotesWithChecklists() {
    final String _sql = "SELECT * FROM notes WHERE isDeleted = 0 ORDER BY isPinned DESC, updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"checklist_items",
        "notes"}, new Callable<List<NoteWithChecklist>>() {
      @Override
      @NonNull
      public List<NoteWithChecklist> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
            final int _cursorIndexOfColorIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorIndex");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfIsChecklist = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecklist");
            final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<ArrayList<ChecklistItemEntity>> _collectionChecklistItems = new LongSparseArray<ArrayList<ChecklistItemEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionChecklistItems.containsKey(_tmpKey)) {
                _collectionChecklistItems.put(_tmpKey, new ArrayList<ChecklistItemEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistItemsAscomNazeeranQuillvibeNotesDataLocalEntityChecklistItemEntity(_collectionChecklistItems);
            final List<NoteWithChecklist> _result = new ArrayList<NoteWithChecklist>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final NoteWithChecklist _item;
              final NoteEntity _tmpNote;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpCategory;
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
              final int _tmpColorIndex;
              _tmpColorIndex = _cursor.getInt(_cursorIndexOfColorIndex);
              final boolean _tmpIsPinned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp != 0;
              final boolean _tmpIsChecklist;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsChecklist);
              _tmpIsChecklist = _tmp_1 != 0;
              final boolean _tmpIsDeleted;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
              _tmpIsDeleted = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpNote = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategory,_tmpColorIndex,_tmpIsPinned,_tmpIsChecklist,_tmpIsDeleted,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<ChecklistItemEntity> _tmpChecklistItemsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpChecklistItemsCollection = _collectionChecklistItems.get(_tmpKey_1);
              _item = new NoteWithChecklist(_tmpNote,_tmpChecklistItemsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NoteWithChecklist>> getTrashNotesWithChecklists() {
    final String _sql = "SELECT * FROM notes WHERE isDeleted = 1 ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"checklist_items",
        "notes"}, new Callable<List<NoteWithChecklist>>() {
      @Override
      @NonNull
      public List<NoteWithChecklist> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
            final int _cursorIndexOfColorIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorIndex");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfIsChecklist = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecklist");
            final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<ArrayList<ChecklistItemEntity>> _collectionChecklistItems = new LongSparseArray<ArrayList<ChecklistItemEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionChecklistItems.containsKey(_tmpKey)) {
                _collectionChecklistItems.put(_tmpKey, new ArrayList<ChecklistItemEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistItemsAscomNazeeranQuillvibeNotesDataLocalEntityChecklistItemEntity(_collectionChecklistItems);
            final List<NoteWithChecklist> _result = new ArrayList<NoteWithChecklist>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final NoteWithChecklist _item;
              final NoteEntity _tmpNote;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpCategory;
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
              final int _tmpColorIndex;
              _tmpColorIndex = _cursor.getInt(_cursorIndexOfColorIndex);
              final boolean _tmpIsPinned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp != 0;
              final boolean _tmpIsChecklist;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsChecklist);
              _tmpIsChecklist = _tmp_1 != 0;
              final boolean _tmpIsDeleted;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
              _tmpIsDeleted = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpNote = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategory,_tmpColorIndex,_tmpIsPinned,_tmpIsChecklist,_tmpIsDeleted,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<ChecklistItemEntity> _tmpChecklistItemsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpChecklistItemsCollection = _collectionChecklistItems.get(_tmpKey_1);
              _item = new NoteWithChecklist(_tmpNote,_tmpChecklistItemsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<NoteWithChecklist> getNoteById(final long id) {
    final String _sql = "SELECT * FROM notes WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"checklist_items",
        "notes"}, new Callable<NoteWithChecklist>() {
      @Override
      @Nullable
      public NoteWithChecklist call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
            final int _cursorIndexOfColorIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorIndex");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfIsChecklist = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecklist");
            final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<ArrayList<ChecklistItemEntity>> _collectionChecklistItems = new LongSparseArray<ArrayList<ChecklistItemEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionChecklistItems.containsKey(_tmpKey)) {
                _collectionChecklistItems.put(_tmpKey, new ArrayList<ChecklistItemEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistItemsAscomNazeeranQuillvibeNotesDataLocalEntityChecklistItemEntity(_collectionChecklistItems);
            final NoteWithChecklist _result;
            if (_cursor.moveToFirst()) {
              final NoteEntity _tmpNote;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpCategory;
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
              final int _tmpColorIndex;
              _tmpColorIndex = _cursor.getInt(_cursorIndexOfColorIndex);
              final boolean _tmpIsPinned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp != 0;
              final boolean _tmpIsChecklist;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsChecklist);
              _tmpIsChecklist = _tmp_1 != 0;
              final boolean _tmpIsDeleted;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
              _tmpIsDeleted = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpNote = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategory,_tmpColorIndex,_tmpIsPinned,_tmpIsChecklist,_tmpIsDeleted,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<ChecklistItemEntity> _tmpChecklistItemsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpChecklistItemsCollection = _collectionChecklistItems.get(_tmpKey_1);
              _result = new NoteWithChecklist(_tmpNote,_tmpChecklistItemsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getNoteByIdDirect(final long id,
      final Continuation<? super NoteWithChecklist> $completion) {
    final String _sql = "SELECT * FROM notes WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<NoteWithChecklist>() {
      @Override
      @Nullable
      public NoteWithChecklist call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
            final int _cursorIndexOfColorIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorIndex");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfIsChecklist = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecklist");
            final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<ArrayList<ChecklistItemEntity>> _collectionChecklistItems = new LongSparseArray<ArrayList<ChecklistItemEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionChecklistItems.containsKey(_tmpKey)) {
                _collectionChecklistItems.put(_tmpKey, new ArrayList<ChecklistItemEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistItemsAscomNazeeranQuillvibeNotesDataLocalEntityChecklistItemEntity(_collectionChecklistItems);
            final NoteWithChecklist _result;
            if (_cursor.moveToFirst()) {
              final NoteEntity _tmpNote;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpCategory;
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
              final int _tmpColorIndex;
              _tmpColorIndex = _cursor.getInt(_cursorIndexOfColorIndex);
              final boolean _tmpIsPinned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp != 0;
              final boolean _tmpIsChecklist;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsChecklist);
              _tmpIsChecklist = _tmp_1 != 0;
              final boolean _tmpIsDeleted;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
              _tmpIsDeleted = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpNote = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategory,_tmpColorIndex,_tmpIsPinned,_tmpIsChecklist,_tmpIsDeleted,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<ChecklistItemEntity> _tmpChecklistItemsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpChecklistItemsCollection = _collectionChecklistItems.get(_tmpKey_1);
              _result = new NoteWithChecklist(_tmpNote,_tmpChecklistItemsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllNotesWithChecklistsDirect(
      final Continuation<? super List<NoteWithChecklist>> $completion) {
    final String _sql = "SELECT * FROM notes ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<NoteWithChecklist>>() {
      @Override
      @NonNull
      public List<NoteWithChecklist> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
            final int _cursorIndexOfColorIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorIndex");
            final int _cursorIndexOfIsPinned = CursorUtil.getColumnIndexOrThrow(_cursor, "isPinned");
            final int _cursorIndexOfIsChecklist = CursorUtil.getColumnIndexOrThrow(_cursor, "isChecklist");
            final int _cursorIndexOfIsDeleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isDeleted");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final LongSparseArray<ArrayList<ChecklistItemEntity>> _collectionChecklistItems = new LongSparseArray<ArrayList<ChecklistItemEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionChecklistItems.containsKey(_tmpKey)) {
                _collectionChecklistItems.put(_tmpKey, new ArrayList<ChecklistItemEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipchecklistItemsAscomNazeeranQuillvibeNotesDataLocalEntityChecklistItemEntity(_collectionChecklistItems);
            final List<NoteWithChecklist> _result = new ArrayList<NoteWithChecklist>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final NoteWithChecklist _item;
              final NoteEntity _tmpNote;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpCategory;
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
              final int _tmpColorIndex;
              _tmpColorIndex = _cursor.getInt(_cursorIndexOfColorIndex);
              final boolean _tmpIsPinned;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsPinned);
              _tmpIsPinned = _tmp != 0;
              final boolean _tmpIsChecklist;
              final int _tmp_1;
              _tmp_1 = _cursor.getInt(_cursorIndexOfIsChecklist);
              _tmpIsChecklist = _tmp_1 != 0;
              final boolean _tmpIsDeleted;
              final int _tmp_2;
              _tmp_2 = _cursor.getInt(_cursorIndexOfIsDeleted);
              _tmpIsDeleted = _tmp_2 != 0;
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpUpdatedAt;
              _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
              _tmpNote = new NoteEntity(_tmpId,_tmpTitle,_tmpContent,_tmpCategory,_tmpColorIndex,_tmpIsPinned,_tmpIsChecklist,_tmpIsDeleted,_tmpCreatedAt,_tmpUpdatedAt);
              final ArrayList<ChecklistItemEntity> _tmpChecklistItemsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpChecklistItemsCollection = _collectionChecklistItems.get(_tmpKey_1);
              _item = new NoteWithChecklist(_tmpNote,_tmpChecklistItemsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipchecklistItemsAscomNazeeranQuillvibeNotesDataLocalEntityChecklistItemEntity(
      @NonNull final LongSparseArray<ArrayList<ChecklistItemEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipchecklistItemsAscomNazeeranQuillvibeNotesDataLocalEntityChecklistItemEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`noteId`,`text`,`isDone`,`position` FROM `checklist_items` WHERE `noteId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "noteId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfNoteId = 1;
      final int _cursorIndexOfText = 2;
      final int _cursorIndexOfIsDone = 3;
      final int _cursorIndexOfPosition = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<ChecklistItemEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ChecklistItemEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpNoteId;
          _tmpNoteId = _cursor.getLong(_cursorIndexOfNoteId);
          final String _tmpText;
          _tmpText = _cursor.getString(_cursorIndexOfText);
          final boolean _tmpIsDone;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsDone);
          _tmpIsDone = _tmp != 0;
          final int _tmpPosition;
          _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
          _item_1 = new ChecklistItemEntity(_tmpId,_tmpNoteId,_tmpText,_tmpIsDone,_tmpPosition);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}

package com.ashish.stash.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ashish.stash.core.database.entity.DocumentLabelEntity;
import com.ashish.stash.core.database.entity.LabelEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
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
public final class LabelDao_Impl implements LabelDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LabelEntity> __insertionAdapterOfLabelEntity;

  private final EntityInsertionAdapter<DocumentLabelEntity> __insertionAdapterOfDocumentLabelEntity;

  private final EntityDeletionOrUpdateAdapter<LabelEntity> __deletionAdapterOfLabelEntity;

  private final EntityDeletionOrUpdateAdapter<LabelEntity> __updateAdapterOfLabelEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteDocumentLabel;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLabelsForDocument;

  public LabelDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLabelEntity = new EntityInsertionAdapter<LabelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `labels` (`label_id`,`name`) VALUES (nullif(?, 0),?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LabelEntity entity) {
        statement.bindLong(1, entity.getLabelId());
        statement.bindString(2, entity.getName());
      }
    };
    this.__insertionAdapterOfDocumentLabelEntity = new EntityInsertionAdapter<DocumentLabelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `document_labels` (`document_id`,`label_id`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DocumentLabelEntity entity) {
        statement.bindLong(1, entity.getDocumentId());
        statement.bindLong(2, entity.getLabelId());
      }
    };
    this.__deletionAdapterOfLabelEntity = new EntityDeletionOrUpdateAdapter<LabelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `labels` WHERE `label_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LabelEntity entity) {
        statement.bindLong(1, entity.getLabelId());
      }
    };
    this.__updateAdapterOfLabelEntity = new EntityDeletionOrUpdateAdapter<LabelEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `labels` SET `label_id` = ?,`name` = ? WHERE `label_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LabelEntity entity) {
        statement.bindLong(1, entity.getLabelId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getLabelId());
      }
    };
    this.__preparedStmtOfDeleteDocumentLabel = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM document_labels WHERE document_id = ? AND label_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteLabelsForDocument = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM document_labels WHERE document_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final LabelEntity label, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfLabelEntity.insertAndReturnId(label);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDocumentLabel(final DocumentLabelEntity documentLabel,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDocumentLabelEntity.insert(documentLabel);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final LabelEntity label, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfLabelEntity.handle(label);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final LabelEntity label, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfLabelEntity.handle(label);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDocumentLabel(final long documentId, final long labelId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteDocumentLabel.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, documentId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, labelId);
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
          __preparedStmtOfDeleteDocumentLabel.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLabelsForDocument(final long documentId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLabelsForDocument.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, documentId);
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
          __preparedStmtOfDeleteLabelsForDocument.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super LabelEntity> $completion) {
    final String _sql = "SELECT * FROM labels WHERE label_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<LabelEntity>() {
      @Override
      @Nullable
      public LabelEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLabelId = CursorUtil.getColumnIndexOrThrow(_cursor, "label_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final LabelEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpLabelId;
            _tmpLabelId = _cursor.getLong(_cursorIndexOfLabelId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _result = new LabelEntity(_tmpLabelId,_tmpName);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<LabelEntity>> observeAll() {
    final String _sql = "SELECT * FROM labels ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"labels"}, new Callable<List<LabelEntity>>() {
      @Override
      @NonNull
      public List<LabelEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLabelId = CursorUtil.getColumnIndexOrThrow(_cursor, "label_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final List<LabelEntity> _result = new ArrayList<LabelEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LabelEntity _item;
            final long _tmpLabelId;
            _tmpLabelId = _cursor.getLong(_cursorIndexOfLabelId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item = new LabelEntity(_tmpLabelId,_tmpName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<LabelEntity>> observeLabelsForDocument(final long documentId,
      final int showLocked) {
    final String _sql = "\n"
            + "        SELECT l.* FROM labels l\n"
            + "        JOIN document_labels dl ON l.label_id = dl.label_id\n"
            + "        JOIN documents d ON dl.document_id = d.document_id\n"
            + "        WHERE d.document_id = ? AND (? = 1 OR d.is_locked = 0)\n"
            + "        ORDER BY l.name ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, documentId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, showLocked);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"labels", "document_labels",
        "documents"}, new Callable<List<LabelEntity>>() {
      @Override
      @NonNull
      public List<LabelEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLabelId = CursorUtil.getColumnIndexOrThrow(_cursor, "label_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final List<LabelEntity> _result = new ArrayList<LabelEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LabelEntity _item;
            final long _tmpLabelId;
            _tmpLabelId = _cursor.getLong(_cursorIndexOfLabelId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            _item = new LabelEntity(_tmpLabelId,_tmpName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

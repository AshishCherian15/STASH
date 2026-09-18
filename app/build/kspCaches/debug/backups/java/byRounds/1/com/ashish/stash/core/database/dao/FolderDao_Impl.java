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
import com.ashish.stash.core.database.entity.FolderEntity;
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
public final class FolderDao_Impl implements FolderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FolderEntity> __insertionAdapterOfFolderEntity;

  private final EntityDeletionOrUpdateAdapter<FolderEntity> __deletionAdapterOfFolderEntity;

  private final EntityDeletionOrUpdateAdapter<FolderEntity> __updateAdapterOfFolderEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLockStatus;

  public FolderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFolderEntity = new EntityInsertionAdapter<FolderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `folders` (`folder_id`,`name`,`parent_folder_id`,`is_locked`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FolderEntity entity) {
        statement.bindLong(1, entity.getFolderId());
        statement.bindString(2, entity.getName());
        if (entity.getParentFolderId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getParentFolderId());
        }
        statement.bindLong(4, entity.isLocked());
      }
    };
    this.__deletionAdapterOfFolderEntity = new EntityDeletionOrUpdateAdapter<FolderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `folders` WHERE `folder_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FolderEntity entity) {
        statement.bindLong(1, entity.getFolderId());
      }
    };
    this.__updateAdapterOfFolderEntity = new EntityDeletionOrUpdateAdapter<FolderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `folders` SET `folder_id` = ?,`name` = ?,`parent_folder_id` = ?,`is_locked` = ? WHERE `folder_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FolderEntity entity) {
        statement.bindLong(1, entity.getFolderId());
        statement.bindString(2, entity.getName());
        if (entity.getParentFolderId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getParentFolderId());
        }
        statement.bindLong(4, entity.isLocked());
        statement.bindLong(5, entity.getFolderId());
      }
    };
    this.__preparedStmtOfUpdateLockStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE folders SET is_locked = ? WHERE folder_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FolderEntity folder, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFolderEntity.insertAndReturnId(folder);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final FolderEntity folder, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFolderEntity.handle(folder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final FolderEntity folder, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFolderEntity.handle(folder);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLockStatus(final long id, final int isLocked,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLockStatus.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, isLocked);
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
          __preparedStmtOfUpdateLockStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super FolderEntity> $completion) {
    final String _sql = "SELECT * FROM folders WHERE folder_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FolderEntity>() {
      @Override
      @Nullable
      public FolderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_folder_id");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final FolderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpFolderId;
            _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentFolderId;
            if (_cursor.isNull(_cursorIndexOfParentFolderId)) {
              _tmpParentFolderId = null;
            } else {
              _tmpParentFolderId = _cursor.getLong(_cursorIndexOfParentFolderId);
            }
            final int _tmpIsLocked;
            _tmpIsLocked = _cursor.getInt(_cursorIndexOfIsLocked);
            _result = new FolderEntity(_tmpFolderId,_tmpName,_tmpParentFolderId,_tmpIsLocked);
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
  public Flow<List<FolderEntity>> observeAll(final int showLocked) {
    final String _sql = "SELECT * FROM folders WHERE (? = 1 OR is_locked = 0) ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, showLocked);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"folders"}, new Callable<List<FolderEntity>>() {
      @Override
      @NonNull
      public List<FolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_folder_id");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final List<FolderEntity> _result = new ArrayList<FolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FolderEntity _item;
            final long _tmpFolderId;
            _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentFolderId;
            if (_cursor.isNull(_cursorIndexOfParentFolderId)) {
              _tmpParentFolderId = null;
            } else {
              _tmpParentFolderId = _cursor.getLong(_cursorIndexOfParentFolderId);
            }
            final int _tmpIsLocked;
            _tmpIsLocked = _cursor.getInt(_cursorIndexOfIsLocked);
            _item = new FolderEntity(_tmpFolderId,_tmpName,_tmpParentFolderId,_tmpIsLocked);
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
  public Flow<List<FolderEntity>> observeSubfolders(final Long parentFolderId,
      final int showLocked) {
    final String _sql = "\n"
            + "        SELECT * FROM folders \n"
            + "        WHERE (parent_folder_id = ? OR (? IS NULL AND parent_folder_id IS NULL)) \n"
            + "          AND (? = 1 OR is_locked = 0) \n"
            + "        ORDER BY name ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (parentFolderId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, parentFolderId);
    }
    _argIndex = 2;
    if (parentFolderId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, parentFolderId);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, showLocked);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"folders"}, new Callable<List<FolderEntity>>() {
      @Override
      @NonNull
      public List<FolderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfParentFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "parent_folder_id");
          final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
          final List<FolderEntity> _result = new ArrayList<FolderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FolderEntity _item;
            final long _tmpFolderId;
            _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final Long _tmpParentFolderId;
            if (_cursor.isNull(_cursorIndexOfParentFolderId)) {
              _tmpParentFolderId = null;
            } else {
              _tmpParentFolderId = _cursor.getLong(_cursorIndexOfParentFolderId);
            }
            final int _tmpIsLocked;
            _tmpIsLocked = _cursor.getInt(_cursorIndexOfIsLocked);
            _item = new FolderEntity(_tmpFolderId,_tmpName,_tmpParentFolderId,_tmpIsLocked);
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

package com.ashish.stash.core.database.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.ashish.stash.core.database.entity.ResourceLinkEntity;
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
public final class ResourceLinkDao_Impl implements ResourceLinkDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ResourceLinkEntity> __insertionAdapterOfResourceLinkEntity;

  private final EntityDeletionOrUpdateAdapter<ResourceLinkEntity> __deletionAdapterOfResourceLinkEntity;

  private final EntityDeletionOrUpdateAdapter<ResourceLinkEntity> __updateAdapterOfResourceLinkEntity;

  public ResourceLinkDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfResourceLinkEntity = new EntityInsertionAdapter<ResourceLinkEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `resource_links` (`link_id`,`document_id`,`url_or_note`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ResourceLinkEntity entity) {
        statement.bindLong(1, entity.getLinkId());
        statement.bindLong(2, entity.getDocumentId());
        statement.bindString(3, entity.getUrlOrNote());
      }
    };
    this.__deletionAdapterOfResourceLinkEntity = new EntityDeletionOrUpdateAdapter<ResourceLinkEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `resource_links` WHERE `link_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ResourceLinkEntity entity) {
        statement.bindLong(1, entity.getLinkId());
      }
    };
    this.__updateAdapterOfResourceLinkEntity = new EntityDeletionOrUpdateAdapter<ResourceLinkEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `resource_links` SET `link_id` = ?,`document_id` = ?,`url_or_note` = ? WHERE `link_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ResourceLinkEntity entity) {
        statement.bindLong(1, entity.getLinkId());
        statement.bindLong(2, entity.getDocumentId());
        statement.bindString(3, entity.getUrlOrNote());
        statement.bindLong(4, entity.getLinkId());
      }
    };
  }

  @Override
  public Object insert(final ResourceLinkEntity resourceLink,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfResourceLinkEntity.insertAndReturnId(resourceLink);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ResourceLinkEntity resourceLink,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfResourceLinkEntity.handle(resourceLink);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ResourceLinkEntity resourceLink,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfResourceLinkEntity.handle(resourceLink);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLinksForDocument(final long documentId,
      final Continuation<? super List<ResourceLinkEntity>> $completion) {
    final String _sql = "SELECT * FROM resource_links WHERE document_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, documentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ResourceLinkEntity>>() {
      @Override
      @NonNull
      public List<ResourceLinkEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLinkId = CursorUtil.getColumnIndexOrThrow(_cursor, "link_id");
          final int _cursorIndexOfDocumentId = CursorUtil.getColumnIndexOrThrow(_cursor, "document_id");
          final int _cursorIndexOfUrlOrNote = CursorUtil.getColumnIndexOrThrow(_cursor, "url_or_note");
          final List<ResourceLinkEntity> _result = new ArrayList<ResourceLinkEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ResourceLinkEntity _item;
            final long _tmpLinkId;
            _tmpLinkId = _cursor.getLong(_cursorIndexOfLinkId);
            final long _tmpDocumentId;
            _tmpDocumentId = _cursor.getLong(_cursorIndexOfDocumentId);
            final String _tmpUrlOrNote;
            _tmpUrlOrNote = _cursor.getString(_cursorIndexOfUrlOrNote);
            _item = new ResourceLinkEntity(_tmpLinkId,_tmpDocumentId,_tmpUrlOrNote);
            _result.add(_item);
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
  public Flow<List<ResourceLinkEntity>> observeLinksForDocument(final long documentId) {
    final String _sql = "SELECT * FROM resource_links WHERE document_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, documentId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"resource_links"}, new Callable<List<ResourceLinkEntity>>() {
      @Override
      @NonNull
      public List<ResourceLinkEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLinkId = CursorUtil.getColumnIndexOrThrow(_cursor, "link_id");
          final int _cursorIndexOfDocumentId = CursorUtil.getColumnIndexOrThrow(_cursor, "document_id");
          final int _cursorIndexOfUrlOrNote = CursorUtil.getColumnIndexOrThrow(_cursor, "url_or_note");
          final List<ResourceLinkEntity> _result = new ArrayList<ResourceLinkEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ResourceLinkEntity _item;
            final long _tmpLinkId;
            _tmpLinkId = _cursor.getLong(_cursorIndexOfLinkId);
            final long _tmpDocumentId;
            _tmpDocumentId = _cursor.getLong(_cursorIndexOfDocumentId);
            final String _tmpUrlOrNote;
            _tmpUrlOrNote = _cursor.getString(_cursorIndexOfUrlOrNote);
            _item = new ResourceLinkEntity(_tmpLinkId,_tmpDocumentId,_tmpUrlOrNote);
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

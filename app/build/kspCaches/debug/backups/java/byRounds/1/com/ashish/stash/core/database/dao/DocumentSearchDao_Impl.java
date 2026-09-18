package com.ashish.stash.core.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import com.ashish.stash.core.database.entity.CategoryEntity;
import com.ashish.stash.core.database.entity.DocumentEntity;
import com.ashish.stash.core.database.entity.DocumentWithMetadata;
import com.ashish.stash.core.database.entity.FolderEntity;
import com.ashish.stash.core.database.entity.LabelEntity;
import com.ashish.stash.core.database.entity.ResourceLinkEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DocumentSearchDao_Impl implements DocumentSearchDao {
  private final RoomDatabase __db;

  public DocumentSearchDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public Flow<List<DocumentWithMetadata>> searchDocuments(final String searchQuery,
      final int showLocked) {
    final String _sql = "\n"
            + "        SELECT d.* FROM documents d\n"
            + "        JOIN documents_fts fts ON d.document_id = fts.rowid\n"
            + "        LEFT JOIN categories c ON d.category_id = c.category_id\n"
            + "        LEFT JOIN folders f ON d.folder_id = f.folder_id\n"
            + "        WHERE documents_fts MATCH ? AND (? = 1 OR d.is_locked = 0)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, searchQuery);
    _argIndex = 2;
    _statement.bindLong(_argIndex, showLocked);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categories", "folders",
        "document_labels", "labels", "resource_links", "documents",
        "documents_fts"}, new Callable<List<DocumentWithMetadata>>() {
      @Override
      @NonNull
      public List<DocumentWithMetadata> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfDocumentId = CursorUtil.getColumnIndexOrThrow(_cursor, "document_id");
            final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
            final int _cursorIndexOfDisplayTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "display_title");
            final int _cursorIndexOfOriginalFilename = CursorUtil.getColumnIndexOrThrow(_cursor, "original_filename");
            final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mime_type");
            final int _cursorIndexOfFileHash = CursorUtil.getColumnIndexOrThrow(_cursor, "file_hash");
            final int _cursorIndexOfFileSize = CursorUtil.getColumnIndexOrThrow(_cursor, "file_size");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "category_id");
            final int _cursorIndexOfFolderId = CursorUtil.getColumnIndexOrThrow(_cursor, "folder_id");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfOcrText = CursorUtil.getColumnIndexOrThrow(_cursor, "ocr_text");
            final int _cursorIndexOfColorTag = CursorUtil.getColumnIndexOrThrow(_cursor, "color_tag");
            final int _cursorIndexOfImportance = CursorUtil.getColumnIndexOrThrow(_cursor, "importance");
            final int _cursorIndexOfIsLocked = CursorUtil.getColumnIndexOrThrow(_cursor, "is_locked");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final int _cursorIndexOfLastOpenedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "last_opened_at");
            final int _cursorIndexOfImportSource = CursorUtil.getColumnIndexOrThrow(_cursor, "import_source");
            final int _cursorIndexOfCategoryName = CursorUtil.getColumnIndexOrThrow(_cursor, "category_name");
            final int _cursorIndexOfLabelNames = CursorUtil.getColumnIndexOrThrow(_cursor, "label_names");
            final LongSparseArray<CategoryEntity> _collectionCategory = new LongSparseArray<CategoryEntity>();
            final LongSparseArray<FolderEntity> _collectionFolder = new LongSparseArray<FolderEntity>();
            final LongSparseArray<ArrayList<LabelEntity>> _collectionLabels = new LongSparseArray<ArrayList<LabelEntity>>();
            final LongSparseArray<ArrayList<ResourceLinkEntity>> _collectionResourceLinks = new LongSparseArray<ArrayList<ResourceLinkEntity>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey != null) {
                _collectionCategory.put(_tmpKey, null);
              }
              final Long _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfFolderId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getLong(_cursorIndexOfFolderId);
              }
              if (_tmpKey_1 != null) {
                _collectionFolder.put(_tmpKey_1, null);
              }
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfDocumentId);
              if (!_collectionLabels.containsKey(_tmpKey_2)) {
                _collectionLabels.put(_tmpKey_2, new ArrayList<LabelEntity>());
              }
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfDocumentId);
              if (!_collectionResourceLinks.containsKey(_tmpKey_3)) {
                _collectionResourceLinks.put(_tmpKey_3, new ArrayList<ResourceLinkEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriesAscomAshishStashCoreDatabaseEntityCategoryEntity(_collectionCategory);
            __fetchRelationshipfoldersAscomAshishStashCoreDatabaseEntityFolderEntity(_collectionFolder);
            __fetchRelationshiplabelsAscomAshishStashCoreDatabaseEntityLabelEntity(_collectionLabels);
            __fetchRelationshipresourceLinksAscomAshishStashCoreDatabaseEntityResourceLinkEntity(_collectionResourceLinks);
            final List<DocumentWithMetadata> _result = new ArrayList<DocumentWithMetadata>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final DocumentWithMetadata _item;
              final DocumentEntity _tmpDocument;
              final long _tmpDocumentId;
              _tmpDocumentId = _cursor.getLong(_cursorIndexOfDocumentId);
              final String _tmpUri;
              _tmpUri = _cursor.getString(_cursorIndexOfUri);
              final String _tmpDisplayTitle;
              _tmpDisplayTitle = _cursor.getString(_cursorIndexOfDisplayTitle);
              final String _tmpOriginalFilename;
              _tmpOriginalFilename = _cursor.getString(_cursorIndexOfOriginalFilename);
              final String _tmpMimeType;
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
              final String _tmpFileHash;
              _tmpFileHash = _cursor.getString(_cursorIndexOfFileHash);
              final long _tmpFileSize;
              _tmpFileSize = _cursor.getLong(_cursorIndexOfFileSize);
              final Long _tmpCategoryId;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpCategoryId = null;
              } else {
                _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              final Long _tmpFolderId;
              if (_cursor.isNull(_cursorIndexOfFolderId)) {
                _tmpFolderId = null;
              } else {
                _tmpFolderId = _cursor.getLong(_cursorIndexOfFolderId);
              }
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              final String _tmpOcrText;
              if (_cursor.isNull(_cursorIndexOfOcrText)) {
                _tmpOcrText = null;
              } else {
                _tmpOcrText = _cursor.getString(_cursorIndexOfOcrText);
              }
              final String _tmpColorTag;
              if (_cursor.isNull(_cursorIndexOfColorTag)) {
                _tmpColorTag = null;
              } else {
                _tmpColorTag = _cursor.getString(_cursorIndexOfColorTag);
              }
              final String _tmpImportance;
              _tmpImportance = _cursor.getString(_cursorIndexOfImportance);
              final int _tmpIsLocked;
              _tmpIsLocked = _cursor.getInt(_cursorIndexOfIsLocked);
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final long _tmpLastOpenedAt;
              _tmpLastOpenedAt = _cursor.getLong(_cursorIndexOfLastOpenedAt);
              final String _tmpImportSource;
              _tmpImportSource = _cursor.getString(_cursorIndexOfImportSource);
              final String _tmpCategoryName;
              if (_cursor.isNull(_cursorIndexOfCategoryName)) {
                _tmpCategoryName = null;
              } else {
                _tmpCategoryName = _cursor.getString(_cursorIndexOfCategoryName);
              }
              final String _tmpLabelNames;
              if (_cursor.isNull(_cursorIndexOfLabelNames)) {
                _tmpLabelNames = null;
              } else {
                _tmpLabelNames = _cursor.getString(_cursorIndexOfLabelNames);
              }
              _tmpDocument = new DocumentEntity(_tmpDocumentId,_tmpUri,_tmpDisplayTitle,_tmpOriginalFilename,_tmpMimeType,_tmpFileHash,_tmpFileSize,_tmpCategoryId,_tmpFolderId,_tmpNotes,_tmpOcrText,_tmpColorTag,_tmpImportance,_tmpIsLocked,_tmpCreatedAt,_tmpLastOpenedAt,_tmpImportSource,_tmpCategoryName,_tmpLabelNames);
              final CategoryEntity _tmpCategory;
              final Long _tmpKey_4;
              if (_cursor.isNull(_cursorIndexOfCategoryId)) {
                _tmpKey_4 = null;
              } else {
                _tmpKey_4 = _cursor.getLong(_cursorIndexOfCategoryId);
              }
              if (_tmpKey_4 != null) {
                _tmpCategory = _collectionCategory.get(_tmpKey_4);
              } else {
                _tmpCategory = null;
              }
              final FolderEntity _tmpFolder;
              final Long _tmpKey_5;
              if (_cursor.isNull(_cursorIndexOfFolderId)) {
                _tmpKey_5 = null;
              } else {
                _tmpKey_5 = _cursor.getLong(_cursorIndexOfFolderId);
              }
              if (_tmpKey_5 != null) {
                _tmpFolder = _collectionFolder.get(_tmpKey_5);
              } else {
                _tmpFolder = null;
              }
              final ArrayList<LabelEntity> _tmpLabelsCollection;
              final long _tmpKey_6;
              _tmpKey_6 = _cursor.getLong(_cursorIndexOfDocumentId);
              _tmpLabelsCollection = _collectionLabels.get(_tmpKey_6);
              final ArrayList<ResourceLinkEntity> _tmpResourceLinksCollection;
              final long _tmpKey_7;
              _tmpKey_7 = _cursor.getLong(_cursorIndexOfDocumentId);
              _tmpResourceLinksCollection = _collectionResourceLinks.get(_tmpKey_7);
              _item = new DocumentWithMetadata(_tmpDocument,_tmpCategory,_tmpFolder,_tmpLabelsCollection,_tmpResourceLinksCollection);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcategoriesAscomAshishStashCoreDatabaseEntityCategoryEntity(
      @NonNull final LongSparseArray<CategoryEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipcategoriesAscomAshishStashCoreDatabaseEntityCategoryEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `category_id`,`name`,`color` FROM `categories` WHERE `category_id` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "category_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfCategoryId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfColor = 2;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final CategoryEntity _item_1;
          final long _tmpCategoryId;
          _tmpCategoryId = _cursor.getLong(_cursorIndexOfCategoryId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          final String _tmpColor;
          _tmpColor = _cursor.getString(_cursorIndexOfColor);
          _item_1 = new CategoryEntity(_tmpCategoryId,_tmpName,_tmpColor);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipfoldersAscomAshishStashCoreDatabaseEntityFolderEntity(
      @NonNull final LongSparseArray<FolderEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipfoldersAscomAshishStashCoreDatabaseEntityFolderEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `folder_id`,`name`,`parent_folder_id`,`is_locked` FROM `folders` WHERE `folder_id` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "folder_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfFolderId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfParentFolderId = 2;
      final int _cursorIndexOfIsLocked = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final FolderEntity _item_1;
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
          _item_1 = new FolderEntity(_tmpFolderId,_tmpName,_tmpParentFolderId,_tmpIsLocked);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshiplabelsAscomAshishStashCoreDatabaseEntityLabelEntity(
      @NonNull final LongSparseArray<ArrayList<LabelEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshiplabelsAscomAshishStashCoreDatabaseEntityLabelEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `labels`.`label_id` AS `label_id`,`labels`.`name` AS `name`,_junction.`document_id` FROM `document_labels` AS _junction INNER JOIN `labels` ON (_junction.`label_id` = `labels`.`label_id`) WHERE _junction.`document_id` IN (");
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
      // _junction.document_id;
      final int _itemKeyIndex = 2;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfLabelId = 0;
      final int _cursorIndexOfName = 1;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<LabelEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final LabelEntity _item_1;
          final long _tmpLabelId;
          _tmpLabelId = _cursor.getLong(_cursorIndexOfLabelId);
          final String _tmpName;
          _tmpName = _cursor.getString(_cursorIndexOfName);
          _item_1 = new LabelEntity(_tmpLabelId,_tmpName);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipresourceLinksAscomAshishStashCoreDatabaseEntityResourceLinkEntity(
      @NonNull final LongSparseArray<ArrayList<ResourceLinkEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipresourceLinksAscomAshishStashCoreDatabaseEntityResourceLinkEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `link_id`,`document_id`,`url_or_note` FROM `resource_links` WHERE `document_id` IN (");
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
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "document_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfLinkId = 0;
      final int _cursorIndexOfDocumentId = 1;
      final int _cursorIndexOfUrlOrNote = 2;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<ResourceLinkEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final ResourceLinkEntity _item_1;
          final long _tmpLinkId;
          _tmpLinkId = _cursor.getLong(_cursorIndexOfLinkId);
          final long _tmpDocumentId;
          _tmpDocumentId = _cursor.getLong(_cursorIndexOfDocumentId);
          final String _tmpUrlOrNote;
          _tmpUrlOrNote = _cursor.getString(_cursorIndexOfUrlOrNote);
          _item_1 = new ResourceLinkEntity(_tmpLinkId,_tmpDocumentId,_tmpUrlOrNote);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}

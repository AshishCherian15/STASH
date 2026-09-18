package com.ashish.stash.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.FtsTableInfo;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.ashish.stash.core.database.dao.CategoryDao;
import com.ashish.stash.core.database.dao.CategoryDao_Impl;
import com.ashish.stash.core.database.dao.DocumentDao;
import com.ashish.stash.core.database.dao.DocumentDao_Impl;
import com.ashish.stash.core.database.dao.DocumentSearchDao;
import com.ashish.stash.core.database.dao.DocumentSearchDao_Impl;
import com.ashish.stash.core.database.dao.FolderDao;
import com.ashish.stash.core.database.dao.FolderDao_Impl;
import com.ashish.stash.core.database.dao.LabelDao;
import com.ashish.stash.core.database.dao.LabelDao_Impl;
import com.ashish.stash.core.database.dao.ResourceLinkDao;
import com.ashish.stash.core.database.dao.ResourceLinkDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StashDatabase_Impl extends StashDatabase {
  private volatile DocumentDao _documentDao;

  private volatile CategoryDao _categoryDao;

  private volatile FolderDao _folderDao;

  private volatile LabelDao _labelDao;

  private volatile DocumentSearchDao _documentSearchDao;

  private volatile ResourceLinkDao _resourceLinkDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `documents` (`document_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `uri` TEXT NOT NULL, `display_title` TEXT NOT NULL, `original_filename` TEXT NOT NULL, `mime_type` TEXT NOT NULL, `file_hash` TEXT NOT NULL, `file_size` INTEGER NOT NULL, `category_id` INTEGER, `folder_id` INTEGER, `notes` TEXT, `ocr_text` TEXT, `color_tag` TEXT, `importance` TEXT NOT NULL, `is_locked` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `last_opened_at` INTEGER NOT NULL, `import_source` TEXT NOT NULL, `category_name` TEXT, `label_names` TEXT, FOREIGN KEY(`category_id`) REFERENCES `categories`(`category_id`) ON UPDATE NO ACTION ON DELETE SET NULL , FOREIGN KEY(`folder_id`) REFERENCES `folders`(`folder_id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_documents_category_id` ON `documents` (`category_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_documents_folder_id` ON `documents` (`folder_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_documents_is_locked` ON `documents` (`is_locked`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_documents_last_opened_at` ON `documents` (`last_opened_at`)");
        db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `documents_fts` USING FTS4(`display_title` TEXT NOT NULL, `original_filename` TEXT NOT NULL, `notes` TEXT, `ocr_text` TEXT, `category_name` TEXT, `label_names` TEXT, content=`documents`)");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_documents_fts_BEFORE_UPDATE BEFORE UPDATE ON `documents` BEGIN DELETE FROM `documents_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_documents_fts_BEFORE_DELETE BEFORE DELETE ON `documents` BEGIN DELETE FROM `documents_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_documents_fts_AFTER_UPDATE AFTER UPDATE ON `documents` BEGIN INSERT INTO `documents_fts`(`docid`, `display_title`, `original_filename`, `notes`, `ocr_text`, `category_name`, `label_names`) VALUES (NEW.`rowid`, NEW.`display_title`, NEW.`original_filename`, NEW.`notes`, NEW.`ocr_text`, NEW.`category_name`, NEW.`label_names`); END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_documents_fts_AFTER_INSERT AFTER INSERT ON `documents` BEGIN INSERT INTO `documents_fts`(`docid`, `display_title`, `original_filename`, `notes`, `ocr_text`, `category_name`, `label_names`) VALUES (NEW.`rowid`, NEW.`display_title`, NEW.`original_filename`, NEW.`notes`, NEW.`ocr_text`, NEW.`category_name`, NEW.`label_names`); END");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`category_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `color` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_categories_name` ON `categories` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `folders` (`folder_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `parent_folder_id` INTEGER, `is_locked` INTEGER NOT NULL, FOREIGN KEY(`parent_folder_id`) REFERENCES `folders`(`folder_id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_folders_parent_folder_id` ON `folders` (`parent_folder_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `labels` (`label_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_labels_name` ON `labels` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `document_labels` (`document_id` INTEGER NOT NULL, `label_id` INTEGER NOT NULL, PRIMARY KEY(`document_id`, `label_id`), FOREIGN KEY(`document_id`) REFERENCES `documents`(`document_id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`label_id`) REFERENCES `labels`(`label_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_document_labels_document_id` ON `document_labels` (`document_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_document_labels_label_id` ON `document_labels` (`label_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `resource_links` (`link_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `document_id` INTEGER NOT NULL, `url_or_note` TEXT NOT NULL, FOREIGN KEY(`document_id`) REFERENCES `documents`(`document_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_resource_links_document_id` ON `resource_links` (`document_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'f1ecff674de20216689b3b6febc9bd1b')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `documents`");
        db.execSQL("DROP TABLE IF EXISTS `documents_fts`");
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `folders`");
        db.execSQL("DROP TABLE IF EXISTS `labels`");
        db.execSQL("DROP TABLE IF EXISTS `document_labels`");
        db.execSQL("DROP TABLE IF EXISTS `resource_links`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_documents_fts_BEFORE_UPDATE BEFORE UPDATE ON `documents` BEGIN DELETE FROM `documents_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_documents_fts_BEFORE_DELETE BEFORE DELETE ON `documents` BEGIN DELETE FROM `documents_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_documents_fts_AFTER_UPDATE AFTER UPDATE ON `documents` BEGIN INSERT INTO `documents_fts`(`docid`, `display_title`, `original_filename`, `notes`, `ocr_text`, `category_name`, `label_names`) VALUES (NEW.`rowid`, NEW.`display_title`, NEW.`original_filename`, NEW.`notes`, NEW.`ocr_text`, NEW.`category_name`, NEW.`label_names`); END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_documents_fts_AFTER_INSERT AFTER INSERT ON `documents` BEGIN INSERT INTO `documents_fts`(`docid`, `display_title`, `original_filename`, `notes`, `ocr_text`, `category_name`, `label_names`) VALUES (NEW.`rowid`, NEW.`display_title`, NEW.`original_filename`, NEW.`notes`, NEW.`ocr_text`, NEW.`category_name`, NEW.`label_names`); END");
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDocuments = new HashMap<String, TableInfo.Column>(19);
        _columnsDocuments.put("document_id", new TableInfo.Column("document_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("display_title", new TableInfo.Column("display_title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("original_filename", new TableInfo.Column("original_filename", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("mime_type", new TableInfo.Column("mime_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("file_hash", new TableInfo.Column("file_hash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("file_size", new TableInfo.Column("file_size", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("category_id", new TableInfo.Column("category_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("folder_id", new TableInfo.Column("folder_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("ocr_text", new TableInfo.Column("ocr_text", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("color_tag", new TableInfo.Column("color_tag", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("importance", new TableInfo.Column("importance", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("is_locked", new TableInfo.Column("is_locked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("last_opened_at", new TableInfo.Column("last_opened_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("import_source", new TableInfo.Column("import_source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("category_name", new TableInfo.Column("category_name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocuments.put("label_names", new TableInfo.Column("label_names", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDocuments = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDocuments.add(new TableInfo.ForeignKey("categories", "SET NULL", "NO ACTION", Arrays.asList("category_id"), Arrays.asList("category_id")));
        _foreignKeysDocuments.add(new TableInfo.ForeignKey("folders", "SET NULL", "NO ACTION", Arrays.asList("folder_id"), Arrays.asList("folder_id")));
        final HashSet<TableInfo.Index> _indicesDocuments = new HashSet<TableInfo.Index>(4);
        _indicesDocuments.add(new TableInfo.Index("index_documents_category_id", false, Arrays.asList("category_id"), Arrays.asList("ASC")));
        _indicesDocuments.add(new TableInfo.Index("index_documents_folder_id", false, Arrays.asList("folder_id"), Arrays.asList("ASC")));
        _indicesDocuments.add(new TableInfo.Index("index_documents_is_locked", false, Arrays.asList("is_locked"), Arrays.asList("ASC")));
        _indicesDocuments.add(new TableInfo.Index("index_documents_last_opened_at", false, Arrays.asList("last_opened_at"), Arrays.asList("ASC")));
        final TableInfo _infoDocuments = new TableInfo("documents", _columnsDocuments, _foreignKeysDocuments, _indicesDocuments);
        final TableInfo _existingDocuments = TableInfo.read(db, "documents");
        if (!_infoDocuments.equals(_existingDocuments)) {
          return new RoomOpenHelper.ValidationResult(false, "documents(com.ashish.stash.core.database.entity.DocumentEntity).\n"
                  + " Expected:\n" + _infoDocuments + "\n"
                  + " Found:\n" + _existingDocuments);
        }
        final HashSet<String> _columnsDocumentsFts = new HashSet<String>(6);
        _columnsDocumentsFts.add("display_title");
        _columnsDocumentsFts.add("original_filename");
        _columnsDocumentsFts.add("notes");
        _columnsDocumentsFts.add("ocr_text");
        _columnsDocumentsFts.add("category_name");
        _columnsDocumentsFts.add("label_names");
        final FtsTableInfo _infoDocumentsFts = new FtsTableInfo("documents_fts", _columnsDocumentsFts, "CREATE VIRTUAL TABLE IF NOT EXISTS `documents_fts` USING FTS4(`display_title` TEXT NOT NULL, `original_filename` TEXT NOT NULL, `notes` TEXT, `ocr_text` TEXT, `category_name` TEXT, `label_names` TEXT, content=`documents`)");
        final FtsTableInfo _existingDocumentsFts = FtsTableInfo.read(db, "documents_fts");
        if (!_infoDocumentsFts.equals(_existingDocumentsFts)) {
          return new RoomOpenHelper.ValidationResult(false, "documents_fts(com.ashish.stash.core.database.entity.DocumentFtsEntity).\n"
                  + " Expected:\n" + _infoDocumentsFts + "\n"
                  + " Found:\n" + _existingDocumentsFts);
        }
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(3);
        _columnsCategories.put("category_id", new TableInfo.Column("category_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("color", new TableInfo.Column("color", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(1);
        _indicesCategories.add(new TableInfo.Index("index_categories_name", true, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.ashish.stash.core.database.entity.CategoryEntity).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsFolders = new HashMap<String, TableInfo.Column>(4);
        _columnsFolders.put("folder_id", new TableInfo.Column("folder_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolders.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolders.put("parent_folder_id", new TableInfo.Column("parent_folder_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFolders.put("is_locked", new TableInfo.Column("is_locked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFolders = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysFolders.add(new TableInfo.ForeignKey("folders", "SET NULL", "NO ACTION", Arrays.asList("parent_folder_id"), Arrays.asList("folder_id")));
        final HashSet<TableInfo.Index> _indicesFolders = new HashSet<TableInfo.Index>(1);
        _indicesFolders.add(new TableInfo.Index("index_folders_parent_folder_id", false, Arrays.asList("parent_folder_id"), Arrays.asList("ASC")));
        final TableInfo _infoFolders = new TableInfo("folders", _columnsFolders, _foreignKeysFolders, _indicesFolders);
        final TableInfo _existingFolders = TableInfo.read(db, "folders");
        if (!_infoFolders.equals(_existingFolders)) {
          return new RoomOpenHelper.ValidationResult(false, "folders(com.ashish.stash.core.database.entity.FolderEntity).\n"
                  + " Expected:\n" + _infoFolders + "\n"
                  + " Found:\n" + _existingFolders);
        }
        final HashMap<String, TableInfo.Column> _columnsLabels = new HashMap<String, TableInfo.Column>(2);
        _columnsLabels.put("label_id", new TableInfo.Column("label_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLabels.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLabels = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLabels = new HashSet<TableInfo.Index>(1);
        _indicesLabels.add(new TableInfo.Index("index_labels_name", true, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoLabels = new TableInfo("labels", _columnsLabels, _foreignKeysLabels, _indicesLabels);
        final TableInfo _existingLabels = TableInfo.read(db, "labels");
        if (!_infoLabels.equals(_existingLabels)) {
          return new RoomOpenHelper.ValidationResult(false, "labels(com.ashish.stash.core.database.entity.LabelEntity).\n"
                  + " Expected:\n" + _infoLabels + "\n"
                  + " Found:\n" + _existingLabels);
        }
        final HashMap<String, TableInfo.Column> _columnsDocumentLabels = new HashMap<String, TableInfo.Column>(2);
        _columnsDocumentLabels.put("document_id", new TableInfo.Column("document_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDocumentLabels.put("label_id", new TableInfo.Column("label_id", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDocumentLabels = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysDocumentLabels.add(new TableInfo.ForeignKey("documents", "CASCADE", "NO ACTION", Arrays.asList("document_id"), Arrays.asList("document_id")));
        _foreignKeysDocumentLabels.add(new TableInfo.ForeignKey("labels", "CASCADE", "NO ACTION", Arrays.asList("label_id"), Arrays.asList("label_id")));
        final HashSet<TableInfo.Index> _indicesDocumentLabels = new HashSet<TableInfo.Index>(2);
        _indicesDocumentLabels.add(new TableInfo.Index("index_document_labels_document_id", false, Arrays.asList("document_id"), Arrays.asList("ASC")));
        _indicesDocumentLabels.add(new TableInfo.Index("index_document_labels_label_id", false, Arrays.asList("label_id"), Arrays.asList("ASC")));
        final TableInfo _infoDocumentLabels = new TableInfo("document_labels", _columnsDocumentLabels, _foreignKeysDocumentLabels, _indicesDocumentLabels);
        final TableInfo _existingDocumentLabels = TableInfo.read(db, "document_labels");
        if (!_infoDocumentLabels.equals(_existingDocumentLabels)) {
          return new RoomOpenHelper.ValidationResult(false, "document_labels(com.ashish.stash.core.database.entity.DocumentLabelEntity).\n"
                  + " Expected:\n" + _infoDocumentLabels + "\n"
                  + " Found:\n" + _existingDocumentLabels);
        }
        final HashMap<String, TableInfo.Column> _columnsResourceLinks = new HashMap<String, TableInfo.Column>(3);
        _columnsResourceLinks.put("link_id", new TableInfo.Column("link_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResourceLinks.put("document_id", new TableInfo.Column("document_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResourceLinks.put("url_or_note", new TableInfo.Column("url_or_note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysResourceLinks = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysResourceLinks.add(new TableInfo.ForeignKey("documents", "CASCADE", "NO ACTION", Arrays.asList("document_id"), Arrays.asList("document_id")));
        final HashSet<TableInfo.Index> _indicesResourceLinks = new HashSet<TableInfo.Index>(1);
        _indicesResourceLinks.add(new TableInfo.Index("index_resource_links_document_id", false, Arrays.asList("document_id"), Arrays.asList("ASC")));
        final TableInfo _infoResourceLinks = new TableInfo("resource_links", _columnsResourceLinks, _foreignKeysResourceLinks, _indicesResourceLinks);
        final TableInfo _existingResourceLinks = TableInfo.read(db, "resource_links");
        if (!_infoResourceLinks.equals(_existingResourceLinks)) {
          return new RoomOpenHelper.ValidationResult(false, "resource_links(com.ashish.stash.core.database.entity.ResourceLinkEntity).\n"
                  + " Expected:\n" + _infoResourceLinks + "\n"
                  + " Found:\n" + _existingResourceLinks);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "f1ecff674de20216689b3b6febc9bd1b", "5396f2883b4ea4486c425da2bcf08726");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(1);
    _shadowTablesMap.put("documents_fts", "documents");
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "documents","documents_fts","categories","folders","labels","document_labels","resource_links");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `documents`");
      _db.execSQL("DELETE FROM `documents_fts`");
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `folders`");
      _db.execSQL("DELETE FROM `labels`");
      _db.execSQL("DELETE FROM `document_labels`");
      _db.execSQL("DELETE FROM `resource_links`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DocumentDao.class, DocumentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoryDao.class, CategoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FolderDao.class, FolderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(LabelDao.class, LabelDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DocumentSearchDao.class, DocumentSearchDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ResourceLinkDao.class, ResourceLinkDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DocumentDao documentDao() {
    if (_documentDao != null) {
      return _documentDao;
    } else {
      synchronized(this) {
        if(_documentDao == null) {
          _documentDao = new DocumentDao_Impl(this);
        }
        return _documentDao;
      }
    }
  }

  @Override
  public CategoryDao categoryDao() {
    if (_categoryDao != null) {
      return _categoryDao;
    } else {
      synchronized(this) {
        if(_categoryDao == null) {
          _categoryDao = new CategoryDao_Impl(this);
        }
        return _categoryDao;
      }
    }
  }

  @Override
  public FolderDao folderDao() {
    if (_folderDao != null) {
      return _folderDao;
    } else {
      synchronized(this) {
        if(_folderDao == null) {
          _folderDao = new FolderDao_Impl(this);
        }
        return _folderDao;
      }
    }
  }

  @Override
  public LabelDao labelDao() {
    if (_labelDao != null) {
      return _labelDao;
    } else {
      synchronized(this) {
        if(_labelDao == null) {
          _labelDao = new LabelDao_Impl(this);
        }
        return _labelDao;
      }
    }
  }

  @Override
  public DocumentSearchDao documentSearchDao() {
    if (_documentSearchDao != null) {
      return _documentSearchDao;
    } else {
      synchronized(this) {
        if(_documentSearchDao == null) {
          _documentSearchDao = new DocumentSearchDao_Impl(this);
        }
        return _documentSearchDao;
      }
    }
  }

  @Override
  public ResourceLinkDao resourceLinkDao() {
    if (_resourceLinkDao != null) {
      return _resourceLinkDao;
    } else {
      synchronized(this) {
        if(_resourceLinkDao == null) {
          _resourceLinkDao = new ResourceLinkDao_Impl(this);
        }
        return _resourceLinkDao;
      }
    }
  }
}

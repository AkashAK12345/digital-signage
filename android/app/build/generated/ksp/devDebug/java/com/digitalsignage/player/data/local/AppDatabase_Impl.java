package com.digitalsignage.player.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
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
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PlaylistDao _playlistDao;

  private volatile DownloadSessionDao _downloadSessionDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `playlist` (`playlistId` TEXT NOT NULL, `version` INTEGER NOT NULL, `state` TEXT NOT NULL, `lastSyncedAt` INTEGER NOT NULL, PRIMARY KEY(`playlistId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `media_item` (`mediaId` TEXT NOT NULL, `playlistId` TEXT NOT NULL, `url` TEXT NOT NULL, `durationMs` INTEGER NOT NULL, `displayOrder` INTEGER NOT NULL, `md5Hash` TEXT, `sha256Hash` TEXT, `mediaType` TEXT NOT NULL, `isDownloaded` INTEGER NOT NULL, `localFilePath` TEXT, PRIMARY KEY(`mediaId`), FOREIGN KEY(`playlistId`) REFERENCES `playlist`(`playlistId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_media_item_playlistId` ON `media_item` (`playlistId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `download_session` (`mediaId` TEXT NOT NULL, `url` TEXT NOT NULL, `downloadState` TEXT NOT NULL, `currentByteOffset` INTEGER NOT NULL, `expectedSize` INTEGER NOT NULL, `expectedChecksumMd5` TEXT, `expectedChecksumSha256` TEXT, `retryCount` INTEGER NOT NULL, `priority` INTEGER NOT NULL, `destinationPath` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`mediaId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'cf5aa237707726d15ad3eadfaed2c501')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `playlist`");
        db.execSQL("DROP TABLE IF EXISTS `media_item`");
        db.execSQL("DROP TABLE IF EXISTS `download_session`");
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
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPlaylist = new HashMap<String, TableInfo.Column>(4);
        _columnsPlaylist.put("playlistId", new TableInfo.Column("playlistId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylist.put("version", new TableInfo.Column("version", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylist.put("state", new TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaylist.put("lastSyncedAt", new TableInfo.Column("lastSyncedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaylist = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaylist = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaylist = new TableInfo("playlist", _columnsPlaylist, _foreignKeysPlaylist, _indicesPlaylist);
        final TableInfo _existingPlaylist = TableInfo.read(db, "playlist");
        if (!_infoPlaylist.equals(_existingPlaylist)) {
          return new RoomOpenHelper.ValidationResult(false, "playlist(com.digitalsignage.player.data.local.PlaylistEntity).\n"
                  + " Expected:\n" + _infoPlaylist + "\n"
                  + " Found:\n" + _existingPlaylist);
        }
        final HashMap<String, TableInfo.Column> _columnsMediaItem = new HashMap<String, TableInfo.Column>(10);
        _columnsMediaItem.put("mediaId", new TableInfo.Column("mediaId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("playlistId", new TableInfo.Column("playlistId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("url", new TableInfo.Column("url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("durationMs", new TableInfo.Column("durationMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("displayOrder", new TableInfo.Column("displayOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("md5Hash", new TableInfo.Column("md5Hash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("sha256Hash", new TableInfo.Column("sha256Hash", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("mediaType", new TableInfo.Column("mediaType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("isDownloaded", new TableInfo.Column("isDownloaded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMediaItem.put("localFilePath", new TableInfo.Column("localFilePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMediaItem = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMediaItem.add(new TableInfo.ForeignKey("playlist", "CASCADE", "NO ACTION", Arrays.asList("playlistId"), Arrays.asList("playlistId")));
        final HashSet<TableInfo.Index> _indicesMediaItem = new HashSet<TableInfo.Index>(1);
        _indicesMediaItem.add(new TableInfo.Index("index_media_item_playlistId", false, Arrays.asList("playlistId"), Arrays.asList("ASC")));
        final TableInfo _infoMediaItem = new TableInfo("media_item", _columnsMediaItem, _foreignKeysMediaItem, _indicesMediaItem);
        final TableInfo _existingMediaItem = TableInfo.read(db, "media_item");
        if (!_infoMediaItem.equals(_existingMediaItem)) {
          return new RoomOpenHelper.ValidationResult(false, "media_item(com.digitalsignage.player.data.local.MediaItemEntity).\n"
                  + " Expected:\n" + _infoMediaItem + "\n"
                  + " Found:\n" + _existingMediaItem);
        }
        final HashMap<String, TableInfo.Column> _columnsDownloadSession = new HashMap<String, TableInfo.Column>(12);
        _columnsDownloadSession.put("mediaId", new TableInfo.Column("mediaId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("url", new TableInfo.Column("url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("downloadState", new TableInfo.Column("downloadState", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("currentByteOffset", new TableInfo.Column("currentByteOffset", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("expectedSize", new TableInfo.Column("expectedSize", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("expectedChecksumMd5", new TableInfo.Column("expectedChecksumMd5", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("expectedChecksumSha256", new TableInfo.Column("expectedChecksumSha256", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("retryCount", new TableInfo.Column("retryCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("priority", new TableInfo.Column("priority", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("destinationPath", new TableInfo.Column("destinationPath", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadSession.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDownloadSession = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDownloadSession = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDownloadSession = new TableInfo("download_session", _columnsDownloadSession, _foreignKeysDownloadSession, _indicesDownloadSession);
        final TableInfo _existingDownloadSession = TableInfo.read(db, "download_session");
        if (!_infoDownloadSession.equals(_existingDownloadSession)) {
          return new RoomOpenHelper.ValidationResult(false, "download_session(com.digitalsignage.player.data.local.DownloadSessionEntity).\n"
                  + " Expected:\n" + _infoDownloadSession + "\n"
                  + " Found:\n" + _existingDownloadSession);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "cf5aa237707726d15ad3eadfaed2c501", "ba7435155009e4848e083e8afc27c269");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "playlist","media_item","download_session");
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
      _db.execSQL("DELETE FROM `playlist`");
      _db.execSQL("DELETE FROM `media_item`");
      _db.execSQL("DELETE FROM `download_session`");
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
    _typeConvertersMap.put(PlaylistDao.class, PlaylistDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DownloadSessionDao.class, DownloadSessionDao_Impl.getRequiredConverters());
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
  public PlaylistDao playlistDao() {
    if (_playlistDao != null) {
      return _playlistDao;
    } else {
      synchronized(this) {
        if(_playlistDao == null) {
          _playlistDao = new PlaylistDao_Impl(this);
        }
        return _playlistDao;
      }
    }
  }

  @Override
  public DownloadSessionDao downloadSessionDao() {
    if (_downloadSessionDao != null) {
      return _downloadSessionDao;
    } else {
      synchronized(this) {
        if(_downloadSessionDao == null) {
          _downloadSessionDao = new DownloadSessionDao_Impl(this);
        }
        return _downloadSessionDao;
      }
    }
  }
}

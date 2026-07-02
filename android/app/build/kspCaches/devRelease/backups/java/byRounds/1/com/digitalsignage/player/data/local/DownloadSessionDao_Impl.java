package com.digitalsignage.player.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.digitalsignage.player.domain.model.DownloadState;
import java.lang.Class;
import java.lang.Exception;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DownloadSessionDao_Impl implements DownloadSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DownloadSessionEntity> __insertionAdapterOfDownloadSessionEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfUpdateSessionOffset;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSessionState;

  private final SharedSQLiteStatement __preparedStmtOfIncrementRetryCount;

  public DownloadSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDownloadSessionEntity = new EntityInsertionAdapter<DownloadSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `download_session` (`mediaId`,`url`,`downloadState`,`currentByteOffset`,`expectedSize`,`expectedChecksumMd5`,`expectedChecksumSha256`,`retryCount`,`priority`,`destinationPath`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DownloadSessionEntity entity) {
        statement.bindString(1, entity.getMediaId());
        statement.bindString(2, entity.getUrl());
        final String _tmp = __converters.fromDownloadState(entity.getDownloadState());
        statement.bindString(3, _tmp);
        statement.bindLong(4, entity.getCurrentByteOffset());
        statement.bindLong(5, entity.getExpectedSize());
        if (entity.getExpectedChecksumMd5() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getExpectedChecksumMd5());
        }
        if (entity.getExpectedChecksumSha256() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getExpectedChecksumSha256());
        }
        statement.bindLong(8, entity.getRetryCount());
        statement.bindLong(9, entity.getPriority());
        statement.bindString(10, entity.getDestinationPath());
        statement.bindLong(11, entity.getCreatedAt());
        statement.bindLong(12, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfUpdateSessionOffset = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE download_session SET currentByteOffset = ?, updatedAt = ? WHERE mediaId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateSessionState = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE download_session SET downloadState = ?, updatedAt = ? WHERE mediaId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementRetryCount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE download_session SET retryCount = ?, updatedAt = ? WHERE mediaId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTasks(final List<DownloadSessionEntity> tasks,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDownloadSessionEntity.insert(tasks);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSessionOffset(final String mediaId, final long offset, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSessionOffset.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, offset);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindString(_argIndex, mediaId);
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
          __preparedStmtOfUpdateSessionOffset.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSessionState(final String mediaId, final DownloadState state,
      final long updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSessionState.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromDownloadState(state);
        _stmt.bindString(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindString(_argIndex, mediaId);
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
          __preparedStmtOfUpdateSessionState.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementRetryCount(final String mediaId, final int count, final long updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementRetryCount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, count);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindString(_argIndex, mediaId);
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
          __preparedStmtOfIncrementRetryCount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingTasks(
      final Continuation<? super List<DownloadSessionEntity>> $completion) {
    final String _sql = "SELECT * FROM download_session WHERE downloadState IN ('QUEUED', 'DOWNLOADING', 'FAILED') ORDER BY priority DESC LIMIT 5";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DownloadSessionEntity>>() {
      @Override
      @NonNull
      public List<DownloadSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMediaId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaId");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfDownloadState = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadState");
          final int _cursorIndexOfCurrentByteOffset = CursorUtil.getColumnIndexOrThrow(_cursor, "currentByteOffset");
          final int _cursorIndexOfExpectedSize = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedSize");
          final int _cursorIndexOfExpectedChecksumMd5 = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedChecksumMd5");
          final int _cursorIndexOfExpectedChecksumSha256 = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedChecksumSha256");
          final int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retryCount");
          final int _cursorIndexOfPriority = CursorUtil.getColumnIndexOrThrow(_cursor, "priority");
          final int _cursorIndexOfDestinationPath = CursorUtil.getColumnIndexOrThrow(_cursor, "destinationPath");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<DownloadSessionEntity> _result = new ArrayList<DownloadSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DownloadSessionEntity _item;
            final String _tmpMediaId;
            _tmpMediaId = _cursor.getString(_cursorIndexOfMediaId);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final DownloadState _tmpDownloadState;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfDownloadState);
            _tmpDownloadState = __converters.toDownloadState(_tmp);
            final long _tmpCurrentByteOffset;
            _tmpCurrentByteOffset = _cursor.getLong(_cursorIndexOfCurrentByteOffset);
            final long _tmpExpectedSize;
            _tmpExpectedSize = _cursor.getLong(_cursorIndexOfExpectedSize);
            final String _tmpExpectedChecksumMd5;
            if (_cursor.isNull(_cursorIndexOfExpectedChecksumMd5)) {
              _tmpExpectedChecksumMd5 = null;
            } else {
              _tmpExpectedChecksumMd5 = _cursor.getString(_cursorIndexOfExpectedChecksumMd5);
            }
            final String _tmpExpectedChecksumSha256;
            if (_cursor.isNull(_cursorIndexOfExpectedChecksumSha256)) {
              _tmpExpectedChecksumSha256 = null;
            } else {
              _tmpExpectedChecksumSha256 = _cursor.getString(_cursorIndexOfExpectedChecksumSha256);
            }
            final int _tmpRetryCount;
            _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount);
            final int _tmpPriority;
            _tmpPriority = _cursor.getInt(_cursorIndexOfPriority);
            final String _tmpDestinationPath;
            _tmpDestinationPath = _cursor.getString(_cursorIndexOfDestinationPath);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new DownloadSessionEntity(_tmpMediaId,_tmpUrl,_tmpDownloadState,_tmpCurrentByteOffset,_tmpExpectedSize,_tmpExpectedChecksumMd5,_tmpExpectedChecksumSha256,_tmpRetryCount,_tmpPriority,_tmpDestinationPath,_tmpCreatedAt,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

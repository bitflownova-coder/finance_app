package com.financemanager.app.data.local.dao;

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
import com.financemanager.app.data.local.entities.ReceiptEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class ReceiptDao_Impl implements ReceiptDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReceiptEntity> __insertionAdapterOfReceiptEntity;

  private final EntityDeletionOrUpdateAdapter<ReceiptEntity> __deletionAdapterOfReceiptEntity;

  private final EntityDeletionOrUpdateAdapter<ReceiptEntity> __updateAdapterOfReceiptEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public ReceiptDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReceiptEntity = new EntityInsertionAdapter<ReceiptEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `receipts` (`receipt_id`,`transaction_id`,`image_uri`,`thumbnail_uri`,`note`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReceiptEntity entity) {
        statement.bindLong(1, entity.getReceiptId());
        statement.bindLong(2, entity.getTransactionId());
        statement.bindString(3, entity.getImageUri());
        if (entity.getThumbnailUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getThumbnailUri());
        }
        statement.bindString(5, entity.getNote());
        statement.bindLong(6, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfReceiptEntity = new EntityDeletionOrUpdateAdapter<ReceiptEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `receipts` WHERE `receipt_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReceiptEntity entity) {
        statement.bindLong(1, entity.getReceiptId());
      }
    };
    this.__updateAdapterOfReceiptEntity = new EntityDeletionOrUpdateAdapter<ReceiptEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `receipts` SET `receipt_id` = ?,`transaction_id` = ?,`image_uri` = ?,`thumbnail_uri` = ?,`note` = ?,`created_at` = ? WHERE `receipt_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReceiptEntity entity) {
        statement.bindLong(1, entity.getReceiptId());
        statement.bindLong(2, entity.getTransactionId());
        statement.bindString(3, entity.getImageUri());
        if (entity.getThumbnailUri() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getThumbnailUri());
        }
        statement.bindString(5, entity.getNote());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getReceiptId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM receipts WHERE receipt_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ReceiptEntity receipt, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfReceiptEntity.insertAndReturnId(receipt);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ReceiptEntity receipt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfReceiptEntity.handle(receipt);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final ReceiptEntity receipt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReceiptEntity.handle(receipt);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long receiptId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, receiptId);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getReceiptById(final long receiptId,
      final Continuation<? super ReceiptEntity> $completion) {
    final String _sql = "SELECT * FROM receipts WHERE receipt_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, receiptId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReceiptEntity>() {
      @Override
      @Nullable
      public ReceiptEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfReceiptId = CursorUtil.getColumnIndexOrThrow(_cursor, "receipt_id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "image_uri");
          final int _cursorIndexOfThumbnailUri = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_uri");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final ReceiptEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpReceiptId;
            _tmpReceiptId = _cursor.getLong(_cursorIndexOfReceiptId);
            final long _tmpTransactionId;
            _tmpTransactionId = _cursor.getLong(_cursorIndexOfTransactionId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpThumbnailUri;
            if (_cursor.isNull(_cursorIndexOfThumbnailUri)) {
              _tmpThumbnailUri = null;
            } else {
              _tmpThumbnailUri = _cursor.getString(_cursorIndexOfThumbnailUri);
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new ReceiptEntity(_tmpReceiptId,_tmpTransactionId,_tmpImageUri,_tmpThumbnailUri,_tmpNote,_tmpCreatedAt);
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
  public Flow<List<ReceiptEntity>> getReceiptsByTransactionId(final long transactionId) {
    final String _sql = "SELECT * FROM receipts WHERE transaction_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, transactionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"receipts"}, new Callable<List<ReceiptEntity>>() {
      @Override
      @NonNull
      public List<ReceiptEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfReceiptId = CursorUtil.getColumnIndexOrThrow(_cursor, "receipt_id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "image_uri");
          final int _cursorIndexOfThumbnailUri = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_uri");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<ReceiptEntity> _result = new ArrayList<ReceiptEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReceiptEntity _item;
            final long _tmpReceiptId;
            _tmpReceiptId = _cursor.getLong(_cursorIndexOfReceiptId);
            final long _tmpTransactionId;
            _tmpTransactionId = _cursor.getLong(_cursorIndexOfTransactionId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpThumbnailUri;
            if (_cursor.isNull(_cursorIndexOfThumbnailUri)) {
              _tmpThumbnailUri = null;
            } else {
              _tmpThumbnailUri = _cursor.getString(_cursorIndexOfThumbnailUri);
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new ReceiptEntity(_tmpReceiptId,_tmpTransactionId,_tmpImageUri,_tmpThumbnailUri,_tmpNote,_tmpCreatedAt);
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
  public Object getFirstReceiptByTransactionId(final long transactionId,
      final Continuation<? super ReceiptEntity> $completion) {
    final String _sql = "SELECT * FROM receipts WHERE transaction_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, transactionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReceiptEntity>() {
      @Override
      @Nullable
      public ReceiptEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfReceiptId = CursorUtil.getColumnIndexOrThrow(_cursor, "receipt_id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_id");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "image_uri");
          final int _cursorIndexOfThumbnailUri = CursorUtil.getColumnIndexOrThrow(_cursor, "thumbnail_uri");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final ReceiptEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpReceiptId;
            _tmpReceiptId = _cursor.getLong(_cursorIndexOfReceiptId);
            final long _tmpTransactionId;
            _tmpTransactionId = _cursor.getLong(_cursorIndexOfTransactionId);
            final String _tmpImageUri;
            _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            final String _tmpThumbnailUri;
            if (_cursor.isNull(_cursorIndexOfThumbnailUri)) {
              _tmpThumbnailUri = null;
            } else {
              _tmpThumbnailUri = _cursor.getString(_cursorIndexOfThumbnailUri);
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new ReceiptEntity(_tmpReceiptId,_tmpTransactionId,_tmpImageUri,_tmpThumbnailUri,_tmpNote,_tmpCreatedAt);
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
  public Object getReceiptCountForTransaction(final long transactionId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM receipts WHERE transaction_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, transactionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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

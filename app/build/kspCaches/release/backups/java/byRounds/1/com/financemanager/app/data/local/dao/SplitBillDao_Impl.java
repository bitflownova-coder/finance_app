package com.financemanager.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.financemanager.app.data.local.entities.ParticipantEntity;
import com.financemanager.app.data.local.entities.SplitBillEntity;
import java.lang.Class;
import java.lang.Double;
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
public final class SplitBillDao_Impl implements SplitBillDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SplitBillEntity> __insertionAdapterOfSplitBillEntity;

  private final EntityInsertionAdapter<ParticipantEntity> __insertionAdapterOfParticipantEntity;

  private final EntityDeletionOrUpdateAdapter<SplitBillEntity> __deletionAdapterOfSplitBillEntity;

  private final EntityDeletionOrUpdateAdapter<SplitBillEntity> __updateAdapterOfSplitBillEntity;

  private final EntityDeletionOrUpdateAdapter<ParticipantEntity> __updateAdapterOfParticipantEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsSettled;

  private final SharedSQLiteStatement __preparedStmtOfMarkParticipantAsPaid;

  public SplitBillDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSplitBillEntity = new EntityInsertionAdapter<SplitBillEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `split_bills` (`split_id`,`transaction_id`,`user_id`,`total_amount`,`description`,`split_type`,`created_at`,`is_settled`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SplitBillEntity entity) {
        statement.bindLong(1, entity.getSplitId());
        if (entity.getTransactionId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getTransactionId());
        }
        statement.bindLong(3, entity.getUserId());
        statement.bindDouble(4, entity.getTotalAmount());
        statement.bindString(5, entity.getDescription());
        statement.bindString(6, entity.getSplitType());
        statement.bindLong(7, entity.getCreatedAt());
        final int _tmp = entity.isSettled() ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__insertionAdapterOfParticipantEntity = new EntityInsertionAdapter<ParticipantEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `split_participants` (`participant_id`,`split_id`,`name`,`phone_number`,`share_amount`,`is_paid`,`paid_at`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ParticipantEntity entity) {
        statement.bindLong(1, entity.getParticipantId());
        statement.bindLong(2, entity.getSplitId());
        statement.bindString(3, entity.getName());
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhoneNumber());
        }
        statement.bindDouble(5, entity.getShareAmount());
        final int _tmp = entity.isPaid() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getPaidAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPaidAt());
        }
      }
    };
    this.__deletionAdapterOfSplitBillEntity = new EntityDeletionOrUpdateAdapter<SplitBillEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `split_bills` WHERE `split_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SplitBillEntity entity) {
        statement.bindLong(1, entity.getSplitId());
      }
    };
    this.__updateAdapterOfSplitBillEntity = new EntityDeletionOrUpdateAdapter<SplitBillEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `split_bills` SET `split_id` = ?,`transaction_id` = ?,`user_id` = ?,`total_amount` = ?,`description` = ?,`split_type` = ?,`created_at` = ?,`is_settled` = ? WHERE `split_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SplitBillEntity entity) {
        statement.bindLong(1, entity.getSplitId());
        if (entity.getTransactionId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getTransactionId());
        }
        statement.bindLong(3, entity.getUserId());
        statement.bindDouble(4, entity.getTotalAmount());
        statement.bindString(5, entity.getDescription());
        statement.bindString(6, entity.getSplitType());
        statement.bindLong(7, entity.getCreatedAt());
        final int _tmp = entity.isSettled() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getSplitId());
      }
    };
    this.__updateAdapterOfParticipantEntity = new EntityDeletionOrUpdateAdapter<ParticipantEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `split_participants` SET `participant_id` = ?,`split_id` = ?,`name` = ?,`phone_number` = ?,`share_amount` = ?,`is_paid` = ?,`paid_at` = ? WHERE `participant_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ParticipantEntity entity) {
        statement.bindLong(1, entity.getParticipantId());
        statement.bindLong(2, entity.getSplitId());
        statement.bindString(3, entity.getName());
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhoneNumber());
        }
        statement.bindDouble(5, entity.getShareAmount());
        final int _tmp = entity.isPaid() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getPaidAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getPaidAt());
        }
        statement.bindLong(8, entity.getParticipantId());
      }
    };
    this.__preparedStmtOfMarkAsSettled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE split_bills SET is_settled = 1 WHERE split_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkParticipantAsPaid = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE split_participants SET is_paid = 1, paid_at = ? WHERE participant_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSplitBill(final SplitBillEntity splitBill,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSplitBillEntity.insertAndReturnId(splitBill);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertParticipants(final List<ParticipantEntity> participants,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfParticipantEntity.insert(participants);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSplitBill(final SplitBillEntity splitBill,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSplitBillEntity.handle(splitBill);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSplitBill(final SplitBillEntity splitBill,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSplitBillEntity.handle(splitBill);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateParticipant(final ParticipantEntity participant,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfParticipantEntity.handle(participant);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSplitBillWithParticipants(final SplitBillEntity splitBill,
      final List<ParticipantEntity> participants, final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> SplitBillDao.DefaultImpls.insertSplitBillWithParticipants(SplitBillDao_Impl.this, splitBill, participants, __cont), $completion);
  }

  @Override
  public Object checkAndUpdateSettledStatus(final long splitId,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> SplitBillDao.DefaultImpls.checkAndUpdateSettledStatus(SplitBillDao_Impl.this, splitId, __cont), $completion);
  }

  @Override
  public Object markAsSettled(final long splitId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsSettled.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, splitId);
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
          __preparedStmtOfMarkAsSettled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markParticipantAsPaid(final long participantId, final long paidAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkParticipantAsPaid.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, paidAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, participantId);
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
          __preparedStmtOfMarkParticipantAsPaid.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SplitBillEntity>> getSplitBills(final long userId) {
    final String _sql = "SELECT * FROM split_bills WHERE user_id = ? ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"split_bills"}, new Callable<List<SplitBillEntity>>() {
      @Override
      @NonNull
      public List<SplitBillEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSplitId = CursorUtil.getColumnIndexOrThrow(_cursor, "split_id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSplitType = CursorUtil.getColumnIndexOrThrow(_cursor, "split_type");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "is_settled");
          final List<SplitBillEntity> _result = new ArrayList<SplitBillEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SplitBillEntity _item;
            final long _tmpSplitId;
            _tmpSplitId = _cursor.getLong(_cursorIndexOfSplitId);
            final Long _tmpTransactionId;
            if (_cursor.isNull(_cursorIndexOfTransactionId)) {
              _tmpTransactionId = null;
            } else {
              _tmpTransactionId = _cursor.getLong(_cursorIndexOfTransactionId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpSplitType;
            _tmpSplitType = _cursor.getString(_cursorIndexOfSplitType);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            _item = new SplitBillEntity(_tmpSplitId,_tmpTransactionId,_tmpUserId,_tmpTotalAmount,_tmpDescription,_tmpSplitType,_tmpCreatedAt,_tmpIsSettled);
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
  public Object getSplitBillById(final long splitId,
      final Continuation<? super SplitBillEntity> $completion) {
    final String _sql = "SELECT * FROM split_bills WHERE split_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, splitId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SplitBillEntity>() {
      @Override
      @Nullable
      public SplitBillEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSplitId = CursorUtil.getColumnIndexOrThrow(_cursor, "split_id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSplitType = CursorUtil.getColumnIndexOrThrow(_cursor, "split_type");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "is_settled");
          final SplitBillEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpSplitId;
            _tmpSplitId = _cursor.getLong(_cursorIndexOfSplitId);
            final Long _tmpTransactionId;
            if (_cursor.isNull(_cursorIndexOfTransactionId)) {
              _tmpTransactionId = null;
            } else {
              _tmpTransactionId = _cursor.getLong(_cursorIndexOfTransactionId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpSplitType;
            _tmpSplitType = _cursor.getString(_cursorIndexOfSplitType);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            _result = new SplitBillEntity(_tmpSplitId,_tmpTransactionId,_tmpUserId,_tmpTotalAmount,_tmpDescription,_tmpSplitType,_tmpCreatedAt,_tmpIsSettled);
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
  public Object getParticipants(final long splitId,
      final Continuation<? super List<ParticipantEntity>> $completion) {
    final String _sql = "SELECT * FROM split_participants WHERE split_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, splitId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ParticipantEntity>>() {
      @Override
      @NonNull
      public List<ParticipantEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfParticipantId = CursorUtil.getColumnIndexOrThrow(_cursor, "participant_id");
          final int _cursorIndexOfSplitId = CursorUtil.getColumnIndexOrThrow(_cursor, "split_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
          final int _cursorIndexOfShareAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "share_amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_paid");
          final int _cursorIndexOfPaidAt = CursorUtil.getColumnIndexOrThrow(_cursor, "paid_at");
          final List<ParticipantEntity> _result = new ArrayList<ParticipantEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ParticipantEntity _item;
            final long _tmpParticipantId;
            _tmpParticipantId = _cursor.getLong(_cursorIndexOfParticipantId);
            final long _tmpSplitId;
            _tmpSplitId = _cursor.getLong(_cursorIndexOfSplitId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final double _tmpShareAmount;
            _tmpShareAmount = _cursor.getDouble(_cursorIndexOfShareAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            final Long _tmpPaidAt;
            if (_cursor.isNull(_cursorIndexOfPaidAt)) {
              _tmpPaidAt = null;
            } else {
              _tmpPaidAt = _cursor.getLong(_cursorIndexOfPaidAt);
            }
            _item = new ParticipantEntity(_tmpParticipantId,_tmpSplitId,_tmpName,_tmpPhoneNumber,_tmpShareAmount,_tmpIsPaid,_tmpPaidAt);
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
  public Flow<List<ParticipantEntity>> getParticipantsFlow(final long splitId) {
    final String _sql = "SELECT * FROM split_participants WHERE split_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, splitId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"split_participants"}, new Callable<List<ParticipantEntity>>() {
      @Override
      @NonNull
      public List<ParticipantEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfParticipantId = CursorUtil.getColumnIndexOrThrow(_cursor, "participant_id");
          final int _cursorIndexOfSplitId = CursorUtil.getColumnIndexOrThrow(_cursor, "split_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phone_number");
          final int _cursorIndexOfShareAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "share_amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_paid");
          final int _cursorIndexOfPaidAt = CursorUtil.getColumnIndexOrThrow(_cursor, "paid_at");
          final List<ParticipantEntity> _result = new ArrayList<ParticipantEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ParticipantEntity _item;
            final long _tmpParticipantId;
            _tmpParticipantId = _cursor.getLong(_cursorIndexOfParticipantId);
            final long _tmpSplitId;
            _tmpSplitId = _cursor.getLong(_cursorIndexOfSplitId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final double _tmpShareAmount;
            _tmpShareAmount = _cursor.getDouble(_cursorIndexOfShareAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            final Long _tmpPaidAt;
            if (_cursor.isNull(_cursorIndexOfPaidAt)) {
              _tmpPaidAt = null;
            } else {
              _tmpPaidAt = _cursor.getLong(_cursorIndexOfPaidAt);
            }
            _item = new ParticipantEntity(_tmpParticipantId,_tmpSplitId,_tmpName,_tmpPhoneNumber,_tmpShareAmount,_tmpIsPaid,_tmpPaidAt);
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
  public Flow<List<SplitBillEntity>> getUnSettledBills(final long userId) {
    final String _sql = "SELECT * FROM split_bills WHERE user_id = ? AND is_settled = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"split_bills"}, new Callable<List<SplitBillEntity>>() {
      @Override
      @NonNull
      public List<SplitBillEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSplitId = CursorUtil.getColumnIndexOrThrow(_cursor, "split_id");
          final int _cursorIndexOfTransactionId = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSplitType = CursorUtil.getColumnIndexOrThrow(_cursor, "split_type");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfIsSettled = CursorUtil.getColumnIndexOrThrow(_cursor, "is_settled");
          final List<SplitBillEntity> _result = new ArrayList<SplitBillEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SplitBillEntity _item;
            final long _tmpSplitId;
            _tmpSplitId = _cursor.getLong(_cursorIndexOfSplitId);
            final Long _tmpTransactionId;
            if (_cursor.isNull(_cursorIndexOfTransactionId)) {
              _tmpTransactionId = null;
            } else {
              _tmpTransactionId = _cursor.getLong(_cursorIndexOfTransactionId);
            }
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpSplitType;
            _tmpSplitType = _cursor.getString(_cursorIndexOfSplitType);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpIsSettled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSettled);
            _tmpIsSettled = _tmp != 0;
            _item = new SplitBillEntity(_tmpSplitId,_tmpTransactionId,_tmpUserId,_tmpTotalAmount,_tmpDescription,_tmpSplitType,_tmpCreatedAt,_tmpIsSettled);
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
  public Flow<Integer> getUnSettledCount(final long userId) {
    final String _sql = "SELECT COUNT(*) FROM split_bills WHERE user_id = ? AND is_settled = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"split_bills"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTotalUnSettledAmount(final long userId,
      final Continuation<? super Double> $completion) {
    final String _sql = "SELECT SUM(total_amount) FROM split_bills WHERE user_id = ? AND is_settled = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @Nullable
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

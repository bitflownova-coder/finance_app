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
import com.financemanager.app.data.local.entities.BudgetEntity;
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
public final class BudgetDao_Impl implements BudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BudgetEntity> __insertionAdapterOfBudgetEntity;

  private final EntityDeletionOrUpdateAdapter<BudgetEntity> __deletionAdapterOfBudgetEntity;

  private final EntityDeletionOrUpdateAdapter<BudgetEntity> __updateAdapterOfBudgetEntity;

  private final SharedSQLiteStatement __preparedStmtOfIncreaseSpentAmount;

  private final SharedSQLiteStatement __preparedStmtOfDecreaseSpentAmount;

  private final SharedSQLiteStatement __preparedStmtOfResetSpentAmount;

  private final SharedSQLiteStatement __preparedStmtOfDeactivateExpiredBudgets;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllByUserId;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudgetEntity = new EntityInsertionAdapter<BudgetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `budgets` (`budget_id`,`user_id`,`budget_name`,`total_budget`,`spent_amount`,`period_type`,`period_start`,`period_end`,`category`,`alert_threshold`,`is_active`,`created_at`,`updated_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetEntity entity) {
        statement.bindLong(1, entity.getBudgetId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getBudgetName());
        statement.bindDouble(4, entity.getTotalBudget());
        statement.bindDouble(5, entity.getSpentAmount());
        statement.bindString(6, entity.getPeriodType());
        statement.bindLong(7, entity.getPeriodStart());
        statement.bindLong(8, entity.getPeriodEnd());
        if (entity.getCategory() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCategory());
        }
        statement.bindDouble(10, entity.getAlertThreshold());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(11, _tmp);
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfBudgetEntity = new EntityDeletionOrUpdateAdapter<BudgetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `budgets` WHERE `budget_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetEntity entity) {
        statement.bindLong(1, entity.getBudgetId());
      }
    };
    this.__updateAdapterOfBudgetEntity = new EntityDeletionOrUpdateAdapter<BudgetEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `budgets` SET `budget_id` = ?,`user_id` = ?,`budget_name` = ?,`total_budget` = ?,`spent_amount` = ?,`period_type` = ?,`period_start` = ?,`period_end` = ?,`category` = ?,`alert_threshold` = ?,`is_active` = ?,`created_at` = ?,`updated_at` = ? WHERE `budget_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BudgetEntity entity) {
        statement.bindLong(1, entity.getBudgetId());
        statement.bindLong(2, entity.getUserId());
        statement.bindString(3, entity.getBudgetName());
        statement.bindDouble(4, entity.getTotalBudget());
        statement.bindDouble(5, entity.getSpentAmount());
        statement.bindString(6, entity.getPeriodType());
        statement.bindLong(7, entity.getPeriodStart());
        statement.bindLong(8, entity.getPeriodEnd());
        if (entity.getCategory() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCategory());
        }
        statement.bindDouble(10, entity.getAlertThreshold());
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(11, _tmp);
        statement.bindLong(12, entity.getCreatedAt());
        statement.bindLong(13, entity.getUpdatedAt());
        statement.bindLong(14, entity.getBudgetId());
      }
    };
    this.__preparedStmtOfIncreaseSpentAmount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE budgets SET spent_amount = spent_amount + ?, updated_at = ? WHERE budget_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDecreaseSpentAmount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE budgets SET spent_amount = spent_amount - ?, updated_at = ? WHERE budget_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfResetSpentAmount = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE budgets SET spent_amount = 0, updated_at = ? WHERE budget_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeactivateExpiredBudgets = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE budgets SET is_active = 0 WHERE period_end < ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllByUserId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM budgets WHERE user_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final BudgetEntity budget, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBudgetEntity.insertAndReturnId(budget);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final BudgetEntity budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBudgetEntity.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BudgetEntity budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBudgetEntity.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object increaseSpentAmount(final long budgetId, final double amount, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncreaseSpentAmount.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, amount);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, budgetId);
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
          __preparedStmtOfIncreaseSpentAmount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object decreaseSpentAmount(final long budgetId, final double amount, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDecreaseSpentAmount.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, amount);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, budgetId);
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
          __preparedStmtOfDecreaseSpentAmount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object resetSpentAmount(final long budgetId, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfResetSpentAmount.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, budgetId);
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
          __preparedStmtOfResetSpentAmount.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deactivateExpiredBudgets(final long currentTime,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeactivateExpiredBudgets.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, currentTime);
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
          __preparedStmtOfDeactivateExpiredBudgets.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllByUserId(final long userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllByUserId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
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
          __preparedStmtOfDeleteAllByUserId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getBudgetById(final long budgetId,
      final Continuation<? super BudgetEntity> $completion) {
    final String _sql = "SELECT * FROM budgets WHERE budget_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, budgetId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BudgetEntity>() {
      @Override
      @Nullable
      public BudgetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBudgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfBudgetName = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_name");
          final int _cursorIndexOfTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "total_budget");
          final int _cursorIndexOfSpentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "spent_amount");
          final int _cursorIndexOfPeriodType = CursorUtil.getColumnIndexOrThrow(_cursor, "period_type");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "period_start");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "period_end");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alert_threshold");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final BudgetEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBudgetId;
            _tmpBudgetId = _cursor.getLong(_cursorIndexOfBudgetId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpBudgetName;
            _tmpBudgetName = _cursor.getString(_cursorIndexOfBudgetName);
            final double _tmpTotalBudget;
            _tmpTotalBudget = _cursor.getDouble(_cursorIndexOfTotalBudget);
            final double _tmpSpentAmount;
            _tmpSpentAmount = _cursor.getDouble(_cursorIndexOfSpentAmount);
            final String _tmpPeriodType;
            _tmpPeriodType = _cursor.getString(_cursorIndexOfPeriodType);
            final long _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getLong(_cursorIndexOfPeriodStart);
            final long _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getLong(_cursorIndexOfPeriodEnd);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final double _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getDouble(_cursorIndexOfAlertThreshold);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new BudgetEntity(_tmpBudgetId,_tmpUserId,_tmpBudgetName,_tmpTotalBudget,_tmpSpentAmount,_tmpPeriodType,_tmpPeriodStart,_tmpPeriodEnd,_tmpCategory,_tmpAlertThreshold,_tmpIsActive,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<BudgetEntity>> getActiveBudgets(final long userId) {
    final String _sql = "SELECT * FROM budgets WHERE user_id = ? AND is_active = 1 ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budgets"}, new Callable<List<BudgetEntity>>() {
      @Override
      @NonNull
      public List<BudgetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBudgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfBudgetName = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_name");
          final int _cursorIndexOfTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "total_budget");
          final int _cursorIndexOfSpentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "spent_amount");
          final int _cursorIndexOfPeriodType = CursorUtil.getColumnIndexOrThrow(_cursor, "period_type");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "period_start");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "period_end");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alert_threshold");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<BudgetEntity> _result = new ArrayList<BudgetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BudgetEntity _item;
            final long _tmpBudgetId;
            _tmpBudgetId = _cursor.getLong(_cursorIndexOfBudgetId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpBudgetName;
            _tmpBudgetName = _cursor.getString(_cursorIndexOfBudgetName);
            final double _tmpTotalBudget;
            _tmpTotalBudget = _cursor.getDouble(_cursorIndexOfTotalBudget);
            final double _tmpSpentAmount;
            _tmpSpentAmount = _cursor.getDouble(_cursorIndexOfSpentAmount);
            final String _tmpPeriodType;
            _tmpPeriodType = _cursor.getString(_cursorIndexOfPeriodType);
            final long _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getLong(_cursorIndexOfPeriodStart);
            final long _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getLong(_cursorIndexOfPeriodEnd);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final double _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getDouble(_cursorIndexOfAlertThreshold);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new BudgetEntity(_tmpBudgetId,_tmpUserId,_tmpBudgetName,_tmpTotalBudget,_tmpSpentAmount,_tmpPeriodType,_tmpPeriodStart,_tmpPeriodEnd,_tmpCategory,_tmpAlertThreshold,_tmpIsActive,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<BudgetEntity>> getCurrentBudgets(final long userId, final long currentTime) {
    final String _sql = "SELECT * FROM budgets WHERE user_id = ? AND period_start <= ? AND period_end >= ? AND is_active = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, currentTime);
    _argIndex = 3;
    _statement.bindLong(_argIndex, currentTime);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budgets"}, new Callable<List<BudgetEntity>>() {
      @Override
      @NonNull
      public List<BudgetEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBudgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfBudgetName = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_name");
          final int _cursorIndexOfTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "total_budget");
          final int _cursorIndexOfSpentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "spent_amount");
          final int _cursorIndexOfPeriodType = CursorUtil.getColumnIndexOrThrow(_cursor, "period_type");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "period_start");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "period_end");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alert_threshold");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<BudgetEntity> _result = new ArrayList<BudgetEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BudgetEntity _item;
            final long _tmpBudgetId;
            _tmpBudgetId = _cursor.getLong(_cursorIndexOfBudgetId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpBudgetName;
            _tmpBudgetName = _cursor.getString(_cursorIndexOfBudgetName);
            final double _tmpTotalBudget;
            _tmpTotalBudget = _cursor.getDouble(_cursorIndexOfTotalBudget);
            final double _tmpSpentAmount;
            _tmpSpentAmount = _cursor.getDouble(_cursorIndexOfSpentAmount);
            final String _tmpPeriodType;
            _tmpPeriodType = _cursor.getString(_cursorIndexOfPeriodType);
            final long _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getLong(_cursorIndexOfPeriodStart);
            final long _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getLong(_cursorIndexOfPeriodEnd);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final double _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getDouble(_cursorIndexOfAlertThreshold);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new BudgetEntity(_tmpBudgetId,_tmpUserId,_tmpBudgetName,_tmpTotalBudget,_tmpSpentAmount,_tmpPeriodType,_tmpPeriodStart,_tmpPeriodEnd,_tmpCategory,_tmpAlertThreshold,_tmpIsActive,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getCategoryBudget(final long userId, final String category, final long currentTime,
      final Continuation<? super BudgetEntity> $completion) {
    final String _sql = "SELECT * FROM budgets WHERE user_id = ? AND category = ? AND period_start <= ? AND period_end >= ? AND is_active = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    _statement.bindString(_argIndex, category);
    _argIndex = 3;
    _statement.bindLong(_argIndex, currentTime);
    _argIndex = 4;
    _statement.bindLong(_argIndex, currentTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BudgetEntity>() {
      @Override
      @Nullable
      public BudgetEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBudgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfBudgetName = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_name");
          final int _cursorIndexOfTotalBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "total_budget");
          final int _cursorIndexOfSpentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "spent_amount");
          final int _cursorIndexOfPeriodType = CursorUtil.getColumnIndexOrThrow(_cursor, "period_type");
          final int _cursorIndexOfPeriodStart = CursorUtil.getColumnIndexOrThrow(_cursor, "period_start");
          final int _cursorIndexOfPeriodEnd = CursorUtil.getColumnIndexOrThrow(_cursor, "period_end");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alert_threshold");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "is_active");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final BudgetEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBudgetId;
            _tmpBudgetId = _cursor.getLong(_cursorIndexOfBudgetId);
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpBudgetName;
            _tmpBudgetName = _cursor.getString(_cursorIndexOfBudgetName);
            final double _tmpTotalBudget;
            _tmpTotalBudget = _cursor.getDouble(_cursorIndexOfTotalBudget);
            final double _tmpSpentAmount;
            _tmpSpentAmount = _cursor.getDouble(_cursorIndexOfSpentAmount);
            final String _tmpPeriodType;
            _tmpPeriodType = _cursor.getString(_cursorIndexOfPeriodType);
            final long _tmpPeriodStart;
            _tmpPeriodStart = _cursor.getLong(_cursorIndexOfPeriodStart);
            final long _tmpPeriodEnd;
            _tmpPeriodEnd = _cursor.getLong(_cursorIndexOfPeriodEnd);
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final double _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getDouble(_cursorIndexOfAlertThreshold);
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new BudgetEntity(_tmpBudgetId,_tmpUserId,_tmpBudgetName,_tmpTotalBudget,_tmpSpentAmount,_tmpPeriodType,_tmpPeriodStart,_tmpPeriodEnd,_tmpCategory,_tmpAlertThreshold,_tmpIsActive,_tmpCreatedAt,_tmpUpdatedAt);
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

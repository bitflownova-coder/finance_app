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
import com.financemanager.app.data.local.entities.UserSettingsEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserSettingsDao_Impl implements UserSettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserSettingsEntity> __insertionAdapterOfUserSettingsEntity;

  private final EntityDeletionOrUpdateAdapter<UserSettingsEntity> __updateAdapterOfUserSettingsEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTheme;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCurrency;

  private final SharedSQLiteStatement __preparedStmtOfUpdateNotifications;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBudgetAlerts;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBiometric;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBackupSettings;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public UserSettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserSettingsEntity = new EntityInsertionAdapter<UserSettingsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `user_settings` (`user_id`,`theme`,`currency`,`currency_symbol`,`notifications_enabled`,`budget_alerts_enabled`,`transaction_reminders_enabled`,`biometric_enabled`,`auto_backup_enabled`,`last_backup_time`,`created_at`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserSettingsEntity entity) {
        statement.bindLong(1, entity.getUserId());
        statement.bindString(2, entity.getTheme());
        statement.bindString(3, entity.getCurrency());
        statement.bindString(4, entity.getCurrencySymbol());
        final int _tmp = entity.getNotificationsEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final int _tmp_1 = entity.getBudgetAlertsEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        final int _tmp_2 = entity.getTransactionRemindersEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        final int _tmp_3 = entity.getBiometricEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        final int _tmp_4 = entity.getAutoBackupEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_4);
        if (entity.getLastBackupTime() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getLastBackupTime());
        }
        statement.bindLong(11, entity.getCreatedAt());
        statement.bindLong(12, entity.getUpdatedAt());
      }
    };
    this.__updateAdapterOfUserSettingsEntity = new EntityDeletionOrUpdateAdapter<UserSettingsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `user_settings` SET `user_id` = ?,`theme` = ?,`currency` = ?,`currency_symbol` = ?,`notifications_enabled` = ?,`budget_alerts_enabled` = ?,`transaction_reminders_enabled` = ?,`biometric_enabled` = ?,`auto_backup_enabled` = ?,`last_backup_time` = ?,`created_at` = ?,`updated_at` = ? WHERE `user_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserSettingsEntity entity) {
        statement.bindLong(1, entity.getUserId());
        statement.bindString(2, entity.getTheme());
        statement.bindString(3, entity.getCurrency());
        statement.bindString(4, entity.getCurrencySymbol());
        final int _tmp = entity.getNotificationsEnabled() ? 1 : 0;
        statement.bindLong(5, _tmp);
        final int _tmp_1 = entity.getBudgetAlertsEnabled() ? 1 : 0;
        statement.bindLong(6, _tmp_1);
        final int _tmp_2 = entity.getTransactionRemindersEnabled() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        final int _tmp_3 = entity.getBiometricEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        final int _tmp_4 = entity.getAutoBackupEnabled() ? 1 : 0;
        statement.bindLong(9, _tmp_4);
        if (entity.getLastBackupTime() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getLastBackupTime());
        }
        statement.bindLong(11, entity.getCreatedAt());
        statement.bindLong(12, entity.getUpdatedAt());
        statement.bindLong(13, entity.getUserId());
      }
    };
    this.__preparedStmtOfUpdateTheme = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_settings SET theme = ?, updated_at = ? WHERE user_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCurrency = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_settings SET currency = ?, currency_symbol = ?, updated_at = ? WHERE user_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateNotifications = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_settings SET notifications_enabled = ?, updated_at = ? WHERE user_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBudgetAlerts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_settings SET budget_alerts_enabled = ?, updated_at = ? WHERE user_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBiometric = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_settings SET biometric_enabled = ?, updated_at = ? WHERE user_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBackupSettings = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE user_settings SET auto_backup_enabled = ?, last_backup_time = ?, updated_at = ? WHERE user_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user_settings WHERE user_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final UserSettingsEntity settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserSettingsEntity.insert(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final UserSettingsEntity settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUserSettingsEntity.handle(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTheme(final long userId, final String theme, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTheme.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, theme);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
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
          __preparedStmtOfUpdateTheme.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCurrency(final long userId, final String currency, final String symbol,
      final long timestamp, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCurrency.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, currency);
        _argIndex = 2;
        _stmt.bindString(_argIndex, symbol);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 4;
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
          __preparedStmtOfUpdateCurrency.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateNotifications(final long userId, final boolean enabled, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateNotifications.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
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
          __preparedStmtOfUpdateNotifications.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBudgetAlerts(final long userId, final boolean enabled, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBudgetAlerts.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
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
          __preparedStmtOfUpdateBudgetAlerts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBiometric(final long userId, final boolean enabled, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBiometric.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 3;
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
          __preparedStmtOfUpdateBiometric.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBackupSettings(final long userId, final boolean enabled,
      final Long backupTime, final long timestamp, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBackupSettings.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (backupTime == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, backupTime);
        }
        _argIndex = 3;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 4;
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
          __preparedStmtOfUpdateBackupSettings.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final long userId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
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
          __preparedStmtOfDelete.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserSettingsEntity> getSettings(final long userId) {
    final String _sql = "SELECT * FROM user_settings WHERE user_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"user_settings"}, new Callable<UserSettingsEntity>() {
      @Override
      @Nullable
      public UserSettingsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCurrencySymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "currency_symbol");
          final int _cursorIndexOfNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notifications_enabled");
          final int _cursorIndexOfBudgetAlertsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_alerts_enabled");
          final int _cursorIndexOfTransactionRemindersEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_reminders_enabled");
          final int _cursorIndexOfBiometricEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "biometric_enabled");
          final int _cursorIndexOfAutoBackupEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_backup_enabled");
          final int _cursorIndexOfLastBackupTime = CursorUtil.getColumnIndexOrThrow(_cursor, "last_backup_time");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final UserSettingsEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpTheme;
            _tmpTheme = _cursor.getString(_cursorIndexOfTheme);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpCurrencySymbol;
            _tmpCurrencySymbol = _cursor.getString(_cursorIndexOfCurrencySymbol);
            final boolean _tmpNotificationsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfNotificationsEnabled);
            _tmpNotificationsEnabled = _tmp != 0;
            final boolean _tmpBudgetAlertsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfBudgetAlertsEnabled);
            _tmpBudgetAlertsEnabled = _tmp_1 != 0;
            final boolean _tmpTransactionRemindersEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfTransactionRemindersEnabled);
            _tmpTransactionRemindersEnabled = _tmp_2 != 0;
            final boolean _tmpBiometricEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfBiometricEnabled);
            _tmpBiometricEnabled = _tmp_3 != 0;
            final boolean _tmpAutoBackupEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfAutoBackupEnabled);
            _tmpAutoBackupEnabled = _tmp_4 != 0;
            final Long _tmpLastBackupTime;
            if (_cursor.isNull(_cursorIndexOfLastBackupTime)) {
              _tmpLastBackupTime = null;
            } else {
              _tmpLastBackupTime = _cursor.getLong(_cursorIndexOfLastBackupTime);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new UserSettingsEntity(_tmpUserId,_tmpTheme,_tmpCurrency,_tmpCurrencySymbol,_tmpNotificationsEnabled,_tmpBudgetAlertsEnabled,_tmpTransactionRemindersEnabled,_tmpBiometricEnabled,_tmpAutoBackupEnabled,_tmpLastBackupTime,_tmpCreatedAt,_tmpUpdatedAt);
          } else {
            _result = null;
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
  public Object getSettingsOnce(final long userId,
      final Continuation<? super UserSettingsEntity> $completion) {
    final String _sql = "SELECT * FROM user_settings WHERE user_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserSettingsEntity>() {
      @Override
      @Nullable
      public UserSettingsEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfTheme = CursorUtil.getColumnIndexOrThrow(_cursor, "theme");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfCurrencySymbol = CursorUtil.getColumnIndexOrThrow(_cursor, "currency_symbol");
          final int _cursorIndexOfNotificationsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "notifications_enabled");
          final int _cursorIndexOfBudgetAlertsEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "budget_alerts_enabled");
          final int _cursorIndexOfTransactionRemindersEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "transaction_reminders_enabled");
          final int _cursorIndexOfBiometricEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "biometric_enabled");
          final int _cursorIndexOfAutoBackupEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "auto_backup_enabled");
          final int _cursorIndexOfLastBackupTime = CursorUtil.getColumnIndexOrThrow(_cursor, "last_backup_time");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final UserSettingsEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpUserId;
            _tmpUserId = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpTheme;
            _tmpTheme = _cursor.getString(_cursorIndexOfTheme);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpCurrencySymbol;
            _tmpCurrencySymbol = _cursor.getString(_cursorIndexOfCurrencySymbol);
            final boolean _tmpNotificationsEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfNotificationsEnabled);
            _tmpNotificationsEnabled = _tmp != 0;
            final boolean _tmpBudgetAlertsEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfBudgetAlertsEnabled);
            _tmpBudgetAlertsEnabled = _tmp_1 != 0;
            final boolean _tmpTransactionRemindersEnabled;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfTransactionRemindersEnabled);
            _tmpTransactionRemindersEnabled = _tmp_2 != 0;
            final boolean _tmpBiometricEnabled;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfBiometricEnabled);
            _tmpBiometricEnabled = _tmp_3 != 0;
            final boolean _tmpAutoBackupEnabled;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfAutoBackupEnabled);
            _tmpAutoBackupEnabled = _tmp_4 != 0;
            final Long _tmpLastBackupTime;
            if (_cursor.isNull(_cursorIndexOfLastBackupTime)) {
              _tmpLastBackupTime = null;
            } else {
              _tmpLastBackupTime = _cursor.getLong(_cursorIndexOfLastBackupTime);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new UserSettingsEntity(_tmpUserId,_tmpTheme,_tmpCurrency,_tmpCurrencySymbol,_tmpNotificationsEnabled,_tmpBudgetAlertsEnabled,_tmpTransactionRemindersEnabled,_tmpBiometricEnabled,_tmpAutoBackupEnabled,_tmpLastBackupTime,_tmpCreatedAt,_tmpUpdatedAt);
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

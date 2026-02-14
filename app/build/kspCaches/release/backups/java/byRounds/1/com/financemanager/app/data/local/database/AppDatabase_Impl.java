package com.financemanager.app.data.local.database;

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
import com.financemanager.app.data.local.dao.BankAccountDao;
import com.financemanager.app.data.local.dao.BankAccountDao_Impl;
import com.financemanager.app.data.local.dao.BudgetDao;
import com.financemanager.app.data.local.dao.BudgetDao_Impl;
import com.financemanager.app.data.local.dao.ReceiptDao;
import com.financemanager.app.data.local.dao.ReceiptDao_Impl;
import com.financemanager.app.data.local.dao.RecurringTransactionDao;
import com.financemanager.app.data.local.dao.RecurringTransactionDao_Impl;
import com.financemanager.app.data.local.dao.SavingsGoalDao;
import com.financemanager.app.data.local.dao.SavingsGoalDao_Impl;
import com.financemanager.app.data.local.dao.SplitBillDao;
import com.financemanager.app.data.local.dao.SplitBillDao_Impl;
import com.financemanager.app.data.local.dao.TransactionDao;
import com.financemanager.app.data.local.dao.TransactionDao_Impl;
import com.financemanager.app.data.local.dao.UserDao;
import com.financemanager.app.data.local.dao.UserDao_Impl;
import com.financemanager.app.data.local.dao.UserSettingsDao;
import com.financemanager.app.data.local.dao.UserSettingsDao_Impl;
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
  private volatile UserDao _userDao;

  private volatile BankAccountDao _bankAccountDao;

  private volatile TransactionDao _transactionDao;

  private volatile BudgetDao _budgetDao;

  private volatile UserSettingsDao _userSettingsDao;

  private volatile RecurringTransactionDao _recurringTransactionDao;

  private volatile SavingsGoalDao _savingsGoalDao;

  private volatile ReceiptDao _receiptDao;

  private volatile SplitBillDao _splitBillDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(10) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`user_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `full_name` TEXT NOT NULL, `phone` TEXT NOT NULL, `pin_hash` TEXT NOT NULL, `upi_id` TEXT, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, `is_active` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_users_phone` ON `users` (`phone`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bank_accounts` (`account_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `account_name` TEXT NOT NULL, `account_number` TEXT NOT NULL, `bank_name` TEXT NOT NULL, `ifsc_code` TEXT, `account_type` TEXT NOT NULL, `balance` REAL NOT NULL, `currency` TEXT NOT NULL, `is_primary` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bank_accounts_user_id` ON `bank_accounts` (`user_id`)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_bank_accounts_account_number` ON `bank_accounts` (`account_number`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`transaction_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `account_id` INTEGER NOT NULL, `amount` REAL NOT NULL, `transaction_type` TEXT NOT NULL, `category` TEXT NOT NULL, `description` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `receipt_path` TEXT, `notes` TEXT, `is_recurring` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`account_id`) REFERENCES `bank_accounts`(`account_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_user_id` ON `transactions` (`user_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_account_id` ON `transactions` (`account_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_timestamp` ON `transactions` (`timestamp`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_category` ON `transactions` (`category`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transactions_transaction_type` ON `transactions` (`transaction_type`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `budgets` (`budget_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `budget_name` TEXT NOT NULL, `total_budget` REAL NOT NULL, `spent_amount` REAL NOT NULL, `period_type` TEXT NOT NULL, `period_start` INTEGER NOT NULL, `period_end` INTEGER NOT NULL, `category` TEXT, `alert_threshold` REAL NOT NULL, `is_active` INTEGER NOT NULL, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_budgets_user_id` ON `budgets` (`user_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_budgets_period_start_period_end` ON `budgets` (`period_start`, `period_end`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `user_settings` (`user_id` INTEGER NOT NULL, `theme` TEXT NOT NULL, `currency` TEXT NOT NULL, `currency_symbol` TEXT NOT NULL, `notifications_enabled` INTEGER NOT NULL, `budget_alerts_enabled` INTEGER NOT NULL, `transaction_reminders_enabled` INTEGER NOT NULL, `biometric_enabled` INTEGER NOT NULL, `auto_backup_enabled` INTEGER NOT NULL, `last_backup_time` INTEGER, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, PRIMARY KEY(`user_id`), FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_user_settings_user_id` ON `user_settings` (`user_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recurring_transactions` (`recurring_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `account_id` INTEGER NOT NULL, `amount` REAL NOT NULL, `type` TEXT NOT NULL, `category` TEXT NOT NULL, `description` TEXT NOT NULL, `frequency` TEXT NOT NULL, `start_date` INTEGER NOT NULL, `end_date` INTEGER, `next_occurrence` INTEGER NOT NULL, `is_active` INTEGER NOT NULL, `last_generated` INTEGER, `created_at` INTEGER NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`account_id`) REFERENCES `bank_accounts`(`account_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_user_id` ON `recurring_transactions` (`user_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_account_id` ON `recurring_transactions` (`account_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_next_occurrence` ON `recurring_transactions` (`next_occurrence`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_recurring_transactions_is_active` ON `recurring_transactions` (`is_active`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `savings_goals` (`goal_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `target_amount` REAL NOT NULL, `current_amount` REAL NOT NULL, `target_date` INTEGER NOT NULL, `category` TEXT NOT NULL, `priority` TEXT NOT NULL, `status` TEXT NOT NULL, `created_at` INTEGER NOT NULL, `is_archived` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_savings_goals_user_id` ON `savings_goals` (`user_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_savings_goals_target_date` ON `savings_goals` (`target_date`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_savings_goals_status` ON `savings_goals` (`status`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_savings_goals_is_archived` ON `savings_goals` (`is_archived`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `goal_contributions` (`contribution_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `goal_id` INTEGER NOT NULL, `amount` REAL NOT NULL, `note` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `transaction_id` INTEGER, FOREIGN KEY(`goal_id`) REFERENCES `savings_goals`(`goal_id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`transaction_id`) REFERENCES `transactions`(`transaction_id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_goal_contributions_goal_id` ON `goal_contributions` (`goal_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_goal_contributions_timestamp` ON `goal_contributions` (`timestamp`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_goal_contributions_transaction_id` ON `goal_contributions` (`transaction_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `receipts` (`receipt_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `transaction_id` INTEGER NOT NULL, `image_uri` TEXT NOT NULL, `thumbnail_uri` TEXT, `note` TEXT NOT NULL, `created_at` INTEGER NOT NULL, FOREIGN KEY(`transaction_id`) REFERENCES `transactions`(`transaction_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_receipts_transaction_id` ON `receipts` (`transaction_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_receipts_created_at` ON `receipts` (`created_at`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `split_bills` (`split_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `transaction_id` INTEGER, `user_id` INTEGER NOT NULL, `total_amount` REAL NOT NULL, `description` TEXT NOT NULL, `split_type` TEXT NOT NULL, `created_at` INTEGER NOT NULL, `is_settled` INTEGER NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_split_bills_transaction_id` ON `split_bills` (`transaction_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_split_bills_user_id` ON `split_bills` (`user_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_split_bills_is_settled` ON `split_bills` (`is_settled`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `split_participants` (`participant_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `split_id` INTEGER NOT NULL, `name` TEXT NOT NULL, `phone_number` TEXT, `share_amount` REAL NOT NULL, `is_paid` INTEGER NOT NULL, `paid_at` INTEGER, FOREIGN KEY(`split_id`) REFERENCES `split_bills`(`split_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_split_participants_split_id` ON `split_participants` (`split_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_split_participants_is_paid` ON `split_participants` (`is_paid`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c8a7f214ff53dd49762e9ff6b54104e9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `bank_accounts`");
        db.execSQL("DROP TABLE IF EXISTS `transactions`");
        db.execSQL("DROP TABLE IF EXISTS `budgets`");
        db.execSQL("DROP TABLE IF EXISTS `user_settings`");
        db.execSQL("DROP TABLE IF EXISTS `recurring_transactions`");
        db.execSQL("DROP TABLE IF EXISTS `savings_goals`");
        db.execSQL("DROP TABLE IF EXISTS `goal_contributions`");
        db.execSQL("DROP TABLE IF EXISTS `receipts`");
        db.execSQL("DROP TABLE IF EXISTS `split_bills`");
        db.execSQL("DROP TABLE IF EXISTS `split_participants`");
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
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(8);
        _columnsUsers.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("full_name", new TableInfo.Column("full_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("pin_hash", new TableInfo.Column("pin_hash", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("upi_id", new TableInfo.Column("upi_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("is_active", new TableInfo.Column("is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(1);
        _indicesUsers.add(new TableInfo.Index("index_users_phone", true, Arrays.asList("phone"), Arrays.asList("ASC")));
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.financemanager.app.data.local.entities.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsBankAccounts = new HashMap<String, TableInfo.Column>(12);
        _columnsBankAccounts.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("account_name", new TableInfo.Column("account_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("account_number", new TableInfo.Column("account_number", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("bank_name", new TableInfo.Column("bank_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("ifsc_code", new TableInfo.Column("ifsc_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("account_type", new TableInfo.Column("account_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("balance", new TableInfo.Column("balance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("is_primary", new TableInfo.Column("is_primary", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBankAccounts.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBankAccounts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBankAccounts.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        final HashSet<TableInfo.Index> _indicesBankAccounts = new HashSet<TableInfo.Index>(2);
        _indicesBankAccounts.add(new TableInfo.Index("index_bank_accounts_user_id", false, Arrays.asList("user_id"), Arrays.asList("ASC")));
        _indicesBankAccounts.add(new TableInfo.Index("index_bank_accounts_account_number", true, Arrays.asList("account_number"), Arrays.asList("ASC")));
        final TableInfo _infoBankAccounts = new TableInfo("bank_accounts", _columnsBankAccounts, _foreignKeysBankAccounts, _indicesBankAccounts);
        final TableInfo _existingBankAccounts = TableInfo.read(db, "bank_accounts");
        if (!_infoBankAccounts.equals(_existingBankAccounts)) {
          return new RoomOpenHelper.ValidationResult(false, "bank_accounts(com.financemanager.app.data.local.entities.BankAccountEntity).\n"
                  + " Expected:\n" + _infoBankAccounts + "\n"
                  + " Found:\n" + _existingBankAccounts);
        }
        final HashMap<String, TableInfo.Column> _columnsTransactions = new HashMap<String, TableInfo.Column>(13);
        _columnsTransactions.put("transaction_id", new TableInfo.Column("transaction_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("transaction_type", new TableInfo.Column("transaction_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("receipt_path", new TableInfo.Column("receipt_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("is_recurring", new TableInfo.Column("is_recurring", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransactions.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransactions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        _foreignKeysTransactions.add(new TableInfo.ForeignKey("bank_accounts", "CASCADE", "NO ACTION", Arrays.asList("account_id"), Arrays.asList("account_id")));
        final HashSet<TableInfo.Index> _indicesTransactions = new HashSet<TableInfo.Index>(5);
        _indicesTransactions.add(new TableInfo.Index("index_transactions_user_id", false, Arrays.asList("user_id"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_account_id", false, Arrays.asList("account_id"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_timestamp", false, Arrays.asList("timestamp"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_category", false, Arrays.asList("category"), Arrays.asList("ASC")));
        _indicesTransactions.add(new TableInfo.Index("index_transactions_transaction_type", false, Arrays.asList("transaction_type"), Arrays.asList("ASC")));
        final TableInfo _infoTransactions = new TableInfo("transactions", _columnsTransactions, _foreignKeysTransactions, _indicesTransactions);
        final TableInfo _existingTransactions = TableInfo.read(db, "transactions");
        if (!_infoTransactions.equals(_existingTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "transactions(com.financemanager.app.data.local.entities.TransactionEntity).\n"
                  + " Expected:\n" + _infoTransactions + "\n"
                  + " Found:\n" + _existingTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsBudgets = new HashMap<String, TableInfo.Column>(13);
        _columnsBudgets.put("budget_id", new TableInfo.Column("budget_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("budget_name", new TableInfo.Column("budget_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("total_budget", new TableInfo.Column("total_budget", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("spent_amount", new TableInfo.Column("spent_amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("period_type", new TableInfo.Column("period_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("period_start", new TableInfo.Column("period_start", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("period_end", new TableInfo.Column("period_end", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("category", new TableInfo.Column("category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("alert_threshold", new TableInfo.Column("alert_threshold", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("is_active", new TableInfo.Column("is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBudgets.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBudgets = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBudgets.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        final HashSet<TableInfo.Index> _indicesBudgets = new HashSet<TableInfo.Index>(2);
        _indicesBudgets.add(new TableInfo.Index("index_budgets_user_id", false, Arrays.asList("user_id"), Arrays.asList("ASC")));
        _indicesBudgets.add(new TableInfo.Index("index_budgets_period_start_period_end", false, Arrays.asList("period_start", "period_end"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoBudgets = new TableInfo("budgets", _columnsBudgets, _foreignKeysBudgets, _indicesBudgets);
        final TableInfo _existingBudgets = TableInfo.read(db, "budgets");
        if (!_infoBudgets.equals(_existingBudgets)) {
          return new RoomOpenHelper.ValidationResult(false, "budgets(com.financemanager.app.data.local.entities.BudgetEntity).\n"
                  + " Expected:\n" + _infoBudgets + "\n"
                  + " Found:\n" + _existingBudgets);
        }
        final HashMap<String, TableInfo.Column> _columnsUserSettings = new HashMap<String, TableInfo.Column>(12);
        _columnsUserSettings.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("theme", new TableInfo.Column("theme", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("currency_symbol", new TableInfo.Column("currency_symbol", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("notifications_enabled", new TableInfo.Column("notifications_enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("budget_alerts_enabled", new TableInfo.Column("budget_alerts_enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("transaction_reminders_enabled", new TableInfo.Column("transaction_reminders_enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("biometric_enabled", new TableInfo.Column("biometric_enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("auto_backup_enabled", new TableInfo.Column("auto_backup_enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("last_backup_time", new TableInfo.Column("last_backup_time", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserSettings.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserSettings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysUserSettings.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        final HashSet<TableInfo.Index> _indicesUserSettings = new HashSet<TableInfo.Index>(1);
        _indicesUserSettings.add(new TableInfo.Index("index_user_settings_user_id", false, Arrays.asList("user_id"), Arrays.asList("ASC")));
        final TableInfo _infoUserSettings = new TableInfo("user_settings", _columnsUserSettings, _foreignKeysUserSettings, _indicesUserSettings);
        final TableInfo _existingUserSettings = TableInfo.read(db, "user_settings");
        if (!_infoUserSettings.equals(_existingUserSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "user_settings(com.financemanager.app.data.local.entities.UserSettingsEntity).\n"
                  + " Expected:\n" + _infoUserSettings + "\n"
                  + " Found:\n" + _existingUserSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsRecurringTransactions = new HashMap<String, TableInfo.Column>(15);
        _columnsRecurringTransactions.put("recurring_id", new TableInfo.Column("recurring_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("account_id", new TableInfo.Column("account_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("frequency", new TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("start_date", new TableInfo.Column("start_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("end_date", new TableInfo.Column("end_date", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("next_occurrence", new TableInfo.Column("next_occurrence", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("is_active", new TableInfo.Column("is_active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("last_generated", new TableInfo.Column("last_generated", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecurringTransactions.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecurringTransactions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysRecurringTransactions.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        _foreignKeysRecurringTransactions.add(new TableInfo.ForeignKey("bank_accounts", "CASCADE", "NO ACTION", Arrays.asList("account_id"), Arrays.asList("account_id")));
        final HashSet<TableInfo.Index> _indicesRecurringTransactions = new HashSet<TableInfo.Index>(4);
        _indicesRecurringTransactions.add(new TableInfo.Index("index_recurring_transactions_user_id", false, Arrays.asList("user_id"), Arrays.asList("ASC")));
        _indicesRecurringTransactions.add(new TableInfo.Index("index_recurring_transactions_account_id", false, Arrays.asList("account_id"), Arrays.asList("ASC")));
        _indicesRecurringTransactions.add(new TableInfo.Index("index_recurring_transactions_next_occurrence", false, Arrays.asList("next_occurrence"), Arrays.asList("ASC")));
        _indicesRecurringTransactions.add(new TableInfo.Index("index_recurring_transactions_is_active", false, Arrays.asList("is_active"), Arrays.asList("ASC")));
        final TableInfo _infoRecurringTransactions = new TableInfo("recurring_transactions", _columnsRecurringTransactions, _foreignKeysRecurringTransactions, _indicesRecurringTransactions);
        final TableInfo _existingRecurringTransactions = TableInfo.read(db, "recurring_transactions");
        if (!_infoRecurringTransactions.equals(_existingRecurringTransactions)) {
          return new RoomOpenHelper.ValidationResult(false, "recurring_transactions(com.financemanager.app.data.local.entities.RecurringTransactionEntity).\n"
                  + " Expected:\n" + _infoRecurringTransactions + "\n"
                  + " Found:\n" + _existingRecurringTransactions);
        }
        final HashMap<String, TableInfo.Column> _columnsSavingsGoals = new HashMap<String, TableInfo.Column>(12);
        _columnsSavingsGoals.put("goal_id", new TableInfo.Column("goal_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("target_amount", new TableInfo.Column("target_amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("current_amount", new TableInfo.Column("current_amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("target_date", new TableInfo.Column("target_date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("priority", new TableInfo.Column("priority", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavingsGoals.put("is_archived", new TableInfo.Column("is_archived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSavingsGoals = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSavingsGoals.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        final HashSet<TableInfo.Index> _indicesSavingsGoals = new HashSet<TableInfo.Index>(4);
        _indicesSavingsGoals.add(new TableInfo.Index("index_savings_goals_user_id", false, Arrays.asList("user_id"), Arrays.asList("ASC")));
        _indicesSavingsGoals.add(new TableInfo.Index("index_savings_goals_target_date", false, Arrays.asList("target_date"), Arrays.asList("ASC")));
        _indicesSavingsGoals.add(new TableInfo.Index("index_savings_goals_status", false, Arrays.asList("status"), Arrays.asList("ASC")));
        _indicesSavingsGoals.add(new TableInfo.Index("index_savings_goals_is_archived", false, Arrays.asList("is_archived"), Arrays.asList("ASC")));
        final TableInfo _infoSavingsGoals = new TableInfo("savings_goals", _columnsSavingsGoals, _foreignKeysSavingsGoals, _indicesSavingsGoals);
        final TableInfo _existingSavingsGoals = TableInfo.read(db, "savings_goals");
        if (!_infoSavingsGoals.equals(_existingSavingsGoals)) {
          return new RoomOpenHelper.ValidationResult(false, "savings_goals(com.financemanager.app.data.local.entities.SavingsGoalEntity).\n"
                  + " Expected:\n" + _infoSavingsGoals + "\n"
                  + " Found:\n" + _existingSavingsGoals);
        }
        final HashMap<String, TableInfo.Column> _columnsGoalContributions = new HashMap<String, TableInfo.Column>(6);
        _columnsGoalContributions.put("contribution_id", new TableInfo.Column("contribution_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoalContributions.put("goal_id", new TableInfo.Column("goal_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoalContributions.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoalContributions.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoalContributions.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGoalContributions.put("transaction_id", new TableInfo.Column("transaction_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGoalContributions = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysGoalContributions.add(new TableInfo.ForeignKey("savings_goals", "CASCADE", "NO ACTION", Arrays.asList("goal_id"), Arrays.asList("goal_id")));
        _foreignKeysGoalContributions.add(new TableInfo.ForeignKey("transactions", "SET NULL", "NO ACTION", Arrays.asList("transaction_id"), Arrays.asList("transaction_id")));
        final HashSet<TableInfo.Index> _indicesGoalContributions = new HashSet<TableInfo.Index>(3);
        _indicesGoalContributions.add(new TableInfo.Index("index_goal_contributions_goal_id", false, Arrays.asList("goal_id"), Arrays.asList("ASC")));
        _indicesGoalContributions.add(new TableInfo.Index("index_goal_contributions_timestamp", false, Arrays.asList("timestamp"), Arrays.asList("ASC")));
        _indicesGoalContributions.add(new TableInfo.Index("index_goal_contributions_transaction_id", false, Arrays.asList("transaction_id"), Arrays.asList("ASC")));
        final TableInfo _infoGoalContributions = new TableInfo("goal_contributions", _columnsGoalContributions, _foreignKeysGoalContributions, _indicesGoalContributions);
        final TableInfo _existingGoalContributions = TableInfo.read(db, "goal_contributions");
        if (!_infoGoalContributions.equals(_existingGoalContributions)) {
          return new RoomOpenHelper.ValidationResult(false, "goal_contributions(com.financemanager.app.data.local.entities.GoalContributionEntity).\n"
                  + " Expected:\n" + _infoGoalContributions + "\n"
                  + " Found:\n" + _existingGoalContributions);
        }
        final HashMap<String, TableInfo.Column> _columnsReceipts = new HashMap<String, TableInfo.Column>(6);
        _columnsReceipts.put("receipt_id", new TableInfo.Column("receipt_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("transaction_id", new TableInfo.Column("transaction_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("image_uri", new TableInfo.Column("image_uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("thumbnail_uri", new TableInfo.Column("thumbnail_uri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReceipts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReceipts.add(new TableInfo.ForeignKey("transactions", "CASCADE", "NO ACTION", Arrays.asList("transaction_id"), Arrays.asList("transaction_id")));
        final HashSet<TableInfo.Index> _indicesReceipts = new HashSet<TableInfo.Index>(2);
        _indicesReceipts.add(new TableInfo.Index("index_receipts_transaction_id", false, Arrays.asList("transaction_id"), Arrays.asList("ASC")));
        _indicesReceipts.add(new TableInfo.Index("index_receipts_created_at", false, Arrays.asList("created_at"), Arrays.asList("ASC")));
        final TableInfo _infoReceipts = new TableInfo("receipts", _columnsReceipts, _foreignKeysReceipts, _indicesReceipts);
        final TableInfo _existingReceipts = TableInfo.read(db, "receipts");
        if (!_infoReceipts.equals(_existingReceipts)) {
          return new RoomOpenHelper.ValidationResult(false, "receipts(com.financemanager.app.data.local.entities.ReceiptEntity).\n"
                  + " Expected:\n" + _infoReceipts + "\n"
                  + " Found:\n" + _existingReceipts);
        }
        final HashMap<String, TableInfo.Column> _columnsSplitBills = new HashMap<String, TableInfo.Column>(8);
        _columnsSplitBills.put("split_id", new TableInfo.Column("split_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitBills.put("transaction_id", new TableInfo.Column("transaction_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitBills.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitBills.put("total_amount", new TableInfo.Column("total_amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitBills.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitBills.put("split_type", new TableInfo.Column("split_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitBills.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitBills.put("is_settled", new TableInfo.Column("is_settled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSplitBills = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSplitBills.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        final HashSet<TableInfo.Index> _indicesSplitBills = new HashSet<TableInfo.Index>(3);
        _indicesSplitBills.add(new TableInfo.Index("index_split_bills_transaction_id", false, Arrays.asList("transaction_id"), Arrays.asList("ASC")));
        _indicesSplitBills.add(new TableInfo.Index("index_split_bills_user_id", false, Arrays.asList("user_id"), Arrays.asList("ASC")));
        _indicesSplitBills.add(new TableInfo.Index("index_split_bills_is_settled", false, Arrays.asList("is_settled"), Arrays.asList("ASC")));
        final TableInfo _infoSplitBills = new TableInfo("split_bills", _columnsSplitBills, _foreignKeysSplitBills, _indicesSplitBills);
        final TableInfo _existingSplitBills = TableInfo.read(db, "split_bills");
        if (!_infoSplitBills.equals(_existingSplitBills)) {
          return new RoomOpenHelper.ValidationResult(false, "split_bills(com.financemanager.app.data.local.entities.SplitBillEntity).\n"
                  + " Expected:\n" + _infoSplitBills + "\n"
                  + " Found:\n" + _existingSplitBills);
        }
        final HashMap<String, TableInfo.Column> _columnsSplitParticipants = new HashMap<String, TableInfo.Column>(7);
        _columnsSplitParticipants.put("participant_id", new TableInfo.Column("participant_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitParticipants.put("split_id", new TableInfo.Column("split_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitParticipants.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitParticipants.put("phone_number", new TableInfo.Column("phone_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitParticipants.put("share_amount", new TableInfo.Column("share_amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitParticipants.put("is_paid", new TableInfo.Column("is_paid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSplitParticipants.put("paid_at", new TableInfo.Column("paid_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSplitParticipants = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSplitParticipants.add(new TableInfo.ForeignKey("split_bills", "CASCADE", "NO ACTION", Arrays.asList("split_id"), Arrays.asList("split_id")));
        final HashSet<TableInfo.Index> _indicesSplitParticipants = new HashSet<TableInfo.Index>(2);
        _indicesSplitParticipants.add(new TableInfo.Index("index_split_participants_split_id", false, Arrays.asList("split_id"), Arrays.asList("ASC")));
        _indicesSplitParticipants.add(new TableInfo.Index("index_split_participants_is_paid", false, Arrays.asList("is_paid"), Arrays.asList("ASC")));
        final TableInfo _infoSplitParticipants = new TableInfo("split_participants", _columnsSplitParticipants, _foreignKeysSplitParticipants, _indicesSplitParticipants);
        final TableInfo _existingSplitParticipants = TableInfo.read(db, "split_participants");
        if (!_infoSplitParticipants.equals(_existingSplitParticipants)) {
          return new RoomOpenHelper.ValidationResult(false, "split_participants(com.financemanager.app.data.local.entities.ParticipantEntity).\n"
                  + " Expected:\n" + _infoSplitParticipants + "\n"
                  + " Found:\n" + _existingSplitParticipants);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c8a7f214ff53dd49762e9ff6b54104e9", "bca4e71874fdc8391591fe91849811c7");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","bank_accounts","transactions","budgets","user_settings","recurring_transactions","savings_goals","goal_contributions","receipts","split_bills","split_participants");
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
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `bank_accounts`");
      _db.execSQL("DELETE FROM `transactions`");
      _db.execSQL("DELETE FROM `budgets`");
      _db.execSQL("DELETE FROM `user_settings`");
      _db.execSQL("DELETE FROM `recurring_transactions`");
      _db.execSQL("DELETE FROM `savings_goals`");
      _db.execSQL("DELETE FROM `goal_contributions`");
      _db.execSQL("DELETE FROM `receipts`");
      _db.execSQL("DELETE FROM `split_bills`");
      _db.execSQL("DELETE FROM `split_participants`");
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
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BankAccountDao.class, BankAccountDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TransactionDao.class, TransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BudgetDao.class, BudgetDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserSettingsDao.class, UserSettingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecurringTransactionDao.class, RecurringTransactionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SavingsGoalDao.class, SavingsGoalDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReceiptDao.class, ReceiptDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SplitBillDao.class, SplitBillDao_Impl.getRequiredConverters());
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
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public BankAccountDao bankAccountDao() {
    if (_bankAccountDao != null) {
      return _bankAccountDao;
    } else {
      synchronized(this) {
        if(_bankAccountDao == null) {
          _bankAccountDao = new BankAccountDao_Impl(this);
        }
        return _bankAccountDao;
      }
    }
  }

  @Override
  public TransactionDao transactionDao() {
    if (_transactionDao != null) {
      return _transactionDao;
    } else {
      synchronized(this) {
        if(_transactionDao == null) {
          _transactionDao = new TransactionDao_Impl(this);
        }
        return _transactionDao;
      }
    }
  }

  @Override
  public BudgetDao budgetDao() {
    if (_budgetDao != null) {
      return _budgetDao;
    } else {
      synchronized(this) {
        if(_budgetDao == null) {
          _budgetDao = new BudgetDao_Impl(this);
        }
        return _budgetDao;
      }
    }
  }

  @Override
  public UserSettingsDao userSettingsDao() {
    if (_userSettingsDao != null) {
      return _userSettingsDao;
    } else {
      synchronized(this) {
        if(_userSettingsDao == null) {
          _userSettingsDao = new UserSettingsDao_Impl(this);
        }
        return _userSettingsDao;
      }
    }
  }

  @Override
  public RecurringTransactionDao recurringTransactionDao() {
    if (_recurringTransactionDao != null) {
      return _recurringTransactionDao;
    } else {
      synchronized(this) {
        if(_recurringTransactionDao == null) {
          _recurringTransactionDao = new RecurringTransactionDao_Impl(this);
        }
        return _recurringTransactionDao;
      }
    }
  }

  @Override
  public SavingsGoalDao savingsGoalDao() {
    if (_savingsGoalDao != null) {
      return _savingsGoalDao;
    } else {
      synchronized(this) {
        if(_savingsGoalDao == null) {
          _savingsGoalDao = new SavingsGoalDao_Impl(this);
        }
        return _savingsGoalDao;
      }
    }
  }

  @Override
  public ReceiptDao receiptDao() {
    if (_receiptDao != null) {
      return _receiptDao;
    } else {
      synchronized(this) {
        if(_receiptDao == null) {
          _receiptDao = new ReceiptDao_Impl(this);
        }
        return _receiptDao;
      }
    }
  }

  @Override
  public SplitBillDao splitBillDao() {
    if (_splitBillDao != null) {
      return _splitBillDao;
    } else {
      synchronized(this) {
        if(_splitBillDao == null) {
          _splitBillDao = new SplitBillDao_Impl(this);
        }
        return _splitBillDao;
      }
    }
  }
}

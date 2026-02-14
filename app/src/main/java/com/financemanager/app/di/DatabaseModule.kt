package com.financemanager.app.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.financemanager.app.data.local.dao.BankAccountDao
import com.financemanager.app.data.local.dao.BudgetDao
import com.financemanager.app.data.local.dao.ReceiptDao
import com.financemanager.app.data.local.dao.RecurringTransactionDao
import com.financemanager.app.data.local.dao.SavingsGoalDao
import com.financemanager.app.data.local.dao.SplitBillDao
import com.financemanager.app.data.local.dao.TransactionDao
import com.financemanager.app.data.local.dao.UserDao
import com.financemanager.app.data.local.dao.UserSettingsDao
import com.financemanager.app.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

/**
 * Hilt module for providing database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Migration from version 2 to 3
     * Adds recurring_transactions table
     */
    private val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create recurring_transactions table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS recurring_transactions (
                    recurring_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    user_id INTEGER NOT NULL,
                    account_id INTEGER NOT NULL,
                    amount REAL NOT NULL,
                    type TEXT NOT NULL,
                    category TEXT NOT NULL,
                    description TEXT NOT NULL,
                    frequency TEXT NOT NULL,
                    start_date INTEGER NOT NULL,
                    end_date INTEGER,
                    next_occurrence INTEGER NOT NULL,
                    is_active INTEGER NOT NULL,
                    last_generated INTEGER,
                    created_at INTEGER NOT NULL,
                    updated_at INTEGER NOT NULL,
                    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                    FOREIGN KEY(account_id) REFERENCES bank_accounts(account_id) ON DELETE CASCADE
                )
            """)
            
            // Create indices for performance
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_user_id 
                ON recurring_transactions(user_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_account_id 
                ON recurring_transactions(account_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_next_occurrence 
                ON recurring_transactions(next_occurrence)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_is_active 
                ON recurring_transactions(is_active)
            """)
        }
    }
    
    /**
     * Migration from version 3 to 4
     * Fixes user_id column type from TEXT to INTEGER in recurring_transactions
     */
    private val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create a new table with correct schema
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS recurring_transactions_new (
                    recurring_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    user_id INTEGER NOT NULL,
                    account_id INTEGER NOT NULL,
                    amount REAL NOT NULL,
                    type TEXT NOT NULL,
                    category TEXT NOT NULL,
                    description TEXT NOT NULL,
                    frequency TEXT NOT NULL,
                    start_date INTEGER NOT NULL,
                    end_date INTEGER,
                    next_occurrence INTEGER NOT NULL,
                    is_active INTEGER NOT NULL,
                    last_generated INTEGER,
                    created_at INTEGER NOT NULL,
                    updated_at INTEGER NOT NULL,
                    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                    FOREIGN KEY(account_id) REFERENCES bank_accounts(account_id) ON DELETE CASCADE
                )
            """)
            
            // Copy data from old table to new table (if the old table exists)
            // Note: This might fail if user_id contains non-numeric TEXT values
            // In that case, we'll just skip the data (acceptable for dev phase)
            database.execSQL("""
                INSERT OR IGNORE INTO recurring_transactions_new 
                SELECT recurring_id, CAST(user_id AS INTEGER), account_id, amount, type, 
                       category, description, frequency, start_date, end_date, 
                       next_occurrence, is_active, last_generated, created_at, updated_at
                FROM recurring_transactions
            """)
            
            // Drop old table
            database.execSQL("DROP TABLE IF EXISTS recurring_transactions")
            
            // Rename new table to original name
            database.execSQL("ALTER TABLE recurring_transactions_new RENAME TO recurring_transactions")
            
            // Recreate indices
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_user_id 
                ON recurring_transactions(user_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_account_id 
                ON recurring_transactions(account_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_next_occurrence 
                ON recurring_transactions(next_occurrence)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_is_active 
                ON recurring_transactions(is_active)
            """)
        }
    }
    
    /**
     * Migration from version 4 to 5
     * Adds savings_goals and goal_contributions tables
     */
    private val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create savings_goals table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS savings_goals (
                    goal_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    description TEXT NOT NULL,
                    target_amount REAL NOT NULL,
                    current_amount REAL NOT NULL DEFAULT 0.0,
                    target_date INTEGER NOT NULL,
                    category TEXT NOT NULL,
                    priority TEXT NOT NULL,
                    status TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    is_archived INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
            """)
            
            // Create indices for savings_goals
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_savings_goals_user_id 
                ON savings_goals(user_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_savings_goals_target_date 
                ON savings_goals(target_date)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_savings_goals_status 
                ON savings_goals(status)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_savings_goals_is_archived 
                ON savings_goals(is_archived)
            """)
            
            // Create goal_contributions table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS goal_contributions (
                    contribution_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    goal_id INTEGER NOT NULL,
                    amount REAL NOT NULL,
                    note TEXT NOT NULL,
                    timestamp INTEGER NOT NULL,
                    transaction_id INTEGER,
                    FOREIGN KEY(goal_id) REFERENCES savings_goals(goal_id) ON DELETE CASCADE,
                    FOREIGN KEY(transaction_id) REFERENCES transactions(transaction_id) ON DELETE SET NULL
                )
            """)
            
            // Create indices for goal_contributions
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_goal_contributions_goal_id 
                ON goal_contributions(goal_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_goal_contributions_timestamp 
                ON goal_contributions(timestamp)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_goal_contributions_transaction_id 
                ON goal_contributions(transaction_id)
            """)
        }
    }
    
    /**
     * Migration from version 5 to 6
     * Adds receipts table
     */
    private val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create receipts table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS receipts (
                    receipt_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    transaction_id INTEGER NOT NULL,
                    image_uri TEXT NOT NULL,
                    thumbnail_uri TEXT,
                    note TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    FOREIGN KEY(transaction_id) REFERENCES transactions(transaction_id) ON DELETE CASCADE
                )
            """)
            
            // Create indices for receipts
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_receipts_transaction_id 
                ON receipts(transaction_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_receipts_created_at 
                ON receipts(created_at)
            """)
        }
    }
    
    /**
     * Migration from version 6 to 7
     * Adds split_bills and split_participants tables
     */
    private val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create split_bills table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS split_bills (
                    split_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    transaction_id INTEGER NOT NULL,
                    user_id INTEGER NOT NULL,
                    total_amount REAL NOT NULL,
                    description TEXT NOT NULL,
                    split_type TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    is_settled INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY(transaction_id) REFERENCES transactions(transaction_id) ON DELETE CASCADE,
                    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
            """)
            
            // Create split_participants table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS split_participants (
                    participant_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    split_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    phone_number TEXT,
                    share_amount REAL NOT NULL,
                    is_paid INTEGER NOT NULL DEFAULT 0,
                    paid_at INTEGER,
                    FOREIGN KEY(split_id) REFERENCES split_bills(split_id) ON DELETE CASCADE
                )
            """)
            
            // Create indices
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_split_bills_transaction_id 
                ON split_bills(transaction_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_split_bills_user_id 
                ON split_bills(user_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_split_bills_is_settled 
                ON split_bills(is_settled)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_split_participants_split_id 
                ON split_participants(split_id)
            """)
            
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_split_participants_is_paid 
                ON split_participants(is_paid)
            """)
        }
    }
    
    /**
     * Migration from version 7 to 8
     * Update receipts table structure
     */
    private val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Receipts table updates handled by fallback migration
        }
    }
    
    /**
     * Migration from version 8 to 9
     * Made transactionId nullable in split_bills, removed transaction FK
     */
    private val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Recreate split_bills table with nullable transaction_id
            database.execSQL("DROP TABLE IF EXISTS split_bills")
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS split_bills (
                    split_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    transaction_id INTEGER,
                    user_id INTEGER NOT NULL,
                    total_amount REAL NOT NULL,
                    description TEXT NOT NULL,
                    split_type TEXT NOT NULL,
                    created_at INTEGER NOT NULL,
                    is_settled INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
                )
            """)
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_split_bills_user_id 
                ON split_bills(user_id)
            """)
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_split_bills_is_settled 
                ON split_bills(is_settled)
            """)
        }
    }
    
    /**
     * Migration from version 9 to 10
     * Fix user_id type in recurring_transactions from TEXT to INTEGER
     */
    private val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create new table with correct schema
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS recurring_transactions_new (
                    recurring_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    user_id INTEGER NOT NULL,
                    account_id INTEGER NOT NULL,
                    amount REAL NOT NULL,
                    type TEXT NOT NULL,
                    category TEXT NOT NULL,
                    description TEXT NOT NULL,
                    frequency TEXT NOT NULL,
                    start_date INTEGER NOT NULL,
                    end_date INTEGER,
                    next_occurrence INTEGER NOT NULL,
                    is_active INTEGER NOT NULL DEFAULT 1,
                    last_generated INTEGER,
                    created_at INTEGER NOT NULL,
                    updated_at INTEGER NOT NULL,
                    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                    FOREIGN KEY(account_id) REFERENCES bank_accounts(account_id) ON DELETE CASCADE
                )
            """)
            
            // Copy data with type conversion
            database.execSQL("""
                INSERT INTO recurring_transactions_new 
                SELECT 
                    recurring_id,
                    CAST(user_id AS INTEGER),
                    account_id,
                    amount,
                    type,
                    category,
                    description,
                    frequency,
                    start_date,
                    end_date,
                    next_occurrence,
                    is_active,
                    last_generated,
                    created_at,
                    updated_at
                FROM recurring_transactions
            """)
            
            // Drop old table
            database.execSQL("DROP TABLE recurring_transactions")
            
            // Rename new table
            database.execSQL("ALTER TABLE recurring_transactions_new RENAME TO recurring_transactions")
            
            // Recreate indices
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_user_id 
                ON recurring_transactions(user_id)
            """)
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_account_id 
                ON recurring_transactions(account_id)
            """)
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_next_occurrence 
                ON recurring_transactions(next_occurrence)
            """)
            database.execSQL("""
                CREATE INDEX IF NOT EXISTS index_recurring_transactions_is_active 
                ON recurring_transactions(is_active)
            """)
        }
    }
    
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        // SQLCipher encryption setup
        val passphrase = SQLiteDatabase.getBytes("finance_manager_secure_key_2026".toCharArray())
        val factory = SupportFactory(passphrase)
        
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10)
            .fallbackToDestructiveMigration() // Allow destructive migration for dev phase
            .build()
    }
    
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
    
    @Provides
    fun provideBankAccountDao(database: AppDatabase): BankAccountDao {
        return database.bankAccountDao()
    }
    
    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }
    
    @Provides
    fun provideBudgetDao(database: AppDatabase): BudgetDao {
        return database.budgetDao()
    }
    
    @Provides
    fun provideUserSettingsDao(database: AppDatabase): UserSettingsDao {
        return database.userSettingsDao()
    }
    
    @Provides
    fun provideRecurringTransactionDao(database: AppDatabase): RecurringTransactionDao {
        return database.recurringTransactionDao()
    }
    
    @Provides
    fun provideSavingsGoalDao(database: AppDatabase): SavingsGoalDao {
        return database.savingsGoalDao()
    }
    
    @Provides
    fun provideReceiptDao(database: AppDatabase): ReceiptDao {
        return database.receiptDao()
    }
    
    @Provides
    fun provideSplitBillDao(database: AppDatabase): SplitBillDao {
        return database.splitBillDao()
    }
}

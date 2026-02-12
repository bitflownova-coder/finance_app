package com.financemanager.app.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.financemanager.app.data.local.dao.BankAccountDao
import com.financemanager.app.data.local.dao.BudgetDao
import com.financemanager.app.data.local.dao.ReceiptDao
import com.financemanager.app.data.local.dao.RecurringTransactionDao
import com.financemanager.app.data.local.dao.SavingsGoalDao
import com.financemanager.app.data.local.dao.SplitBillDao
import com.financemanager.app.data.local.dao.TransactionDao
import com.financemanager.app.data.local.dao.UserDao
import com.financemanager.app.data.local.dao.UserSettingsDao
import com.financemanager.app.data.local.entities.BankAccountEntity
import com.financemanager.app.data.local.entities.BudgetEntity
import com.financemanager.app.data.local.entities.GoalContributionEntity
import com.financemanager.app.data.local.entities.ParticipantEntity
import com.financemanager.app.data.local.entities.ReceiptEntity
import com.financemanager.app.data.local.entities.RecurringTransactionEntity
import com.financemanager.app.data.local.entities.SavingsGoalEntity
import com.financemanager.app.data.local.entities.SplitBillEntity
import com.financemanager.app.data.local.entities.TransactionEntity
import com.financemanager.app.data.local.entities.UserEntity
import com.financemanager.app.data.local.entities.UserSettingsEntity

/**
 * Main Room database for Finance Manager app
 * Contains all entities and DAOs
 */
@Database(
    entities = [
        UserEntity::class,
        BankAccountEntity::class,
        TransactionEntity::class,
        BudgetEntity::class,
        UserSettingsEntity::class,
        RecurringTransactionEntity::class,
        SavingsGoalEntity::class,
        GoalContributionEntity::class,
        ReceiptEntity::class,
        SplitBillEntity::class,
        ParticipantEntity::class
    ],
    version = 8,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun bankAccountDao(): BankAccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun receiptDao(): ReceiptDao
    abstract fun splitBillDao(): SplitBillDao
    
    companion object {
        const val DATABASE_NAME = "finance_manager_db"
    }
}

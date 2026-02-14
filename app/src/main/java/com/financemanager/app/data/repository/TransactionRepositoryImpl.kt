package com.financemanager.app.data.repository

import androidx.room.withTransaction
import com.financemanager.app.data.local.dao.BankAccountDao
import com.financemanager.app.data.local.dao.BudgetDao
import com.financemanager.app.data.local.dao.TransactionDao
import com.financemanager.app.data.local.database.AppDatabase
import com.financemanager.app.data.mapper.TransactionMapper
import com.financemanager.app.di.IoDispatcher
import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionType
import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of TransactionRepository
 * Handles transaction operations with automatic balance and budget updates
 */
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val bankAccountDao: BankAccountDao,
    private val budgetDao: BudgetDao,
    private val database: AppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TransactionRepository {
    
    override fun getTransactions(userId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByUserId(userId)
            .map { entities -> entities.map { TransactionMapper.toDomain(it) } }
    }
    
    override fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByAccountId(accountId)
            .map { entities -> entities.map { TransactionMapper.toDomain(it) } }
    }
    
    override fun getTransactionsByDateRange(userId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(userId, startDate, endDate)
            .map { entities -> entities.map { TransactionMapper.toDomain(it) } }
    }
    
    override fun getTransactionsByCategory(userId: Long, category: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(userId, category)
            .map { entities -> entities.map { TransactionMapper.toDomain(it) } }
    }
    
    override fun searchTransactions(userId: Long, query: String): Flow<List<Transaction>> {
        return transactionDao.searchTransactions(userId, query)
            .map { entities -> entities.map { TransactionMapper.toDomain(it) } }
    }
    
    override suspend fun getTransactionById(transactionId: Long): Transaction? = withContext(ioDispatcher) {
        transactionDao.getTransactionById(transactionId)?.let {
            TransactionMapper.toDomain(it)
        }
    }
    
    override suspend fun addTransaction(transaction: Transaction): Long = withContext(ioDispatcher) {
        var transactionId: Long = 0
        
        // Use database transaction to ensure atomicity
        database.withTransaction {
            // Insert transaction
            val entity = TransactionMapper.toEntity(transaction)
            transactionId = transactionDao.insert(entity)
            
            // Update account balance
            when (transaction.transactionType) {
                TransactionType.DEBIT -> {
                    bankAccountDao.decreaseBalance(transaction.accountId, transaction.amount)
                    
                    // Update budget spent amount if applicable
                    val budget = budgetDao.getCategoryBudget(
                        userId = transaction.userId,
                        category = transaction.category.name
                    )
                    budget?.let {
                        budgetDao.increaseSpentAmount(it.budgetId, transaction.amount)
                    }
                }
                TransactionType.CREDIT -> {
                    bankAccountDao.increaseBalance(transaction.accountId, transaction.amount)
                }
            }
        }
        
        transactionId
    }
    
    override suspend fun updateTransaction(transaction: Transaction): Unit = withContext(ioDispatcher) {
        database.withTransaction {
            // Get old transaction to revert balance changes
            val oldTransaction = transactionDao.getTransactionById(transaction.transactionId)
            
            if (oldTransaction != null) {
                // Revert old transaction's balance impact
                when (oldTransaction.transactionType) {
                    "DEBIT" -> {
                        bankAccountDao.increaseBalance(oldTransaction.accountId, oldTransaction.amount)
                        // Revert budget spent
                        val budget = budgetDao.getCategoryBudget(
                            userId = oldTransaction.userId,
                            category = oldTransaction.category
                        )
                        budget?.let {
                            budgetDao.decreaseSpentAmount(it.budgetId, oldTransaction.amount)
                        }
                    }
                    "CREDIT" -> bankAccountDao.decreaseBalance(oldTransaction.accountId, oldTransaction.amount)
                }
                
                // Apply new transaction's balance impact
                when (transaction.transactionType) {
                    TransactionType.DEBIT -> {
                        bankAccountDao.decreaseBalance(transaction.accountId, transaction.amount)
                        // Update budget spent
                        val budget = budgetDao.getCategoryBudget(
                            userId = transaction.userId,
                            category = transaction.category.name
                        )
                        budget?.let {
                            budgetDao.increaseSpentAmount(it.budgetId, transaction.amount)
                        }
                    }
                    TransactionType.CREDIT -> bankAccountDao.increaseBalance(transaction.accountId, transaction.amount)
                }
                
                // Update transaction
                val entity = TransactionMapper.toEntity(transaction)
                transactionDao.update(entity)
            }
        }
    }
    
    override suspend fun deleteTransaction(transactionId: Long): Unit = withContext(ioDispatcher) {
        database.withTransaction {
            val transaction = transactionDao.getTransactionById(transactionId)
            
            if (transaction != null) {
                // Restore balance
                when (transaction.transactionType) {
                    "DEBIT" -> {
                        bankAccountDao.increaseBalance(transaction.accountId, transaction.amount)
                        // Restore budget spent
                        val budget = budgetDao.getCategoryBudget(
                            userId = transaction.userId,
                            category = transaction.category
                        )
                        budget?.let {
                            budgetDao.decreaseSpentAmount(it.budgetId, transaction.amount)
                        }
                    }
                    "CREDIT" -> bankAccountDao.decreaseBalance(transaction.accountId, transaction.amount)
                }
                
                // Delete transaction
                transactionDao.delete(transaction)
            }
        }
    }
    
    override suspend fun getMonthlyExpenses(userId: Long, month: Int, year: Int): Double = withContext(ioDispatcher) {
        val (startDate, endDate) = getMonthDateRange(month, year)
        transactionDao.getTotalExpenses(userId, startDate, endDate) ?: 0.0
    }
    
    override suspend fun getMonthlyIncome(userId: Long, month: Int, year: Int): Double = withContext(ioDispatcher) {
        val (startDate, endDate) = getMonthDateRange(month, year)
        transactionDao.getTotalIncome(userId, startDate, endDate) ?: 0.0
    }
    
    private fun getMonthDateRange(month: Int, year: Int): Pair<Long, Long> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.timeInMillis
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endDate = calendar.timeInMillis
        
        return Pair(startDate, endDate)
    }
}

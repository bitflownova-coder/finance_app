package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Transaction operations
 */
interface TransactionRepository {
    /**
     * Get all transactions for a user
     */
    fun getTransactions(userId: Long): Flow<List<Transaction>>
    
    /**
     * Get transactions for a specific account
     */
    fun getTransactionsByAccount(accountId: Long): Flow<List<Transaction>>
    
    /**
     * Get transactions by date range
     */
    fun getTransactionsByDateRange(userId: Long, startDate: Long, endDate: Long): Flow<List<Transaction>>
    
    /**
     * Get transactions by category
     */
    fun getTransactionsByCategory(userId: Long, category: String): Flow<List<Transaction>>
    
    /**
     * Search transactions
     */
    fun searchTransactions(userId: Long, query: String): Flow<List<Transaction>>
    
    /**
     * Get a single transaction by ID
     */
    suspend fun getTransactionById(transactionId: Long): Transaction?
    
    /**
     * Add a new transaction (includes balance update)
     */
    suspend fun addTransaction(transaction: Transaction): Long
    
    /**
     * Update an existing transaction
     */
    suspend fun updateTransaction(transaction: Transaction)
    
    /**
     * Delete a transaction (restores balance)
     */
    suspend fun deleteTransaction(transactionId: Long)
    
    /**
     * Get monthly expenses
     */
    suspend fun getMonthlyExpenses(userId: Long, month: Int, year: Int): Double
    
    /**
     * Get monthly income
     */
    suspend fun getMonthlyIncome(userId: Long, month: Int, year: Int): Double
}

package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.BankAccount
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Bank Account operations
 */
interface BankAccountRepository {
    /**
     * Get all bank accounts for a user
     */
    fun getAccounts(userId: Long): Flow<List<BankAccount>>
    
    /**
     * Get a single account by ID
     */
    suspend fun getAccountById(accountId: Long): BankAccount?
    
    /**
     * Add a new bank account
     */
    suspend fun addAccount(account: BankAccount): Long
    
    /**
     * Update an existing account
     */
    suspend fun updateAccount(account: BankAccount)
    
    /**
     * Delete an account
     */
    suspend fun deleteAccount(accountId: Long)
    
    /**
     * Get total balance across all accounts
     */
    suspend fun getTotalBalance(userId: Long): Double
    
    /**
     * Set an account as primary
     */
    suspend fun setPrimaryAccount(accountId: Long, userId: Long)
}

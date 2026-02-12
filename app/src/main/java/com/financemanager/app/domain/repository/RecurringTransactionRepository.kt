package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.RecurringTransaction
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Recurring Transaction operations
 */
interface RecurringTransactionRepository {
    
    suspend fun addRecurringTransaction(recurringTransaction: RecurringTransaction): Long
    
    suspend fun updateRecurringTransaction(recurringTransaction: RecurringTransaction)
    
    suspend fun deleteRecurringTransaction(recurringTransaction: RecurringTransaction)
    
    suspend fun getRecurringTransactionById(recurringId: Long): RecurringTransaction?
    
    fun getActiveRecurringTransactions(userId: Long): Flow<List<RecurringTransaction>>
    
    fun getAllRecurringTransactions(userId: Long): Flow<List<RecurringTransaction>>
    
    suspend fun getDueRecurringTransactions(): List<RecurringTransaction>
    
    suspend fun updateNextOccurrence(recurringId: Long, nextOccurrence: Long, lastGenerated: Long)
    
    suspend fun toggleActiveStatus(recurringId: Long, isActive: Boolean)
    
    suspend fun processDueRecurringTransactions(): Int
    
    suspend fun getActiveCount(userId: Long): Int
}

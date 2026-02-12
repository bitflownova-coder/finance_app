package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.RecurringTransactionDao
import com.financemanager.app.data.local.dao.TransactionDao
import com.financemanager.app.data.local.entities.TransactionEntity
import com.financemanager.app.data.mapper.RecurringTransactionMapper
import com.financemanager.app.di.IoDispatcher
import com.financemanager.app.domain.model.RecurringFrequency
import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.repository.RecurringTransactionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

/**
 * Implementation of RecurringTransactionRepository
 */
class RecurringTransactionRepositoryImpl @Inject constructor(
    private val recurringTransactionDao: RecurringTransactionDao,
    private val transactionDao: TransactionDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RecurringTransactionRepository {
    
    override suspend fun addRecurringTransaction(recurringTransaction: RecurringTransaction): Long {
        return withContext(ioDispatcher) {
            val entity = RecurringTransactionMapper.toEntity(recurringTransaction)
            recurringTransactionDao.insert(entity)
        }
    }
    
    override suspend fun updateRecurringTransaction(recurringTransaction: RecurringTransaction) {
        withContext(ioDispatcher) {
            val entity = RecurringTransactionMapper.toEntity(recurringTransaction)
            recurringTransactionDao.update(entity)
        }
    }
    
    override suspend fun deleteRecurringTransaction(recurringTransaction: RecurringTransaction) {
        withContext(ioDispatcher) {
            val entity = RecurringTransactionMapper.toEntity(recurringTransaction)
            recurringTransactionDao.delete(entity)
        }
    }
    
    override suspend fun getRecurringTransactionById(recurringId: Long): RecurringTransaction? {
        return withContext(ioDispatcher) {
            recurringTransactionDao.getById(recurringId)?.let {
                RecurringTransactionMapper.toDomain(it)
            }
        }
    }
    
    override fun getActiveRecurringTransactions(userId: Long): Flow<List<RecurringTransaction>> {
        return recurringTransactionDao.getActiveRecurring(userId)
            .map { entities -> entities.map { RecurringTransactionMapper.toDomain(it) } }
            .flowOn(ioDispatcher)
    }
    
    override fun getAllRecurringTransactions(userId: Long): Flow<List<RecurringTransaction>> {
        return recurringTransactionDao.getAllRecurring(userId)
            .map { entities -> entities.map { RecurringTransactionMapper.toDomain(it) } }
            .flowOn(ioDispatcher)
    }
    
    override suspend fun getDueRecurringTransactions(): List<RecurringTransaction> {
        return withContext(ioDispatcher) {
            recurringTransactionDao.getDueRecurring().map {
                RecurringTransactionMapper.toDomain(it)
            }
        }
    }
    
    override suspend fun updateNextOccurrence(recurringId: Long, nextOccurrence: Long, lastGenerated: Long) {
        withContext(ioDispatcher) {
            recurringTransactionDao.updateNextOccurrence(recurringId, nextOccurrence, lastGenerated)
        }
    }
    
    override suspend fun toggleActiveStatus(recurringId: Long, isActive: Boolean) {
        withContext(ioDispatcher) {
            recurringTransactionDao.updateActiveStatus(recurringId, isActive)
        }
    }
    
    override suspend fun processDueRecurringTransactions(): Int {
        return withContext(ioDispatcher) {
            val currentTime = System.currentTimeMillis()
            val dueRecurring = getDueRecurringTransactions()
            var generatedCount = 0
            
            dueRecurring.forEach { recurring ->
                try {
                    // Check if end date has passed
                    if (recurring.endDate != null && currentTime > recurring.endDate) {
                        toggleActiveStatus(recurring.recurringId, false)
                        return@forEach
                    }
                    
                    // Generate transaction
                    val transaction = TransactionEntity(
                        userId = recurring.userId,
                        accountId = recurring.accountId,
                        amount = recurring.amount,
                        transactionType = recurring.type.name,
                        category = recurring.category.name,
                        description = recurring.description,
                        timestamp = currentTime,
                        isRecurring = true
                    )
                    
                    transactionDao.insert(transaction)
                    
                    // Calculate next occurrence
                    val nextOccurrence = calculateNextOccurrence(recurring.nextOccurrence, recurring.frequency)
                    
                    // Update recurring transaction
                    updateNextOccurrence(recurring.recurringId, nextOccurrence, System.currentTimeMillis())
                    
                    generatedCount++
                } catch (e: Exception) {
                    // Log error but continue processing other recurring transactions
                    e.printStackTrace()
                }
            }
            
            generatedCount
        }
    }
    
    override suspend fun getActiveCount(userId: Long): Int {
        return withContext(ioDispatcher) {
            recurringTransactionDao.getActiveCount(userId)
        }
    }
    
    private fun calculateNextOccurrence(currentOccurrence: Long, frequency: RecurringFrequency): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentOccurrence
        
        when (frequency) {
            RecurringFrequency.DAILY -> calendar.add(Calendar.DAY_OF_MONTH, 1)
            RecurringFrequency.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
            RecurringFrequency.BIWEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 2)
            RecurringFrequency.MONTHLY -> calendar.add(Calendar.MONTH, 1)
            RecurringFrequency.QUARTERLY -> calendar.add(Calendar.MONTH, 3)
            RecurringFrequency.YEARLY -> calendar.add(Calendar.YEAR, 1)
        }
        
        return calendar.timeInMillis
    }
}

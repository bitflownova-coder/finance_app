package com.financemanager.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financemanager.app.data.local.entities.RecurringTransactionEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for Recurring Transaction operations
 */
@Dao
interface RecurringTransactionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recurringTransaction: RecurringTransactionEntity): Long
    
    @Update
    suspend fun update(recurringTransaction: RecurringTransactionEntity)
    
    @Delete
    suspend fun delete(recurringTransaction: RecurringTransactionEntity)
    
    @Query("SELECT * FROM recurring_transactions WHERE recurring_id = :recurringId")
    suspend fun getById(recurringId: Long): RecurringTransactionEntity?
    
    @Query("SELECT * FROM recurring_transactions WHERE user_id = :userId AND is_active = 1 ORDER BY next_occurrence ASC")
    fun getActiveRecurring(userId: Long): Flow<List<RecurringTransactionEntity>>
    
    @Query("SELECT * FROM recurring_transactions WHERE user_id = :userId ORDER BY created_at DESC")
    fun getAllRecurring(userId: Long): Flow<List<RecurringTransactionEntity>>
    
    @Query("SELECT * FROM recurring_transactions WHERE next_occurrence <= :currentTime AND is_active = 1")
    suspend fun getDueRecurring(currentTime: Long = System.currentTimeMillis()): List<RecurringTransactionEntity>
    
    @Query("UPDATE recurring_transactions SET next_occurrence = :nextOccurrence, last_generated = :lastGenerated, updated_at = :updatedAt WHERE recurring_id = :recurringId")
    suspend fun updateNextOccurrence(recurringId: Long, nextOccurrence: Long, lastGenerated: Long, updatedAt: Long = System.currentTimeMillis())
    
    @Query("UPDATE recurring_transactions SET is_active = :isActive, updated_at = :updatedAt WHERE recurring_id = :recurringId")
    suspend fun updateActiveStatus(recurringId: Long, isActive: Boolean, updatedAt: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM recurring_transactions WHERE user_id = :userId")
    suspend fun deleteAllByUserId(userId: Long)
    
    @Query("SELECT COUNT(*) FROM recurring_transactions WHERE user_id = :userId AND is_active = 1")
    suspend fun getActiveCount(userId: Long): Int
}

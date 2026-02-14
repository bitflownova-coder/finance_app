package com.financemanager.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financemanager.app.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

// Data class for category-wise expenses result
data class CategoryExpense(
    val category: String,
    val total: Double
)

/**
 * Data Access Object for Transaction operations
 */
@Dao
interface TransactionDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long
    
    @Update
    suspend fun update(transaction: TransactionEntity)
    
    @Delete
    suspend fun delete(transaction: TransactionEntity)
    
    @Query("SELECT * FROM transactions WHERE transaction_id = :transactionId")
    suspend fun getTransactionById(transactionId: Long): TransactionEntity?
    
    @Query("SELECT * FROM transactions WHERE user_id = :userId ORDER BY timestamp DESC")
    fun getTransactionsByUserId(userId: Long): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE user_id = :userId ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentTransactions(userId: Long, limit: Int): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE user_id = :userId ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
    suspend fun getTransactionsPaged(userId: Long, limit: Int, offset: Int): List<TransactionEntity>
    
    @Query("SELECT * FROM transactions WHERE account_id = :accountId ORDER BY timestamp DESC")
    fun getTransactionsByAccountId(accountId: Long): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE user_id = :userId AND category = :category ORDER BY timestamp DESC")
    fun getTransactionsByCategory(userId: Long, category: String): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE user_id = :userId AND timestamp BETWEEN :startDate AND :endDate ORDER BY timestamp DESC")
    fun getTransactionsByDateRange(userId: Long, startDate: Long, endDate: Long): Flow<List<TransactionEntity>>
    
    @Query("SELECT * FROM transactions WHERE user_id = :userId AND description LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    fun searchTransactions(userId: Long, searchQuery: String): Flow<List<TransactionEntity>>
    
    @Query("SELECT SUM(amount) FROM transactions WHERE user_id = :userId AND transaction_type = 'DEBIT' AND timestamp BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpenses(userId: Long, startDate: Long, endDate: Long): Double?
    
    @Query("SELECT SUM(amount) FROM transactions WHERE user_id = :userId AND transaction_type = 'CREDIT' AND timestamp BETWEEN :startDate AND :endDate")
    suspend fun getTotalIncome(userId: Long, startDate: Long, endDate: Long): Double?
    
    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE user_id = :userId AND transaction_type = 'DEBIT' AND timestamp BETWEEN :startDate AND :endDate GROUP BY category ORDER BY total DESC")
    suspend fun getCategoryWiseExpenses(userId: Long, startDate: Long, endDate: Long): List<CategoryExpense>
    
    @Query("DELETE FROM transactions WHERE user_id = :userId")
    suspend fun deleteAllByUserId(userId: Long)
}

package com.financemanager.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financemanager.app.data.local.entities.BudgetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Budget operations
 */
@Dao
interface BudgetDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: BudgetEntity): Long
    
    @Update
    suspend fun update(budget: BudgetEntity)
    
    @Delete
    suspend fun delete(budget: BudgetEntity)
    
    @Query("SELECT * FROM budgets WHERE budget_id = :budgetId")
    suspend fun getBudgetById(budgetId: Long): BudgetEntity?
    
    @Query("SELECT * FROM budgets WHERE user_id = :userId AND is_active = 1 ORDER BY created_at DESC")
    fun getActiveBudgets(userId: Long): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE user_id = :userId AND period_start <= :currentTime AND period_end >= :currentTime AND is_active = 1")
    fun getCurrentBudgets(userId: Long, currentTime: Long = System.currentTimeMillis()): Flow<List<BudgetEntity>>
    
    @Query("SELECT * FROM budgets WHERE user_id = :userId AND category = :category AND period_start <= :currentTime AND period_end >= :currentTime AND is_active = 1 LIMIT 1")
    suspend fun getCategoryBudget(userId: Long, category: String, currentTime: Long = System.currentTimeMillis()): BudgetEntity?
    
    @Query("UPDATE budgets SET spent_amount = spent_amount + :amount, updated_at = :timestamp WHERE budget_id = :budgetId")
    suspend fun increaseSpentAmount(budgetId: Long, amount: Double, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE budgets SET spent_amount = spent_amount - :amount, updated_at = :timestamp WHERE budget_id = :budgetId")
    suspend fun decreaseSpentAmount(budgetId: Long, amount: Double, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE budgets SET spent_amount = 0, updated_at = :timestamp WHERE budget_id = :budgetId")
    suspend fun resetSpentAmount(budgetId: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE budgets SET is_active = 0 WHERE period_end < :currentTime")
    suspend fun deactivateExpiredBudgets(currentTime: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM budgets WHERE user_id = :userId")
    suspend fun deleteAllByUserId(userId: Long)
}

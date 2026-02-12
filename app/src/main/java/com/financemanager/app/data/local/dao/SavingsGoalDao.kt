package com.financemanager.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.financemanager.app.data.local.entities.GoalContributionEntity
import com.financemanager.app.data.local.entities.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsGoalDao {
    
    // ==================== Goals CRUD ====================
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoalEntity): Long
    
    @Update
    suspend fun updateGoal(goal: SavingsGoalEntity)
    
    @Delete
    suspend fun deleteGoal(goal: SavingsGoalEntity)
    
    @Query("SELECT * FROM savings_goals WHERE goal_id = :goalId")
    suspend fun getGoalById(goalId: Long): SavingsGoalEntity?
    
    @Query("SELECT * FROM savings_goals WHERE goal_id = :goalId")
    fun getGoalByIdFlow(goalId: Long): Flow<SavingsGoalEntity?>
    
    // ==================== Get Goals ====================
    
    @Query("""
        SELECT * FROM savings_goals 
        WHERE user_id = :userId AND is_archived = 0
        ORDER BY 
            CASE priority 
                WHEN 'HIGH' THEN 1
                WHEN 'MEDIUM' THEN 2
                WHEN 'LOW' THEN 3
            END,
            target_date ASC
    """)
    fun getActiveGoals(userId: Long): Flow<List<SavingsGoalEntity>>
    
    @Query("""
        SELECT * FROM savings_goals 
        WHERE user_id = :userId AND status = :status AND is_archived = 0
        ORDER BY target_date ASC
    """)
    fun getGoalsByStatus(userId: Long, status: String): Flow<List<SavingsGoalEntity>>
    
    @Query("""
        SELECT * FROM savings_goals 
        WHERE user_id = :userId AND is_archived = 1
        ORDER BY created_at DESC
    """)
    fun getArchivedGoals(userId: Long): Flow<List<SavingsGoalEntity>>
    
    @Query("""
        SELECT * FROM savings_goals 
        WHERE user_id = :userId AND category = :category AND is_archived = 0
        ORDER BY created_at DESC
    """)
    fun getGoalsByCategory(userId: Long, category: String): Flow<List<SavingsGoalEntity>>
    
    // ==================== Statistics ====================
    
    @Query("""
        SELECT SUM(target_amount) FROM savings_goals 
        WHERE user_id = :userId AND status = 'ACTIVE' AND is_archived = 0
    """)
    suspend fun getTotalTargetAmount(userId: Long): Double?
    
    @Query("""
        SELECT SUM(current_amount) FROM savings_goals 
        WHERE user_id = :userId AND status = 'ACTIVE' AND is_archived = 0
    """)
    suspend fun getTotalSavedAmount(userId: Long): Double?
    
    @Query("""
        SELECT COUNT(*) FROM savings_goals 
        WHERE user_id = :userId AND status = 'COMPLETED'
    """)
    suspend fun getCompletedGoalsCount(userId: Long): Int
    
    @Query("""
        SELECT COUNT(*) FROM savings_goals 
        WHERE user_id = :userId AND status = 'ACTIVE' AND is_archived = 0
    """)
    suspend fun getActiveGoalsCount(userId: Long): Int
    
    // ==================== Contributions ====================
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContribution(contribution: GoalContributionEntity): Long
    
    @Delete
    suspend fun deleteContribution(contribution: GoalContributionEntity)
    
    @Query("SELECT * FROM goal_contributions WHERE goal_id = :goalId ORDER BY timestamp DESC")
    fun getContributionsByGoalId(goalId: Long): Flow<List<GoalContributionEntity>>
    
    @Query("SELECT SUM(amount) FROM goal_contributions WHERE goal_id = :goalId")
    suspend fun getTotalContributions(goalId: Long): Double?
    
    // ==================== Complex Operations ====================
    
    @Transaction
    suspend fun addContributionAndUpdateGoal(goalId: Long, amount: Double, note: String) {
        // Insert contribution
        val contribution = GoalContributionEntity(
            goalId = goalId,
            amount = amount,
            note = note
        )
        insertContribution(contribution)
        
        // Update goal current amount
        val goal = getGoalById(goalId)
        if (goal != null) {
            val updatedGoal = goal.copy(
                currentAmount = goal.currentAmount + amount,
                status = if (goal.currentAmount + amount >= goal.targetAmount) "COMPLETED" else goal.status
            )
            updateGoal(updatedGoal)
        }
    }
    
    @Query("UPDATE savings_goals SET status = :status WHERE goal_id = :goalId")
    suspend fun updateGoalStatus(goalId: Long, status: String)
    
    @Query("UPDATE savings_goals SET is_archived = 1 WHERE goal_id = :goalId")
    suspend fun archiveGoal(goalId: Long)
    
    @Query("UPDATE savings_goals SET is_archived = 0 WHERE goal_id = :goalId")
    suspend fun unarchiveGoal(goalId: Long)
    
    @Query("""
        UPDATE savings_goals 
        SET status = 'COMPLETED' 
        WHERE current_amount >= target_amount AND status = 'ACTIVE'
    """)
    suspend fun markCompletedGoals(): Int
}

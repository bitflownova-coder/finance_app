package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.GoalContribution
import com.financemanager.app.domain.model.GoalStatus
import com.financemanager.app.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

interface SavingsGoalRepository {
    
    // Goals CRUD
    suspend fun insertGoal(goal: SavingsGoal): Long
    suspend fun updateGoal(goal: SavingsGoal)
    suspend fun deleteGoal(goal: SavingsGoal)
    suspend fun getGoalById(goalId: Long): SavingsGoal?
    fun getGoalByIdFlow(goalId: Long): Flow<SavingsGoal?>
    
    // Get goals
    fun getActiveGoals(userId: Long): Flow<List<SavingsGoal>>
    fun getGoalsByStatus(userId: Long, status: GoalStatus): Flow<List<SavingsGoal>>
    fun getArchivedGoals(userId: Long): Flow<List<SavingsGoal>>
    fun getGoalsByCategory(userId: Long, category: String): Flow<List<SavingsGoal>>
    
    // Statistics
    suspend fun getTotalTargetAmount(userId: Long): Double
    suspend fun getTotalSavedAmount(userId: Long): Double
    suspend fun getCompletedGoalsCount(userId: Long): Int
    suspend fun getActiveGoalsCount(userId: Long): Int
    
    // Contributions
    suspend fun addContribution(goalId: Long, amount: Double, note: String, transactionId: Long? = null)
    suspend fun deleteContribution(contribution: GoalContribution)
    fun getContributionsByGoalId(goalId: Long): Flow<List<GoalContribution>>
    
    // Goal operations
    suspend fun archiveGoal(goalId: Long)
    suspend fun unarchiveGoal(goalId: Long)
    suspend fun updateGoalStatus(goalId: Long, status: GoalStatus)
    suspend fun markCompletedGoals()
}

package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.Budget
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Budget operations
 */
interface BudgetRepository {
    
    /**
     * Add a new budget
     */
    suspend fun addBudget(budget: Budget): Long
    
    /**
     * Update an existing budget
     */
    suspend fun updateBudget(budget: Budget)
    
    /**
     * Delete a budget
     */
    suspend fun deleteBudget(budget: Budget)
    
    /**
     * Get budget by ID
     */
    suspend fun getBudgetById(budgetId: Long): Budget?
    
    /**
     * Get all active budgets for a user
     */
    fun getActiveBudgets(userId: Long): Flow<List<Budget>>
    
    /**
     * Get current active budgets for a user (within date range)
     */
    fun getCurrentBudgets(userId: Long): Flow<List<Budget>>
    
    /**
     * Get budget for a specific category
     */
    suspend fun getCategoryBudget(userId: Long, categoryName: String): Budget?
    
    /**
     * Increase spent amount for a budget
     */
    suspend fun increaseSpentAmount(budgetId: Long, amount: Double)
    
    /**
     * Decrease spent amount for a budget
     */
    suspend fun decreaseSpentAmount(budgetId: Long, amount: Double)
    
    /**
     * Reset spent amount for a budget
     */
    suspend fun resetSpentAmount(budgetId: Long)
    
    /**
     * Deactivate budgets that have expired
     */
    suspend fun deactivateExpiredBudgets()
}

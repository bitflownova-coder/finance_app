package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.BudgetDao
import com.financemanager.app.data.mapper.BudgetMapper
import com.financemanager.app.domain.model.Budget
import com.financemanager.app.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of BudgetRepository
 * Handles data operations for budgets
 */
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {
    
    override suspend fun addBudget(budget: Budget): Long {
        val entity = BudgetMapper.toEntity(budget)
        return budgetDao.insert(entity)
    }
    
    override suspend fun updateBudget(budget: Budget) {
        val entity = BudgetMapper.toEntity(budget)
        budgetDao.update(entity)
    }
    
    override suspend fun deleteBudget(budget: Budget) {
        val entity = BudgetMapper.toEntity(budget)
        budgetDao.delete(entity)
    }
    
    override suspend fun getBudgetById(budgetId: Long): Budget? {
        return budgetDao.getBudgetById(budgetId)?.let { entity ->
            BudgetMapper.toDomain(entity)
        }
    }
    
    override fun getActiveBudgets(userId: Long): Flow<List<Budget>> {
        return budgetDao.getActiveBudgets(userId).map { entities ->
            entities.map { BudgetMapper.toDomain(it) }
        }
    }
    
    override fun getCurrentBudgets(userId: Long): Flow<List<Budget>> {
        return budgetDao.getCurrentBudgets(userId).map { entities ->
            entities.map { BudgetMapper.toDomain(it) }
        }
    }
    
    override suspend fun getCategoryBudget(userId: Long, categoryName: String): Budget? {
        return budgetDao.getCategoryBudget(userId, categoryName)?.let { entity ->
            BudgetMapper.toDomain(entity)
        }
    }
    
    override suspend fun increaseSpentAmount(budgetId: Long, amount: Double) {
        budgetDao.increaseSpentAmount(budgetId, amount)
    }
    
    override suspend fun decreaseSpentAmount(budgetId: Long, amount: Double) {
        budgetDao.decreaseSpentAmount(budgetId, amount)
    }
    
    override suspend fun resetSpentAmount(budgetId: Long) {
        budgetDao.resetSpentAmount(budgetId)
    }
    
    override suspend fun deactivateExpiredBudgets() {
        budgetDao.deactivateExpiredBudgets()
    }
}

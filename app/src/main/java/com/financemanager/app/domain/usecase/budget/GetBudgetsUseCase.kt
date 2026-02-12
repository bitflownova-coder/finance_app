package com.financemanager.app.domain.usecase.budget

import com.financemanager.app.domain.model.Budget
import com.financemanager.app.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting budgets
 */
class GetBudgetsUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(userId: Long): Flow<List<Budget>> {
        return budgetRepository.getActiveBudgets(userId)
    }
    
    fun getCurrentBudgets(userId: Long): Flow<List<Budget>> {
        return budgetRepository.getCurrentBudgets(userId)
    }
}

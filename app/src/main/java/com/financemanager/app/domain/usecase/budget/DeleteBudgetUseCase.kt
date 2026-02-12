package com.financemanager.app.domain.usecase.budget

import com.financemanager.app.domain.model.Budget
import com.financemanager.app.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Use case for deleting a budget
 */
class DeleteBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget) {
        budgetRepository.deleteBudget(budget)
    }
}

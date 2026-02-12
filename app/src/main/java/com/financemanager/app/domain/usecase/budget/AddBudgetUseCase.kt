package com.financemanager.app.domain.usecase.budget

import com.financemanager.app.domain.model.Budget
import com.financemanager.app.domain.repository.BudgetRepository
import javax.inject.Inject

/**
 * Use case for adding a new budget
 */
class AddBudgetUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget): Long {
        require(budget.budgetName.isNotBlank()) { "Budget name cannot be empty" }
        require(budget.totalBudget > 0) { "Budget amount must be greater than 0" }
        require(budget.periodStart < budget.periodEnd) { "Invalid period dates" }
        
        return budgetRepository.addBudget(budget)
    }
}

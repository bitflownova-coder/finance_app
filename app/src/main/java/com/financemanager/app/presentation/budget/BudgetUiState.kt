package com.financemanager.app.presentation.budget

import com.financemanager.app.domain.model.Budget

/**
 * UI State for Budget screen
 */
data class BudgetUiState(
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Events for Budget screen
 */
sealed class BudgetEvent {
    object LoadBudgets : BudgetEvent()
    data class AddBudget(val budget: Budget) : BudgetEvent()
    data class UpdateBudget(val budget: Budget) : BudgetEvent()
    data class DeleteBudget(val budget: Budget) : BudgetEvent()
}

package com.financemanager.app.domain.usecase.budget

import com.financemanager.app.domain.model.Budget
import javax.inject.Inject

/**
 * Use case for checking budget status
 * Returns alerts if budget is near limit or exceeded
 */
class CheckBudgetStatusUseCase @Inject constructor() {
    
    data class BudgetStatus(
        val budget: Budget,
        val status: Status
    )
    
    enum class Status {
        OK,           // Spending is normal
        NEAR_LIMIT,   // Spending is near the alert threshold
        EXCEEDED      // Budget has been exceeded
    }
    
    operator fun invoke(budgets: List<Budget>): List<BudgetStatus> {
        return budgets.map { budget ->
            val status = when {
                budget.isExceeded -> Status.EXCEEDED
                budget.isNearLimit -> Status.NEAR_LIMIT
                else -> Status.OK
            }
            BudgetStatus(budget, status)
        }
    }
}

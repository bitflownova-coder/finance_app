package com.financemanager.app.domain.usecase.insights

import com.financemanager.app.domain.repository.InsightRepository
import javax.inject.Inject

/**
 * Use case to predict next month's expenses
 */
class PredictExpensesUseCase @Inject constructor(
    private val repository: InsightRepository
) {
    suspend operator fun invoke(userId: Long): Double {
        return repository.predictNextMonthExpense(userId)
    }
}

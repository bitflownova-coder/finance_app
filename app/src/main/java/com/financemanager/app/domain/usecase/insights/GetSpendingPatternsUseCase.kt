package com.financemanager.app.domain.usecase.insights

import com.financemanager.app.domain.model.SpendingPattern
import com.financemanager.app.domain.repository.InsightRepository
import javax.inject.Inject

/**
 * Use case to get spending patterns
 */
class GetSpendingPatternsUseCase @Inject constructor(
    private val repository: InsightRepository
) {
    suspend operator fun invoke(userId: Long, startDate: Long, endDate: Long): List<SpendingPattern> {
        return repository.getSpendingPatterns(userId, startDate, endDate)
    }
}

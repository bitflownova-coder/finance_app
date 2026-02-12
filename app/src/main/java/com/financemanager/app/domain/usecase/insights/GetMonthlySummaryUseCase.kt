package com.financemanager.app.domain.usecase.insights

import com.financemanager.app.domain.model.MonthlySummary
import com.financemanager.app.domain.repository.InsightRepository
import javax.inject.Inject

/**
 * Use case to get monthly summary
 */
class GetMonthlySummaryUseCase @Inject constructor(
    private val repository: InsightRepository
) {
    suspend operator fun invoke(userId: Long, month: Int, year: Int): MonthlySummary {
        return repository.getMonthlySummary(userId, month, year)
    }
}

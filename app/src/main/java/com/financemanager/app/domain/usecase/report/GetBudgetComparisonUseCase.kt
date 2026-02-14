package com.financemanager.app.domain.usecase.report

import com.financemanager.app.domain.model.BudgetComparison
import com.financemanager.app.domain.model.DateRange
import com.financemanager.app.domain.repository.ReportRepository
import javax.inject.Inject

/**
 * Use case for getting budget comparison
 */
class GetBudgetComparisonUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(userId: Long, dateRange: DateRange): List<BudgetComparison> {
        require(dateRange.startDate < dateRange.endDate) { "Invalid date range" }
        return reportRepository.getBudgetComparison(userId, dateRange)
    }
}

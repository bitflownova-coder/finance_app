package com.financemanager.app.domain.usecase.report

import com.financemanager.app.domain.model.DateRange
import com.financemanager.app.domain.model.TransactionSummary
import com.financemanager.app.domain.repository.ReportRepository
import javax.inject.Inject

/**
 * Use case for getting transaction summary
 */
class GetTransactionSummaryUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(userId: Long, dateRange: DateRange): TransactionSummary {
        require(dateRange.startDate < dateRange.endDate) { "Invalid date range" }
        return reportRepository.getTransactionSummary(userId, dateRange)
    }
}

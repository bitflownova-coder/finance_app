package com.financemanager.app.domain.usecase.report

import com.financemanager.app.domain.model.DateRange
import com.financemanager.app.domain.model.ReportType
import com.financemanager.app.domain.repository.ReportRepository
import java.io.File
import javax.inject.Inject

/**
 * Use case for generating PDF reports
 */
class GeneratePdfReportUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    suspend operator fun invoke(
        userId: Long,
        reportType: ReportType,
        dateRange: DateRange,
        fileName: String = "report_${System.currentTimeMillis()}.pdf"
    ): File {
        require(dateRange.startDate < dateRange.endDate) { "Invalid date range" }
        require(fileName.isNotBlank()) { "File name cannot be blank" }
        
        return reportRepository.generatePdfReport(userId, reportType, dateRange, fileName)
    }
}

package com.financemanager.app.presentation.reports

import com.financemanager.app.domain.model.*
import java.io.File

/**
 * UI State for Reports screen
 */
data class ReportsUiState(
    val isLoading: Boolean = false,
    val summary: TransactionSummary? = null,
    val categoryBreakdown: List<CategorySummary> = emptyList(),
    val budgetComparison: List<BudgetComparison> = emptyList(),
    val selectedReportType: ReportType = ReportType.TRANSACTION_SUMMARY,
    val selectedDateRange: DateRange = DateRange.thisMonth(),
    val selectedFileFormat: FileFormat = FileFormat.PDF,
    val generatedFile: File? = null,
    val error: String? = null
)

/**
 * Events for Reports screen
 */
sealed class ReportEvent {
    data class SelectReportType(val reportType: ReportType) : ReportEvent()
    data class SelectDateRange(val dateRange: DateRange) : ReportEvent()
    data class SelectFileFormat(val fileFormat: FileFormat) : ReportEvent()
    object GenerateReport : ReportEvent()
    object ShareReport : ReportEvent()
    object LoadSummary : ReportEvent()
}

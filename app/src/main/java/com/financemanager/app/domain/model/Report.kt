package com.financemanager.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model for Report metadata
 */
@Parcelize
data class Report(
    val reportId: String,
    val reportName: String,
    val reportType: ReportType,
    val dateRange: DateRange,
    val fileFormat: FileFormat,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

/**
 * Types of reports available
 */
enum class ReportType {
    TRANSACTION_SUMMARY,
    CATEGORY_BREAKDOWN,
    BUDGET_VS_ACTUAL,
    MONTHLY_SUMMARY,
    YEARLY_SUMMARY,
    CUSTOM;
    
    val displayName: String
        get() = when (this) {
            TRANSACTION_SUMMARY -> "Transaction Summary"
            CATEGORY_BREAKDOWN -> "Category Breakdown"
            BUDGET_VS_ACTUAL -> "Budget vs Actual"
            MONTHLY_SUMMARY -> "Monthly Summary"
            YEARLY_SUMMARY -> "Yearly Summary"
            CUSTOM -> "Custom Report"
        }
}

/**
 * File formats for export
 */
enum class FileFormat {
    PDF,
    CSV;
    
    val extension: String
        get() = when (this) {
            PDF -> "pdf"
            CSV -> "csv"
        }
    
    val mimeType: String
        get() = when (this) {
            PDF -> "application/pdf"
            CSV -> "text/csv"
        }
}

/**
 * Date range for report generation
 */
@Parcelize
data class DateRange(
    val startDate: Long,
    val endDate: Long,
    val label: String = ""
) : Parcelable {
    
    companion object {
        fun thisMonth(): DateRange {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            val startDate = calendar.timeInMillis
            
            calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
            calendar.set(java.util.Calendar.MINUTE, 59)
            calendar.set(java.util.Calendar.SECOND, 59)
            val endDate = calendar.timeInMillis
            
            return DateRange(startDate, endDate, "This Month")
        }
        
        fun lastMonth(): DateRange {
            val calendar = java.util.Calendar.getInstance()
            calendar.add(java.util.Calendar.MONTH, -1)
            calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            val startDate = calendar.timeInMillis
            
            calendar.set(java.util.Calendar.DAY_OF_MONTH, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
            calendar.set(java.util.Calendar.MINUTE, 59)
            calendar.set(java.util.Calendar.SECOND, 59)
            val endDate = calendar.timeInMillis
            
            return DateRange(startDate, endDate, "Last Month")
        }
        
        fun thisYear(): DateRange {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.MONTH, 0)
            calendar.set(java.util.Calendar.DAY_OF_MONTH, 1)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            val startDate = calendar.timeInMillis
            
            calendar.set(java.util.Calendar.MONTH, 11)
            calendar.set(java.util.Calendar.DAY_OF_MONTH, 31)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
            calendar.set(java.util.Calendar.MINUTE, 59)
            calendar.set(java.util.Calendar.SECOND, 59)
            val endDate = calendar.timeInMillis
            
            return DateRange(startDate, endDate, "This Year")
        }
        
        fun custom(startDate: Long, endDate: Long): DateRange {
            return DateRange(startDate, endDate, "Custom Range")
        }
    }
}

/**
 * Transaction summary data for reports
 */
data class TransactionSummary(
    val totalIncome: Double,
    val totalExpenses: Double,
    val netAmount: Double,
    val transactionCount: Int,
    val averageTransaction: Double,
    val categoryBreakdown: Map<TransactionCategory, CategorySummary>,
    val dateRange: DateRange
)

/**
 * Category-wise summary
 */
data class CategorySummary(
    val category: TransactionCategory,
    val totalAmount: Double,
    val transactionCount: Int,
    val percentage: Double,
    val averageAmount: Double
)

/**
 * Budget vs Actual comparison
 */
data class BudgetComparison(
    val budget: Budget,
    val actualSpent: Double,
    val difference: Double,
    val percentageUsed: Double,
    val status: BudgetStatus
)

enum class BudgetStatus {
    UNDER_BUDGET,
    ON_BUDGET,
    OVER_BUDGET;
    
    val displayName: String
        get() = when (this) {
            UNDER_BUDGET -> "Under Budget"
            ON_BUDGET -> "On Budget"
            OVER_BUDGET -> "Over Budget"
        }
}

package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.*
import java.io.File

/**
 * Repository interface for Report operations
 */
interface ReportRepository {
    
    /**
     * Get transaction summary for a date range
     */
    suspend fun getTransactionSummary(userId: Long, dateRange: DateRange): TransactionSummary
    
    /**
     * Get category breakdown for a date range
     */
    suspend fun getCategoryBreakdown(userId: Long, dateRange: DateRange): List<CategorySummary>
    
    /**
     * Get budget vs actual comparison
     */
    suspend fun getBudgetComparison(userId: Long, dateRange: DateRange): List<BudgetComparison>
    
    /**
     * Generate PDF report
     */
    suspend fun generatePdfReport(
        userId: Long,
        reportType: ReportType,
        dateRange: DateRange,
        fileName: String
    ): File
    
    /**
     * Generate CSV report
     */
    suspend fun generateCsvReport(
        userId: Long,
        reportType: ReportType,
        dateRange: DateRange,
        fileName: String
    ): File
}

package com.financemanager.app.util

import android.content.Context
import com.financemanager.app.domain.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileWriter
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Utility class for generating CSV reports
 */
class CsvGenerator @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance()
    
    /**
     * Generate transaction summary CSV
     */
    fun generateTransactionCsv(
        transactions: List<Transaction>,
        fileName: String
    ): File {
        val file = File(context.cacheDir, fileName)
        
        FileWriter(file).use { writer ->
            // Write header
            writer.append("Date,Description,Category,Type,Amount,Account\n")
            
            // Write data rows
            for (transaction in transactions) {
                writer.append("\"${dateFormat.format(Date(transaction.timestamp))}\",")
                writer.append("\"${escapeCsv(transaction.description)}\",")
                writer.append("\"${transaction.category.displayName}\",")
                writer.append("\"${transaction.transactionType.name}\",")
                writer.append("\"${transaction.amount}\",")
                writer.append("\"Account ${transaction.accountId}\"\n")
            }
        }
        
        return file
    }
    
    /**
     * Generate category breakdown CSV
     */
    fun generateCategoryBreakdownCsv(
        categorySummaries: List<CategorySummary>,
        fileName: String
    ): File {
        val file = File(context.cacheDir, fileName)
        
        FileWriter(file).use { writer ->
            // Write header
            writer.append("Category,Total Amount,Transaction Count,Percentage,Average Amount\n")
            
            // Write data rows
            for (summary in categorySummaries) {
                writer.append("\"${summary.category.displayName}\",")
                writer.append("\"${summary.totalAmount}\",")
                writer.append("\"${summary.transactionCount}\",")
                writer.append("\"${String.format("%.2f", summary.percentage)}\",")
                writer.append("\"${summary.averageAmount}\"\n")
            }
        }
        
        return file
    }
    
    /**
     * Generate budget comparison CSV
     */
    fun generateBudgetComparisonCsv(
        budgetComparisons: List<BudgetComparison>,
        fileName: String
    ): File {
        val file = File(context.cacheDir, fileName)
        
        FileWriter(file).use { writer ->
            // Write header
            writer.append("Budget Name,Category,Budgeted Amount,Actual Spent,Difference,Percentage Used,Status\n")
            
            // Write data rows
            for (comparison in budgetComparisons) {
                writer.append("\"${escapeCsv(comparison.budget.budgetName)}\",")
                writer.append("\"${comparison.budget.category?.displayName ?: "All Categories"}\",")
                writer.append("\"${comparison.budget.totalBudget}\",")
                writer.append("\"${comparison.actualSpent}\",")
                writer.append("\"${comparison.difference}\",")
                writer.append("\"${String.format("%.2f", comparison.percentageUsed)}\",")
                writer.append("\"${comparison.status.displayName}\"\n")
            }
        }
        
        return file
    }
    
    /**
     * Generate full transaction summary CSV with all details
     */
    fun generateFullSummaryCsv(
        summary: TransactionSummary,
        transactions: List<Transaction>,
        fileName: String
    ): File {
        val file = File(context.cacheDir, fileName)
        
        FileWriter(file).use { writer ->
            // Write summary section
            writer.append("TRANSACTION SUMMARY\n")
            writer.append("Date Range,${dateFormat.format(Date(summary.dateRange.startDate))} to ${dateFormat.format(Date(summary.dateRange.endDate))}\n")
            writer.append("Total Income,${summary.totalIncome}\n")
            writer.append("Total Expenses,${summary.totalExpenses}\n")
            writer.append("Net Amount,${summary.netAmount}\n")
            writer.append("Transaction Count,${summary.transactionCount}\n")
            writer.append("Average Transaction,${summary.averageTransaction}\n")
            writer.append("\n")
            
            // Write category breakdown
            writer.append("CATEGORY BREAKDOWN\n")
            writer.append("Category,Amount,Count,Percentage\n")
            for ((_, categorySummary) in summary.categoryBreakdown) {
                writer.append("\"${categorySummary.category.displayName}\",")
                writer.append("\"${categorySummary.totalAmount}\",")
                writer.append("\"${categorySummary.transactionCount}\",")
                writer.append("\"${String.format("%.2f", categorySummary.percentage)}%\"\n")
            }
            writer.append("\n")
            
            // Write transaction details
            writer.append("TRANSACTION DETAILS\n")
            writer.append("Date,Description,Category,Type,Amount,Account\n")
            for (transaction in transactions) {
                writer.append("\"${dateFormat.format(Date(transaction.timestamp))}\",")
                writer.append("\"${escapeCsv(transaction.description)}\",")
                writer.append("\"${transaction.category.displayName}\",")
                writer.append("\"${transaction.transactionType.name}\",")
                writer.append("\"${transaction.amount}\",")
                writer.append("\"Account ${transaction.accountId}\"\n")
            }
        }
        
        return file
    }
    
    /**
     * Escape special characters in CSV values
     */
    private fun escapeCsv(value: String): String {
        return value.replace("\"", "\"\"")
    }
}

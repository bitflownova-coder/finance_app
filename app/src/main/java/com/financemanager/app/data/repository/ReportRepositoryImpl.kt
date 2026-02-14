package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.BudgetDao
import com.financemanager.app.data.local.dao.TransactionDao
import com.financemanager.app.data.mapper.BudgetMapper
import com.financemanager.app.data.mapper.TransactionMapper
import com.financemanager.app.domain.model.*
import com.financemanager.app.domain.repository.ReportRepository
import com.financemanager.app.util.CsvGenerator
import com.financemanager.app.util.PdfGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Implementation of ReportRepository
 */
class ReportRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val pdfGenerator: PdfGenerator,
    private val csvGenerator: CsvGenerator
) : ReportRepository {
    
    /**
     * Helper function to get transactions from Flow
     */
    private suspend fun getTransactionList(userId: Long, startDate: Long, endDate: Long): List<Transaction> {
        val transactionEntities = transactionDao.getTransactionsByDateRange(userId, startDate, endDate).first()
        return transactionEntities.map { TransactionMapper.toDomain(it) }
    }
    
    override suspend fun getTransactionSummary(userId: Long, dateRange: DateRange): TransactionSummary = withContext(Dispatchers.IO) {
        val transactions = getTransactionList(userId, dateRange.startDate, dateRange.endDate)
        
        val totalIncome = transactions
            .filter { it.transactionType == TransactionType.CREDIT }
            .sumOf { it.amount }
        
        val totalExpenses = transactions
            .filter { it.transactionType == TransactionType.DEBIT }
            .sumOf { it.amount }
        
        val netAmount = totalIncome - totalExpenses
        val transactionCount = transactions.size
        val averageTransaction = if (transactionCount > 0) {
            (totalIncome + totalExpenses) / transactionCount
        } else {
            0.0
        }
        
        // Calculate category breakdown
        val categoryBreakdown = calculateCategoryBreakdown(transactions, totalExpenses)
        
        TransactionSummary(
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            netAmount = netAmount,
            transactionCount = transactionCount,
            averageTransaction = averageTransaction,
            categoryBreakdown = categoryBreakdown,
            dateRange = dateRange
        )
    }
    
    override suspend fun getCategoryBreakdown(userId: Long, dateRange: DateRange): List<CategorySummary> = withContext(Dispatchers.IO) {
        val transactions = getTransactionList(userId, dateRange.startDate, dateRange.endDate)
        
        val expenseTransactions = transactions.filter { it.transactionType == TransactionType.DEBIT }
        val totalExpenses = expenseTransactions.sumOf { it.amount }
        
        return@withContext expenseTransactions
            .groupBy { it.category }
            .map { (category, categoryTransactions) ->
                val totalAmount = categoryTransactions.sumOf { it.amount }
                val percentage = if (totalExpenses > 0) (totalAmount / totalExpenses) * 100 else 0.0
                
                CategorySummary(
                    category = category,
                    totalAmount = totalAmount,
                    transactionCount = categoryTransactions.size,
                    percentage = percentage,
                    averageAmount = totalAmount / categoryTransactions.size
                )
            }
            .sortedByDescending { it.totalAmount }
    }
    
    override suspend fun getBudgetComparison(userId: Long, dateRange: DateRange): List<BudgetComparison> = withContext(Dispatchers.IO) {
        val budgetEntities = budgetDao.getCurrentBudgets(userId, dateRange.startDate).first()
        
        budgetEntities.map { budgetEntity ->
            val budget = BudgetMapper.toDomain(budgetEntity)
            val actualSpent = budget.spentAmount
            val difference = budget.totalBudget - actualSpent
            val percentageUsed = if (budget.totalBudget > 0) {
                (actualSpent / budget.totalBudget) * 100
            } else {
                0.0
            }
            
            val status = when {
                actualSpent > budget.totalBudget -> BudgetStatus.OVER_BUDGET
                actualSpent >= budget.totalBudget * 0.95 -> BudgetStatus.ON_BUDGET
                else -> BudgetStatus.UNDER_BUDGET
            }
            
            BudgetComparison(
                budget = budget,
                actualSpent = actualSpent,
                difference = difference,
                percentageUsed = percentageUsed,
                status = status
            )
        }
    }
    
    override suspend fun generatePdfReport(
        userId: Long,
        reportType: ReportType,
        dateRange: DateRange,
        fileName: String
    ): File = withContext(Dispatchers.IO) {
        return@withContext when (reportType) {
            ReportType.TRANSACTION_SUMMARY, ReportType.MONTHLY_SUMMARY, ReportType.YEARLY_SUMMARY -> {
                val summary = getTransactionSummary(userId, dateRange)
                val transactions = getTransactionList(userId, dateRange.startDate, dateRange.endDate)
                
                pdfGenerator.generateTransactionSummaryPdf(summary, transactions, fileName)
            }
            ReportType.BUDGET_VS_ACTUAL -> {
                val budgetComparisons = getBudgetComparison(userId, dateRange)
                pdfGenerator.generateBudgetComparisonPdf(budgetComparisons, dateRange, fileName)
            }
            ReportType.CATEGORY_BREAKDOWN -> {
                val summary = getTransactionSummary(userId, dateRange)
                val transactions = getTransactionList(userId, dateRange.startDate, dateRange.endDate)
                
                pdfGenerator.generateTransactionSummaryPdf(summary, transactions, fileName)
            }
            ReportType.CUSTOM -> {
                val summary = getTransactionSummary(userId, dateRange)
                val transactions = getTransactionList(userId, dateRange.startDate, dateRange.endDate)
                
                pdfGenerator.generateTransactionSummaryPdf(summary, transactions, fileName)
            }
        }
    }
    
    override suspend fun generateCsvReport(
        userId: Long,
        reportType: ReportType,
        dateRange: DateRange,
        fileName: String
    ): File = withContext(Dispatchers.IO) {
        return@withContext when (reportType) {
            ReportType.TRANSACTION_SUMMARY, ReportType.MONTHLY_SUMMARY, ReportType.YEARLY_SUMMARY, ReportType.CUSTOM -> {
                val summary = getTransactionSummary(userId, dateRange)
                val transactions = getTransactionList(userId, dateRange.startDate, dateRange.endDate)
                
                csvGenerator.generateFullSummaryCsv(summary, transactions, fileName)
            }
            ReportType.CATEGORY_BREAKDOWN -> {
                val categorySummaries = getCategoryBreakdown(userId, dateRange)
                csvGenerator.generateCategoryBreakdownCsv(categorySummaries, fileName)
            }
            ReportType.BUDGET_VS_ACTUAL -> {
                val budgetComparisons = getBudgetComparison(userId, dateRange)
                csvGenerator.generateBudgetComparisonCsv(budgetComparisons, fileName)
            }
        }
    }
    
    private fun calculateCategoryBreakdown(
        transactions: List<Transaction>,
        totalExpenses: Double
    ): Map<TransactionCategory, CategorySummary> {
        return transactions
            .filter { it.transactionType == TransactionType.DEBIT }
            .groupBy { it.category }
            .mapValues { (category, categoryTransactions) ->
                val totalAmount = categoryTransactions.sumOf { it.amount }
                val percentage = if (totalExpenses > 0) (totalAmount / totalExpenses) * 100 else 0.0
                
                CategorySummary(
                    category = category,
                    totalAmount = totalAmount,
                    transactionCount = categoryTransactions.size,
                    percentage = percentage,
                    averageAmount = if (categoryTransactions.isNotEmpty()) {
                        totalAmount / categoryTransactions.size
                    } else {
                        0.0
                    }
                )
            }
    }
}

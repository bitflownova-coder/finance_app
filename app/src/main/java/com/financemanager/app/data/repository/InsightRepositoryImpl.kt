package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.BudgetDao
import com.financemanager.app.data.local.dao.TransactionDao
import com.financemanager.app.di.IoDispatcher
import com.financemanager.app.domain.model.*
import com.financemanager.app.domain.repository.InsightRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Implementation of InsightRepository with analytics calculations
 */
class InsightRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : InsightRepository {

    override suspend fun generateInsights(userId: Long): List<Insight> {
        return withContext(ioDispatcher) {
            try {
                val insights = mutableListOf<Insight>()
                
                // Get current month data
                val calendar = Calendar.getInstance()
                val currentMonth = calendar.get(Calendar.MONTH) + 1
                val currentYear = calendar.get(Calendar.YEAR)
                
                // Add budget warnings
                insights.addAll(checkBudgetWarnings(userId, currentMonth, currentYear))
                
                // Add spending trends
                insights.addAll(analyzeSpendingTrends(userId))
                
                // Add unusual activity
                insights.addAll(detectUnusualActivity(userId))
                
                // Add savings opportunities
                insights.addAll(getSavingsOpportunities(userId))
                
                insights
            } catch (e: Exception) {
                android.util.Log.e("InsightRepositoryImpl", "Error generating insights", e)
                emptyList()
            }
        }
    }

    override suspend fun getSpendingPatterns(
        userId: Long,
        startDate: Long,
        endDate: Long
    ): List<SpendingPattern> {
        return withContext(ioDispatcher) {
            try {
                val transactions = transactionDao.getTransactionsByDateRange(userId, startDate, endDate).first()
                val expenses = transactions.filter { it.transactionType == "DEBIT" }
                
                if (expenses.isEmpty()) {
                    return@withContext emptyList()
                }
                
                // Group by category
                val categoryGroups = expenses.groupBy { it.category }
                val totalExpense = expenses.sumOf { it.amount }
            
            // Get previous period for comparison
            val periodLength = endDate - startDate
            val previousStart = startDate - periodLength
            val previousTransactions = transactionDao.getTransactionsByDateRange(userId, previousStart, startDate).first()
                .filter { it.transactionType == "DEBIT" }
            val previousCategoryTotals = previousTransactions.groupBy { it.category }
                .mapValues { (_, txns) -> txns.sumOf { it.amount } }
            
            categoryGroups.map { (category, txns) ->
                val totalAmount = txns.sumOf { it.amount }
                val previousAmount = previousCategoryTotals[category] ?: 0.0
                val change = if (previousAmount > 0) {
                    ((totalAmount - previousAmount) / previousAmount * 100).toFloat()
                } else 0f
                
                SpendingPattern(
                    category = TransactionCategory.valueOf(category),
                    averageAmount = totalAmount / txns.size,
                    totalAmount = totalAmount,
                    transactionCount = txns.size,
                    trend = when {
                        change > 10 -> Trend.INCREASING
                        change < -10 -> Trend.DECREASING
                        else -> Trend.STABLE
                    },
                    percentageOfTotal = (totalAmount / totalExpense * 100).toFloat(),
                    comparisonWithLastMonth = change
                )
            }
            } catch (e: Exception) {
                android.util.Log.e("InsightRepositoryImpl", "Error getting spending patterns", e)
                emptyList()
            }
        }
    }

    override suspend fun getMonthlySummary(userId: Long, month: Int, year: Int): MonthlySummary {
        return withContext(ioDispatcher) {
            try {
                val calendar = Calendar.getInstance()
                calendar.set(year, month - 1, 1, 0, 0, 0)
                val startDate = calendar.timeInMillis
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                val endDate = calendar.timeInMillis
                
                val transactions = transactionDao.getTransactionsByDateRange(userId, startDate, endDate).first()
                val income = transactions.filter { it.transactionType == "CREDIT" }.sumOf { it.amount }
                val expense = transactions.filter { it.transactionType == "DEBIT" }.sumOf { it.amount }
            
            // Get previous month for comparison
            calendar.add(Calendar.MONTH, -1)
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            val prevStartDate = calendar.timeInMillis
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val prevEndDate = calendar.timeInMillis
            
            val prevTransactions = transactionDao.getTransactionsByDateRange(userId, prevStartDate, prevEndDate).first()
            val prevIncome = prevTransactions.filter { it.transactionType == "CREDIT" }.sumOf { it.amount }
            val prevExpense = prevTransactions.filter { it.transactionType == "DEBIT" }.sumOf { it.amount }
            
            val topCategory = transactions
                .filter { it.transactionType == "DEBIT" }
                .groupBy { it.category }
                .maxByOrNull { it.value.sumOf { txn -> txn.amount } }
                ?.key?.let { TransactionCategory.valueOf(it) } ?: TransactionCategory.OTHER
            
            MonthlySummary(
                month = month,
                year = year,
                totalIncome = income,
                totalExpense = expense,
                netSavings = income - expense,
                savingsRate = if (income > 0) ((income - expense) / income * 100).toFloat() else 0f,
                topExpenseCategory = topCategory,
                transactionCount = transactions.size,
                averageTransactionSize = if (transactions.isNotEmpty()) expense / transactions.size else 0.0,
                comparisonWithLastMonth = MonthComparison(
                    incomeChange = if (prevIncome > 0) ((income - prevIncome) / prevIncome * 100).toFloat() else 0f,
                    expenseChange = if (prevExpense > 0) ((expense - prevExpense) / prevExpense * 100).toFloat() else 0f,
                    savingsChange = ((income - expense) - (prevIncome - prevExpense)).toFloat()
                )
            )
            } catch (e: Exception) {
                android.util.Log.e("InsightRepositoryImpl", "Error getting monthly summary", e)
                // Return empty summary on error
                MonthlySummary(
                    month = month,
                    year = year,
                    totalIncome = 0.0,
                    totalExpense = 0.0,
                    netSavings = 0.0,
                    savingsRate = 0f,
                    topExpenseCategory = TransactionCategory.OTHER,
                    transactionCount = 0,
                    averageTransactionSize = 0.0,
                    comparisonWithLastMonth = MonthComparison(
                        incomeChange = 0f,
                        expenseChange = 0f,
                        savingsChange = 0f
                    )
                )
            }
        }
    }

    override suspend fun getCategoryInsights(
        userId: Long,
        month: Int,
        year: Int
    ): List<CategoryInsight> {
        return withContext(ioDispatcher) {
            try {
                val calendar = Calendar.getInstance()
                calendar.set(year, month - 1, 1, 0, 0, 0)
                val startDate = calendar.timeInMillis
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                val endDate = calendar.timeInMillis
                
                val transactions = transactionDao.getTransactionsByDateRange(userId, startDate, endDate).first()
                    .filter { it.transactionType == "DEBIT" }
                
                if (transactions.isEmpty()) {
                    return@withContext emptyList()
                }
                
                val budgets = budgetDao.getCurrentBudgets(userId, System.currentTimeMillis()).first()
                val budgetMap = budgets.associateBy { it.category }
            
            transactions.groupBy { it.category }
                .map { (category, txns) ->
                    val totalSpent = txns.sumOf { it.amount }
                    val budget = budgetMap[category]
                    
                    CategoryInsight(
                        category = TransactionCategory.valueOf(category),
                        totalSpent = totalSpent,
                        budgetAmount = budget?.totalBudget,
                        percentageUsed = budget?.let { (totalSpent / it.totalBudget * 100).toFloat() },
                        averagePerTransaction = totalSpent / txns.size,
                        transactionCount = txns.size,
                        recommendation = generateCategoryRecommendation(totalSpent, budget?.totalBudget)
                    )
                }
            } catch (e: Exception) {
                android.util.Log.e("InsightRepositoryImpl", "Error getting category insights", e)
                emptyList()
            }
        }
    }

    override suspend fun predictNextMonthExpense(userId: Long): Double {
        return withContext(ioDispatcher) {
            try {
                // Simple moving average of last 3 months
                val expenses = mutableListOf<Double>()
            
            repeat(3) { index ->
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -index)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)
                
                calendar.set(year, month - 1, 1, 0, 0, 0)
                val startDate = calendar.timeInMillis
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                val endDate = calendar.timeInMillis
                
                val monthExpense = transactionDao.getTransactionsByDateRange(userId, startDate, endDate).first()
                    .filter { it.transactionType == "DEBIT" }
                    .sumOf { it.amount }
                
                expenses.add(monthExpense)
            }
            
            if (expenses.isEmpty()) 0.0 else expenses.average()
            } catch (e: Exception) {
                android.util.Log.e("InsightRepositoryImpl", "Error predicting next month expense", e)
                0.0
            }
        }
    }

    override fun getSpendingTrend(userId: Long, months: Int): Flow<List<Pair<String, Double>>> {
        return flow {
            val trend = mutableListOf<Pair<String, Double>>()
            
            repeat(months) { index ->
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, -index)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)
                
                calendar.set(year, month - 1, 1, 0, 0, 0)
                val startDate = calendar.timeInMillis
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                val endDate = calendar.timeInMillis
                
                val expense = transactionDao.getTransactionsByDateRange(userId, startDate, endDate).first()
                    .filter { it.transactionType == "DEBIT" }
                    .sumOf { it.amount }
                
                val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                        ?: "Month"
                trend.add("$monthName $year" to expense)
            }
            
            emit(trend.reversed())
        }.flowOn(ioDispatcher)
    }

    override suspend fun detectUnusualActivity(userId: Long): List<Insight> {
        return withContext(ioDispatcher) {
            val insights = mutableListOf<Insight>()
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -30)
            val thirtyDaysAgo = calendar.timeInMillis
            
            val recentTransactions = transactionDao.getTransactionsByDateRange(userId, thirtyDaysAgo, System.currentTimeMillis()).first()
                .filter { it.transactionType == "DEBIT" }
            
            if (recentTransactions.isEmpty()) return@withContext insights
            
            val average = recentTransactions.sumOf { it.amount } / recentTransactions.size
            val stdDev = calculateStandardDeviation(recentTransactions.map { it.amount })
            
            // Find transactions that are more than 2 standard deviations from mean
            val unusualTransactions = recentTransactions.filter {
                abs(it.amount - average) > (2 * stdDev)
            }
            
            if (unusualTransactions.isNotEmpty()) {
                val totalUnusual = unusualTransactions.sumOf { it.amount }
                insights.add(
                    Insight(
                        id = "unusual_${System.currentTimeMillis()}",
                        type = InsightType.UNUSUAL_ACTIVITY,
                        title = "Unusual Spending Detected",
                        description = "${unusualTransactions.size} transaction(s) significantly higher than your average",
                        amount = totalUnusual,
                        priority = InsightPriority.HIGH,
                        actionable = true,
                        actionText = "Review transactions",
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
            
            insights
        }
    }

    override suspend fun getSavingsOpportunities(userId: Long): List<Insight> {
        return withContext(ioDispatcher) {
            val insights = mutableListOf<Insight>()
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)
            
            val patterns = getSpendingPatterns(userId, getMonthStartDate(month, year), System.currentTimeMillis())
            
            // Find categories with increasing trends
            patterns.filter { it.trend == Trend.INCREASING && it.comparisonWithLastMonth > 20 }
                .forEach { pattern ->
                    insights.add(
                        Insight(
                            id = "savings_${pattern.category.name}_${System.currentTimeMillis()}",
                            type = InsightType.SAVINGS_OPPORTUNITY,
                            title = "Spending Increase in ${pattern.category.name}",
                            description = "${pattern.category.name} spending increased by ${pattern.comparisonWithLastMonth.toInt()}%",
                            amount = pattern.totalAmount,
                            category = pattern.category,
                            priority = InsightPriority.MEDIUM,
                            actionable = true,
                            actionText = "Set a budget",
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            
            insights
        }
    }

    private suspend fun checkBudgetWarnings(userId: Long, month: Int, year: Int): List<Insight> {
        return try {
            val insights = mutableListOf<Insight>()
            val budgets = budgetDao.getCurrentBudgets(userId, System.currentTimeMillis()).first()
            
            if (budgets.isEmpty()) {
                return emptyList()
            }
            
            budgets.forEach { budget ->
            val spent = budget.spentAmount
            val percentage = (spent / budget.totalBudget * 100).toFloat()
            
            when {
                percentage >= 100 -> {
                    insights.add(
                        Insight(
                            id = "budget_exceeded_${budget.category ?: "general"}",
                            type = InsightType.BUDGET_WARNING,
                            title = "Budget Exceeded",
                            description = "${budget.category ?: "General"} budget exceeded by ₹${spent - budget.totalBudget}",
                            amount = spent - budget.totalBudget,
                            percentage = percentage,
                            category = budget.category?.let { TransactionCategory.valueOf(it) },
                            priority = InsightPriority.CRITICAL,
                            actionable = true,
                            actionText = "Review spending",
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
                percentage >= 80 -> {
                    insights.add(
                        Insight(
                            id = "budget_warning_${budget.category ?: "general"}",
                            type = InsightType.BUDGET_WARNING,
                            title = "Budget Alert",
                            description = "${budget.category ?: "General"}: ${percentage.toInt()}% of budget used",
                            amount = budget.totalBudget - spent,
                            percentage = percentage,
                            category = budget.category?.let { TransactionCategory.valueOf(it) },
                            priority = InsightPriority.HIGH,
                            actionable = false,
                            actionText = null,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
                else -> {
                    // Budget usage is below 80%, no alert needed
                }
            }
        }
            
            insights
        } catch (e: Exception) {
            android.util.Log.e("InsightRepositoryImpl", "Error checking budget warnings", e)
            emptyList()
        }
    }

    private suspend fun analyzeSpendingTrends(userId: Long): List<Insight> {
        val insights = mutableListOf<Insight>()
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        
        calendar.add(Calendar.MONTH, -1)
        val prevMonth = calendar.get(Calendar.MONTH) + 1
        val prevYear = calendar.get(Calendar.YEAR)
        
        val currentSummary = getMonthlySummary(userId, currentMonth, currentYear)
        
        if (currentSummary.comparisonWithLastMonth.expenseChange > 15) {
            insights.add(
                Insight(
                    id = "spending_increase_${System.currentTimeMillis()}",
                    type = InsightType.SPENDING_TREND,
                    title = "Spending Increased",
                    description = "Your spending increased by ${currentSummary.comparisonWithLastMonth.expenseChange.toInt()}% this month",
                    amount = currentSummary.totalExpense,
                    percentage = currentSummary.comparisonWithLastMonth.expenseChange,
                    priority = InsightPriority.MEDIUM,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        
        if (currentSummary.savingsRate > 20) {
            insights.add(
                Insight(
                    id = "good_savings_${System.currentTimeMillis()}",
                    type = InsightType.SAVINGS_OPPORTUNITY,
                    title = "Great Savings!",
                    description = "You saved ${currentSummary.savingsRate.toInt()}% of your income this month",
                    amount = currentSummary.netSavings,
                    percentage = currentSummary.savingsRate,
                    priority = InsightPriority.LOW,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        
        return insights
    }

    private fun createMonthlySummaryInsight(summary: MonthlySummary): Insight {
        return Insight(
            id = "monthly_summary_${summary.month}_${summary.year}",
            type = InsightType.MONTHLY_SUMMARY,
            title = "Monthly Summary",
            description = "Income: ₹${summary.totalIncome.toInt()}, Expense: ₹${summary.totalExpense.toInt()}, Savings: ₹${summary.netSavings.toInt()}",
            amount = summary.netSavings,
            priority = InsightPriority.LOW,
            timestamp = System.currentTimeMillis()
        )
    }

    private fun generateCategoryRecommendation(spent: Double, budget: Double?): String? {
        return budget?.let {
            val percentage = (spent / budget * 100)
            when {
                percentage > 100 -> "Consider reducing spending in this category"
                percentage > 80 -> "Getting close to budget limit"
                percentage < 50 -> "Great job staying within budget!"
                else -> null
            }
        }
    }

    private fun calculateStandardDeviation(values: List<Double>): Double {
        if (values.isEmpty()) return 0.0
        val mean = values.average()
        val variance = values.map { (it - mean) * (it - mean) }.average()
        return sqrt(variance)
    }

    private fun getMonthStartDate(month: Int, year: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        return calendar.timeInMillis
    }
}

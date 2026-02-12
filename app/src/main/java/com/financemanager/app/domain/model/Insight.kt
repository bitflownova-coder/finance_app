package com.financemanager.app.domain.model

/**
 * Domain model for financial insights
 */
data class Insight(
    val id: String,
    val type: InsightType,
    val title: String,
    val description: String,
    val amount: Double? = null,
    val percentage: Float? = null,
    val category: TransactionCategory? = null,
    val priority: InsightPriority = InsightPriority.MEDIUM,
    val actionable: Boolean = false,
    val actionText: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class InsightType {
    SPENDING_TREND,
    BUDGET_WARNING,
    SAVINGS_OPPORTUNITY,
    UNUSUAL_ACTIVITY,
    CATEGORY_ANALYSIS,
    INCOME_TREND,
    RECURRING_EXPENSE,
    MONTHLY_SUMMARY,
    PREDICTION
}

enum class InsightPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

/**
 * Spending pattern analysis
 */
data class SpendingPattern(
    val category: TransactionCategory,
    val averageAmount: Double,
    val totalAmount: Double,
    val transactionCount: Int,
    val trend: Trend,
    val percentageOfTotal: Float,
    val comparisonWithLastMonth: Float // Positive means increase
)

enum class Trend {
    INCREASING,
    DECREASING,
    STABLE
}

/**
 * Monthly summary statistics
 */
data class MonthlySummary(
    val month: Int,
    val year: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val netSavings: Double,
    val savingsRate: Float,
    val topExpenseCategory: TransactionCategory,
    val transactionCount: Int,
    val averageTransactionSize: Double,
    val comparisonWithLastMonth: MonthComparison
)

data class MonthComparison(
    val incomeChange: Float,
    val expenseChange: Float,
    val savingsChange: Float
)

/**
 * Category insights
 */
data class CategoryInsight(
    val category: TransactionCategory,
    val totalSpent: Double,
    val budgetAmount: Double?,
    val percentageUsed: Float?,
    val averagePerTransaction: Double,
    val transactionCount: Int,
    val topMerchants: List<String> = emptyList(),
    val recommendation: String? = null
)

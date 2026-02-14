package com.financemanager.app.presentation.analytics

import com.financemanager.app.domain.model.TransactionCategory
import com.financemanager.app.domain.usecase.analytics.GetExpenseByCategoryUseCase
import com.financemanager.app.domain.usecase.analytics.GetFinancialSummaryUseCase
import com.financemanager.app.domain.usecase.analytics.GetMonthlyTrendUseCase
import com.financemanager.app.domain.usecase.analytics.GetTopSpendingCategoriesUseCase

/**
 * UI State for Analytics screen
 */
data class AnalyticsUiState(
    val expenseByCategory: Map<TransactionCategory, Double> = emptyMap(),
    val monthlyTrend: List<GetMonthlyTrendUseCase.MonthlyData> = emptyList(),
    val topSpending: List<GetTopSpendingCategoriesUseCase.CategorySpending> = emptyList(),
    val financialSummary: GetFinancialSummaryUseCase.FinancialSummary? = null,
    val selectedMonth: Int = 0,
    val selectedYear: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Events for Analytics screen
 */
sealed class AnalyticsEvent {
    data class SelectMonth(val month: Int, val year: Int) : AnalyticsEvent()
    object LoadAnalytics : AnalyticsEvent()
    object ExportData : AnalyticsEvent()
}

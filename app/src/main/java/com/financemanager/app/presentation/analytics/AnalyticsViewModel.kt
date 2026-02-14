package com.financemanager.app.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.analytics.GetExpenseByCategoryUseCase
import com.financemanager.app.domain.usecase.analytics.GetFinancialSummaryUseCase
import com.financemanager.app.domain.usecase.analytics.GetMonthlyTrendUseCase
import com.financemanager.app.domain.usecase.analytics.GetTopSpendingCategoriesUseCase
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for Analytics screen
 * Handles data aggregation and chart preparation
 */
@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getExpenseByCategoryUseCase: GetExpenseByCategoryUseCase,
    private val getMonthlyTrendUseCase: GetMonthlyTrendUseCase,
    private val getTopSpendingCategoriesUseCase: GetTopSpendingCategoriesUseCase,
    private val getFinancialSummaryUseCase: GetFinancialSummaryUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()
    
    init {
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        
        _uiState.update {
            it.copy(selectedMonth = currentMonth, selectedYear = currentYear)
        }
        
        loadAnalytics()
    }
    
    fun onEvent(event: AnalyticsEvent) {
        when (event) {
            is AnalyticsEvent.LoadAnalytics -> loadAnalytics()
            is AnalyticsEvent.SelectMonth -> selectMonth(event.month, event.year)
            is AnalyticsEvent.ExportData -> exportData()
        }
    }
    
    private fun selectMonth(month: Int, year: Int) {
        _uiState.update {
            it.copy(selectedMonth = month, selectedYear = year)
        }
        loadAnalytics()
    }
    
    private fun loadAnalytics() {
        val userId = sessionManager.getUserId() ?: return
        val month = _uiState.value.selectedMonth
        val year = _uiState.value.selectedYear
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // Load all analytics data in parallel
                val expenseByCategory = getExpenseByCategoryUseCase(userId, month, year)
                val monthlyTrend = getMonthlyTrendUseCase(userId, numberOfMonths = 6)
                val topSpending = getTopSpendingCategoriesUseCase(userId, month, year, limit = 5)
                val financialSummary = getFinancialSummaryUseCase(userId, month, year)
                
                _uiState.update {
                    it.copy(
                        expenseByCategory = expenseByCategory,
                        monthlyTrend = monthlyTrend,
                        topSpending = topSpending,
                        financialSummary = financialSummary,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load analytics"
                    )
                }
            }
        }
    }
    
    private fun exportData() {
        // TODO: Implement CSV/PDF export in future enhancement
        viewModelScope.launch {
            // Export logic will be added later
        }
    }
}

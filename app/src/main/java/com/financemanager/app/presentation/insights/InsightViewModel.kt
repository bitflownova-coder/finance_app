package com.financemanager.app.presentation.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.CategoryInsight
import com.financemanager.app.domain.model.Insight
import com.financemanager.app.domain.model.MonthlySummary
import com.financemanager.app.domain.model.SpendingPattern
import com.financemanager.app.domain.usecase.insights.GenerateInsightsUseCase
import com.financemanager.app.domain.usecase.insights.GetCategoryInsightsUseCase
import com.financemanager.app.domain.usecase.insights.GetMonthlySummaryUseCase
import com.financemanager.app.domain.usecase.insights.GetSpendingPatternsUseCase
import com.financemanager.app.domain.usecase.insights.PredictExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for Smart Insights screen
 */
@HiltViewModel
class InsightViewModel @Inject constructor(
    private val generateInsightsUseCase: GenerateInsightsUseCase,
    private val getSpendingPatternsUseCase: GetSpendingPatternsUseCase,
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase,
    private val getCategoryInsightsUseCase: GetCategoryInsightsUseCase,
    private val predictExpensesUseCase: PredictExpensesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightUiState())
    val uiState: StateFlow<InsightUiState> = _uiState.asStateFlow()

    /**
     * Load all insights for user
     */
    fun loadInsights(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val insights = generateInsightsUseCase(userId)
                _uiState.update {
                    it.copy(
                        insights = insights,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load insights"
                    )
                }
            }
        }
    }

    /**
     * Load spending patterns for specified period
     */
    fun loadSpendingPatterns(userId: Long, startDate: Long, endDate: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val patterns = getSpendingPatternsUseCase(userId, startDate, endDate)
                _uiState.update {
                    it.copy(
                        spendingPatterns = patterns,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load spending patterns"
                    )
                }
            }
        }
    }

    /**
     * Load monthly summary
     */
    fun loadMonthlySummary(userId: Long, month: Int? = null, year: Int? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val calendar = Calendar.getInstance()
                val currentMonth = month ?: (calendar.get(Calendar.MONTH) + 1)
                val currentYear = year ?: calendar.get(Calendar.YEAR)
                
                val summary = getMonthlySummaryUseCase(userId, currentMonth, currentYear)
                _uiState.update {
                    it.copy(
                        monthlySummary = summary,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load monthly summary"
                    )
                }
            }
        }
    }

    /**
     * Load category insights
     */
    fun loadCategoryInsights(userId: Long, month: Int? = null, year: Int? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val calendar = Calendar.getInstance()
                val currentMonth = month ?: (calendar.get(Calendar.MONTH) + 1)
                val currentYear = year ?: calendar.get(Calendar.YEAR)
                
                val insights = getCategoryInsightsUseCase(userId, currentMonth, currentYear)
                _uiState.update {
                    it.copy(
                        categoryInsights = insights,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load category insights"
                    )
                }
            }
        }
    }

    /**
     * Load predicted expenses for next month
     */
    fun loadPrediction(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val prediction = predictExpensesUseCase(userId)
                _uiState.update {
                    it.copy(
                        predictedExpense = prediction,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load prediction"
                    )
                }
            }
        }
    }

    /**
     * Load all data at once
     */
    fun loadAllData(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Get current month/year
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)
                
                // Get start and end of current month
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val startDate = calendar.timeInMillis
                
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                val endDate = calendar.timeInMillis
                
                // Load all data
                val insights = generateInsightsUseCase(userId)
                val patterns = getSpendingPatternsUseCase(userId, startDate, endDate)
                val summary = getMonthlySummaryUseCase(userId, month, year)
                val categoryInsights = getCategoryInsightsUseCase(userId, month, year)
                val prediction = predictExpensesUseCase(userId)
                
                _uiState.update {
                    it.copy(
                        insights = insights,
                        spendingPatterns = patterns,
                        monthlySummary = summary,
                        categoryInsights = categoryInsights,
                        predictedExpense = prediction,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load data"
                    )
                }
            }
        }
    }

    /**
     * Refresh all insights
     */
    fun refresh(userId: Long) {
        loadAllData(userId)
    }

    /**
     * Clear error
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * UI state for insights screen
 */
data class InsightUiState(
    val insights: List<Insight> = emptyList(),
    val spendingPatterns: List<SpendingPattern> = emptyList(),
    val monthlySummary: MonthlySummary? = null,
    val categoryInsights: List<CategoryInsight> = emptyList(),
    val predictedExpense: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val hasData: Boolean
        get() = insights.isNotEmpty() || spendingPatterns.isNotEmpty() || monthlySummary != null
    
    val criticalInsights: List<Insight>
        get() = insights.filter { it.priority == com.financemanager.app.domain.model.InsightPriority.CRITICAL }
    
    val highPriorityInsights: List<Insight>
        get() = insights.filter { it.priority == com.financemanager.app.domain.model.InsightPriority.HIGH }
    
    val mediumPriorityInsights: List<Insight>
        get() = insights.filter { it.priority == com.financemanager.app.domain.model.InsightPriority.MEDIUM }
    
    val lowPriorityInsights: List<Insight>
        get() = insights.filter { it.priority == com.financemanager.app.domain.model.InsightPriority.LOW }
}

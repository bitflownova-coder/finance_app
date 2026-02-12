package com.financemanager.app.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.*
import com.financemanager.app.domain.usecase.report.*
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Reports screen
 */
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getTransactionSummaryUseCase: GetTransactionSummaryUseCase,
    private val getBudgetComparisonUseCase: GetBudgetComparisonUseCase,
    private val generatePdfReportUseCase: GeneratePdfReportUseCase,
    private val generateCsvReportUseCase: GenerateCsvReportUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()
    
    init {
        loadSummary()
    }
    
    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.SelectReportType -> {
                _uiState.update { it.copy(selectedReportType = event.reportType) }
                loadSummary()
            }
            is ReportEvent.SelectDateRange -> {
                _uiState.update { it.copy(selectedDateRange = event.dateRange) }
                loadSummary()
            }
            is ReportEvent.SelectFileFormat -> {
                _uiState.update { it.copy(selectedFileFormat = event.fileFormat) }
            }
            ReportEvent.GenerateReport -> generateReport()
            ReportEvent.LoadSummary -> loadSummary()
            ReportEvent.ShareReport -> { /* Handled in Fragment */ }
        }
    }
    
    private fun loadSummary() {
        val userId = sessionManager.getUserId() ?: return
        val dateRange = _uiState.value.selectedDateRange
        val reportType = _uiState.value.selectedReportType
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                when (reportType) {
                    ReportType.TRANSACTION_SUMMARY,
                    ReportType.MONTHLY_SUMMARY,
                    ReportType.YEARLY_SUMMARY,
                    ReportType.CATEGORY_BREAKDOWN,
                    ReportType.CUSTOM -> {
                        val summary = getTransactionSummaryUseCase(userId, dateRange)
                        _uiState.update {
                            it.copy(
                                summary = summary,
                                categoryBreakdown = summary.categoryBreakdown.values.toList(),
                                isLoading = false
                            )
                        }
                    }
                    ReportType.BUDGET_VS_ACTUAL -> {
                        val budgetComparison = getBudgetComparisonUseCase(userId, dateRange)
                        val summary = getTransactionSummaryUseCase(userId, dateRange)
                        _uiState.update {
                            it.copy(
                                summary = summary,
                                budgetComparison = budgetComparison,
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load report data"
                    )
                }
            }
        }
    }
    
    private fun generateReport() {
        val userId = sessionManager.getUserId() ?: return
        val state = _uiState.value
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val fileName = generateFileName(state.selectedReportType, state.selectedFileFormat)
                
                val file = when (state.selectedFileFormat) {
                    FileFormat.PDF -> generatePdfReportUseCase(
                        userId,
                        state.selectedReportType,
                        state.selectedDateRange,
                        fileName
                    )
                    FileFormat.CSV -> generateCsvReportUseCase(
                        userId,
                        state.selectedReportType,
                        state.selectedDateRange,
                        fileName
                    )
                }
                
                _uiState.update {
                    it.copy(
                        generatedFile = file,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to generate report"
                    )
                }
            }
        }
    }
    
    private fun generateFileName(reportType: ReportType, fileFormat: FileFormat): String {
        val timestamp = System.currentTimeMillis()
        val reportName = reportType.displayName.replace(" ", "_").lowercase()
        return "finance_${reportName}_$timestamp.${fileFormat.extension}"
    }
}

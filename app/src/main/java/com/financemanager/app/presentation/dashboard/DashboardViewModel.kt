package com.financemanager.app.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.account.CalculateTotalBalanceUseCase
import com.financemanager.app.domain.usecase.account.GetAccountsUseCase
import com.financemanager.app.domain.usecase.transaction.GetMonthlyExpensesUseCase
import com.financemanager.app.domain.usecase.transaction.GetMonthlyIncomeUseCase
import com.financemanager.app.domain.usecase.transaction.GetTransactionsUseCase
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel for Dashboard screen
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val calculateTotalBalanceUseCase: CalculateTotalBalanceUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getMonthlyIncomeUseCase: GetMonthlyIncomeUseCase,
    private val getMonthlyExpensesUseCase: GetMonthlyExpensesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    init {
        loadDashboard()
    }
    
    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.LoadDashboard -> loadDashboard()
            is DashboardEvent.RefreshDashboard -> loadDashboard()
        }
    }
    
    private fun loadDashboard() {
        val userId = sessionManager.getUserId() ?: return
        val userName = sessionManager.getUserName() ?: ""
        
        _uiState.update { it.copy(
            isLoading = true,
            userName = userName
        )}
        
        viewModelScope.launch {
            try {
                // Get current month/year
                val calendar = Calendar.getInstance()
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)
                
                // Load total balance
                val totalBalance = calculateTotalBalanceUseCase(userId)
                
                // Load monthly stats
                val monthlyIncome = getMonthlyIncomeUseCase(userId, month, year)
                val monthlyExpenses = getMonthlyExpensesUseCase(userId, month, year)
                
                // Load accounts count and recent transactions
                getAccountsUseCase(userId).collect { accounts ->
                    getTransactionsUseCase(userId).collect { transactions ->
                        _uiState.update { it.copy(
                            totalBalance = totalBalance,
                            monthlyIncome = monthlyIncome,
                            monthlyExpenses = monthlyExpenses,
                            recentTransactions = transactions.take(5),
                            accountCount = accounts.size,
                            isLoading = false
                        )}
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message
                )}
            }
        }
    }
}

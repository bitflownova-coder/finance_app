package com.financemanager.app.presentation.dashboard

import com.financemanager.app.domain.model.Transaction

/**
 * UI state for Dashboard screen
 */
data class DashboardUiState(
    val isLoading: Boolean = false,
    val userName: String = "",
    val totalBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val accountCount: Int = 0,
    val error: String? = null
)

/**
 * Events for Dashboard
 */
sealed class DashboardEvent {
    data object LoadDashboard : DashboardEvent()
    data object RefreshDashboard : DashboardEvent()
}

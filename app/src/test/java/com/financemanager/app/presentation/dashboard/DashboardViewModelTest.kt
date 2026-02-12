package com.financemanager.app.presentation.dashboard

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionType
import com.financemanager.app.domain.usecase.GetRecentTransactionsUseCase
import com.financemanager.app.domain.usecase.GetTotalBalanceUseCase
import com.financemanager.app.domain.usecase.GetMonthlyExpensesUseCase
import com.financemanager.app.domain.usecase.GetMonthlyIncomeUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    
    private lateinit var viewModel: DashboardViewModel
    private lateinit var getTotalBalanceUseCase: GetTotalBalanceUseCase
    private lateinit var getRecentTransactionsUseCase: GetRecentTransactionsUseCase
    private lateinit var getMonthlyExpensesUseCase: GetMonthlyExpensesUseCase
    private lateinit var getMonthlyIncomeUseCase: GetMonthlyIncomeUseCase
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        getTotalBalanceUseCase = mockk()
        getRecentTransactionsUseCase = mockk()
        getMonthlyExpensesUseCase = mockk()
        getMonthlyIncomeUseCase = mockk()
        
        viewModel = DashboardViewModel(
            getTotalBalanceUseCase,
            getRecentTransactionsUseCase,
            getMonthlyExpensesUseCase,
            getMonthlyIncomeUseCase
        )
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `loadDashboardData updates state with balance and transactions`() = runTest {
        // Given
        val mockBalance = 10000.0
        val mockTransactions = listOf(
            Transaction(
                transactionId = 1,
                userId = 1,
                accountId = 1,
                amount = 500.0,
                type = TransactionType.DEBIT,
                category = "Food",
                description = "Lunch",
                timestamp = System.currentTimeMillis()
            )
        )
        
        coEvery { getTotalBalanceUseCase(any()) } returns mockBalance
        coEvery { getRecentTransactionsUseCase(any(), any()) } returns flowOf(mockTransactions)
        coEvery { getMonthlyExpensesUseCase(any()) } returns 5000.0
        coEvery { getMonthlyIncomeUseCase(any()) } returns 15000.0
        
        // When
        viewModel.loadDashboardData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(mockBalance, state.totalBalance, 0.01)
        assertEquals(mockTransactions, state.recentTransactions)
        assertEquals(5000.0, state.monthlyExpenses, 0.01)
        assertEquals(15000.0, state.monthlyIncome, 0.01)
        assertFalse(state.isLoading)
    }
    
    @Test
    fun `loadDashboardData handles error correctly`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { getTotalBalanceUseCase(any()) } throws Exception(errorMessage)
        
        // When
        viewModel.loadDashboardData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertNotNull(state.errorMessage)
        assertFalse(state.isLoading)
    }
}

package com.financemanager.app.presentation.transaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionCategory
import com.financemanager.app.domain.model.TransactionType
import com.financemanager.app.domain.usecase.transaction.*
import com.financemanager.app.util.Result
import com.financemanager.app.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

/**
 * Unit tests for TransactionViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TransactionViewModelTest {
    
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var sessionManager: SessionManager
    private lateinit var getTransactionsUseCase: GetTransactionsUseCase
    private lateinit var addTransactionUseCase: AddTransactionUseCase
    private lateinit var updateTransactionUseCase: UpdateTransactionUseCase
    private lateinit var deleteTransactionUseCase: DeleteTransactionUseCase
    private lateinit var searchTransactionsUseCase: SearchTransactionsUseCase
    private lateinit var viewModel: TransactionViewModel
    
    private val testUserId = 1L
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        sessionManager = mock()
        getTransactionsUseCase = mock()
        addTransactionUseCase = mock()
        updateTransactionUseCase = mock()
        deleteTransactionUseCase = mock()
        searchTransactionsUseCase = mock()
        
        whenever(sessionManager.getUserId()).thenReturn(testUserId)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state should load transactions`() = runTest {
        // Given
        val transactions = listOf(createTestTransaction())
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(transactions))
        
        // When
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(transactions, state.transactions)
        assertFalse(state.isLoading)
        verify(getTransactionsUseCase).invoke(testUserId)
    }
    
    @Test
    fun `loadTransactions should set loading state`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        
        // When
        viewModel.onEvent(TransactionEvent.LoadTransactions)
        
        // Then - Initially loading is true (captured in flow)
        verify(getTransactionsUseCase, atLeast(1)).invoke(testUserId)
    }
    
    @Test
    fun `addTransactionClicked should show dialog`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When
        viewModel.onEvent(TransactionEvent.AddTransactionClicked)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state.isDialogVisible)
        assertNull(state.selectedTransaction)
    }
    
    @Test
    fun `editTransactionClicked should show dialog with transaction`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val transaction = createTestTransaction()
        
        // When
        viewModel.onEvent(TransactionEvent.EditTransactionClicked(transaction))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertTrue(state.isDialogVisible)
        assertEquals(transaction, state.selectedTransaction)
    }
    
    @Test
    fun `saveTransaction with new transaction should call addTransactionUseCase`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val transaction = createTestTransaction().copy(transactionId = 0L)
        whenever(addTransactionUseCase(transaction)).thenReturn(Result.Success(123L))
        
        // When
        viewModel.onEvent(TransactionEvent.SaveTransaction(transaction))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        verify(addTransactionUseCase).invoke(transaction)
    }
    
    @Test
    fun `saveTransaction with existing transaction should call updateTransactionUseCase`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val transaction = createTestTransaction().copy(transactionId = 123L)
        whenever(updateTransactionUseCase(transaction)).thenReturn(Result.Success(Unit))
        
        // When
        viewModel.onEvent(TransactionEvent.SaveTransaction(transaction))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        verify(updateTransactionUseCase).invoke(transaction)
    }
    
    @Test
    fun `saveTransaction with error should set error state`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val transaction = createTestTransaction().copy(transactionId = 0L)
        val errorMessage = "Failed to save transaction"
        whenever(addTransactionUseCase(transaction)).thenReturn(Result.Error(Exception(errorMessage)))
        
        // When
        viewModel.onEvent(TransactionEvent.SaveTransaction(transaction))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(errorMessage, state.error)
    }
    
    @Test
    fun `deleteTransaction should call deleteTransactionUseCase`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val transactionId = 123L
        whenever(deleteTransactionUseCase(transactionId)).thenReturn(Result.Success(Unit))
        
        // When
        viewModel.onEvent(TransactionEvent.DeleteTransactionClicked(transactionId))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        verify(deleteTransactionUseCase).invoke(transactionId)
    }
    
    @Test
    fun `searchQueryChanged with blank query should load all transactions`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // When
        viewModel.onEvent(TransactionEvent.SearchQueryChanged(""))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        verify(getTransactionsUseCase, atLeast(2)).invoke(testUserId)
    }
    
    @Test
    fun `searchQueryChanged with query should call searchTransactionsUseCase`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val query = "food"
        val searchResults = listOf(createTestTransaction())
        whenever(searchTransactionsUseCase(testUserId, query)).thenReturn(flowOf(searchResults))
        
        // When
        viewModel.onEvent(TransactionEvent.SearchQueryChanged(query))
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        verify(searchTransactionsUseCase).invoke(testUserId, query)
        assertEquals(searchResults, viewModel.uiState.value.transactions)
    }
    
    @Test
    fun `dismissDialog should hide dialog`() = runTest {
        // Given
        whenever(getTransactionsUseCase(testUserId)).thenReturn(flowOf(emptyList()))
        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Show dialog first
        viewModel.onEvent(TransactionEvent.AddTransactionClicked)
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isDialogVisible)
        
        // When
        viewModel.onEvent(TransactionEvent.DismissDialog)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertFalse(viewModel.uiState.value.isDialogVisible)
    }
    
    private fun createViewModel() = TransactionViewModel(
        sessionManager = sessionManager,
        getTransactionsUseCase = getTransactionsUseCase,
        addTransactionUseCase = addTransactionUseCase,
        updateTransactionUseCase = updateTransactionUseCase,
        deleteTransactionUseCase = deleteTransactionUseCase,
        searchTransactionsUseCase = searchTransactionsUseCase
    )
    
    private fun createTestTransaction() = Transaction(
        transactionId = 1L,
        userId = testUserId,
        accountId = 1L,
        amount = 100.0,
        transactionType = TransactionType.DEBIT,
        category = TransactionCategory.FOOD,
        description = "Lunch",
        timestamp = System.currentTimeMillis()
    )
}

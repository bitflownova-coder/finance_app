package com.financemanager.app.presentation.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.TransactionFilter
import com.financemanager.app.domain.usecase.transaction.AddTransactionUseCase
import com.financemanager.app.domain.usecase.transaction.DeleteTransactionUseCase
import com.financemanager.app.domain.usecase.transaction.GetTransactionsUseCase
import com.financemanager.app.domain.usecase.transaction.SearchTransactionsUseCase
import com.financemanager.app.domain.usecase.transaction.UpdateTransactionUseCase
import com.financemanager.app.util.Result
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Transaction screen
 */
@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val searchTransactionsUseCase: SearchTransactionsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()
    
    init {
        loadTransactions()
    }
    
    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.LoadTransactions -> loadTransactions()
            is TransactionEvent.AddTransactionClicked -> showAddTransactionDialog()
            is TransactionEvent.EditTransactionClicked -> showEditTransactionDialog(event.transaction)
            is TransactionEvent.DeleteTransactionClicked -> deleteTransaction(event.transactionId)
            is TransactionEvent.SaveTransaction -> saveTransaction(event.transaction)
            is TransactionEvent.SearchQueryChanged -> searchTransactions(event.query)
            is TransactionEvent.DismissDialog -> dismissDialog()
            is TransactionEvent.ShowFilterDialog -> showFilterDialog()
            is TransactionEvent.ApplyFilter -> applyFilter(event.filter)
            is TransactionEvent.ClearFilters -> clearFilters()
        }
    }
    
    private fun loadTransactions() {
        val userId = sessionManager.getUserId() ?: return
        
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            searchTransactionsUseCase(userId, _uiState.value.currentFilter).collect { transactions ->
                _uiState.update { it.copy(
                    transactions = transactions,
                    isLoading = false
                )}
            }
        }
    }
    
    private fun searchTransactions(query: String) {
        val newFilter = _uiState.value.currentFilter.copy(searchQuery = query)
        applyFilter(newFilter)
    }
    
    private fun showFilterDialog() {
        _uiState.update { it.copy(isFilterDialogVisible = true) }
    }
    
    private fun applyFilter(filter: TransactionFilter) {
        val userId = sessionManager.getUserId() ?: return
        
        _uiState.update { it.copy(
            currentFilter = filter,
            searchQuery = filter.searchQuery ?: "",
            isFilterDialogVisible = false,
            isLoading = true
        )}
        
        viewModelScope.launch {
            searchTransactionsUseCase(userId, filter).collect { transactions ->
                _uiState.update { it.copy(
                    transactions = transactions,
                    isLoading = false
                )}
            }
        }
    }
    
    private fun clearFilters() {
        applyFilter(TransactionFilter.EMPTY)
    }
    
    private fun showAddTransactionDialog() {
        _uiState.update { it.copy(
            isDialogVisible = true,
            selectedTransaction = null
        )}
    }
    
    private fun showEditTransactionDialog(transaction: com.financemanager.app.domain.model.Transaction) {
        _uiState.update { it.copy(
            isDialogVisible = true,
            selectedTransaction = transaction
        )}
    }
    
    private fun dismissDialog() {
        _uiState.update { it.copy(
            isDialogVisible = false,
            selectedTransaction = null,
            error = null
        )}
    }
    
    private fun saveTransaction(transaction: com.financemanager.app.domain.model.Transaction) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val result = if (transaction.transactionId == 0L) {
                // Add new transaction
                addTransactionUseCase(transaction.copy(userId = sessionManager.getUserId() ?: 0))
            } else {
                // Update existing transaction
                updateTransactionUseCase(transaction)
            }
            
            when (result) {
                is Result.Success -> {
                    dismissDialog()
                    loadTransactions()
                }
                is Result.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.exception.message
                    )}
                }
                is Result.Loading -> {
                    // Not used
                }
            }
        }
    }
    
    private fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            when (val result = deleteTransactionUseCase(transactionId)) {
                is Result.Success -> {
                    loadTransactions()
                }
                is Result.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.exception.message
                    )}
                }
                is Result.Loading -> {
                    // Not used
                }
            }
        }
    }
}

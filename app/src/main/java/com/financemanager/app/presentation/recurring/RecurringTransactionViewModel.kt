package com.financemanager.app.presentation.recurring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.usecase.recurring.AddRecurringTransactionUseCase
import com.financemanager.app.domain.usecase.recurring.DeleteRecurringTransactionUseCase
import com.financemanager.app.domain.usecase.recurring.GetRecurringTransactionsUseCase
import com.financemanager.app.domain.usecase.recurring.ProcessRecurringTransactionsUseCase
import com.financemanager.app.domain.usecase.recurring.ToggleRecurringActiveUseCase
import com.financemanager.app.domain.usecase.recurring.UpdateRecurringTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing recurring transactions
 */
@HiltViewModel
class RecurringTransactionViewModel @Inject constructor(
    private val getRecurringTransactionsUseCase: GetRecurringTransactionsUseCase,
    private val addRecurringTransactionUseCase: AddRecurringTransactionUseCase,
    private val updateRecurringTransactionUseCase: UpdateRecurringTransactionUseCase,
    private val deleteRecurringTransactionUseCase: DeleteRecurringTransactionUseCase,
    private val toggleRecurringActiveUseCase: ToggleRecurringActiveUseCase,
    private val processRecurringTransactionsUseCase: ProcessRecurringTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecurringTransactionUiState())
    val uiState: StateFlow<RecurringTransactionUiState> = _uiState.asStateFlow()

    private val _events = MutableStateFlow<RecurringTransactionEvent?>(null)
    val events: StateFlow<RecurringTransactionEvent?> = _events.asStateFlow()

    fun loadRecurringTransactions(userId: Long, showAll: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val flow = if (showAll) {
                    getRecurringTransactionsUseCase.getAll(userId)
                } else {
                    getRecurringTransactionsUseCase(userId)
                }
                
                flow.catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load recurring transactions"
                        )
                    }
                }.collect { transactions ->
                    _uiState.update {
                        it.copy(
                            recurringTransactions = transactions,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load recurring transactions"
                    )
                }
            }
        }
    }

    fun addRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, error = null) }
            
            try {
                val id = addRecurringTransactionUseCase(recurringTransaction)
                _uiState.update { it.copy(isProcessing = false) }
                _events.value = RecurringTransactionEvent.TransactionAdded(id)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        error = e.message ?: "Failed to add recurring transaction"
                    )
                }
                _events.value = RecurringTransactionEvent.ShowError(
                    e.message ?: "Failed to add recurring transaction"
                )
            }
        }
    }

    fun updateRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, error = null) }
            
            try {
                updateRecurringTransactionUseCase(recurringTransaction)
                _uiState.update { it.copy(isProcessing = false) }
                _events.value = RecurringTransactionEvent.TransactionUpdated
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        error = e.message ?: "Failed to update recurring transaction"
                    )
                }
                _events.value = RecurringTransactionEvent.ShowError(
                    e.message ?: "Failed to update recurring transaction"
                )
            }
        }
    }

    fun deleteRecurringTransaction(recurringTransaction: RecurringTransaction) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, error = null) }
            
            try {
                deleteRecurringTransactionUseCase(recurringTransaction)
                _uiState.update { it.copy(isProcessing = false) }
                _events.value = RecurringTransactionEvent.TransactionDeleted
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        error = e.message ?: "Failed to delete recurring transaction"
                    )
                }
                _events.value = RecurringTransactionEvent.ShowError(
                    e.message ?: "Failed to delete recurring transaction"
                )
            }
        }
    }

    fun toggleActiveStatus(recurringId: Long, isActive: Boolean) {
        viewModelScope.launch {
            try {
                toggleRecurringActiveUseCase(recurringId, isActive)
                _events.value = RecurringTransactionEvent.ActiveStatusToggled(isActive)
            } catch (e: Exception) {
                _events.value = RecurringTransactionEvent.ShowError(
                    e.message ?: "Failed to toggle active status"
                )
            }
        }
    }

    fun processNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, error = null) }
            
            try {
                val count = processRecurringTransactionsUseCase()
                _uiState.update { it.copy(isProcessing = false) }
                _events.value = RecurringTransactionEvent.TransactionsProcessed(count)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        error = e.message ?: "Failed to process recurring transactions"
                    )
                }
                _events.value = RecurringTransactionEvent.ShowError(
                    e.message ?: "Failed to process recurring transactions"
                )
            }
        }
    }

    fun clearEvent() {
        _events.value = null
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * UI state for recurring transactions screen
 */
data class RecurringTransactionUiState(
    val recurringTransactions: List<RecurringTransaction> = emptyList(),
    val isLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val error: String? = null
)

/**
 * Events for recurring transactions
 */
sealed class RecurringTransactionEvent {
    data class TransactionAdded(val id: Long) : RecurringTransactionEvent()
    object TransactionUpdated : RecurringTransactionEvent()
    object TransactionDeleted : RecurringTransactionEvent()
    data class ActiveStatusToggled(val isActive: Boolean) : RecurringTransactionEvent()
    data class TransactionsProcessed(val count: Int) : RecurringTransactionEvent()
    data class ShowError(val message: String) : RecurringTransactionEvent()
}

package com.financemanager.app.presentation.transaction

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionFilter

/**
 * UI state for Transaction screen
 */
data class TransactionUiState(
    val isLoading: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val error: String? = null,
    val isDialogVisible: Boolean = false,
    val selectedTransaction: Transaction? = null,
    val searchQuery: String = "",
    val currentFilter: TransactionFilter = TransactionFilter.EMPTY,
    val isFilterDialogVisible: Boolean = false
)

/**
 * Events that can be triggered from the Transaction screen
 */
sealed class TransactionEvent {
    data object LoadTransactions : TransactionEvent()
    data object AddTransactionClicked : TransactionEvent()
    data class EditTransactionClicked(val transaction: Transaction) : TransactionEvent()
    data class DeleteTransactionClicked(val transactionId: Long) : TransactionEvent()
    data class SaveTransaction(val transaction: Transaction) : TransactionEvent()
    data class SearchQueryChanged(val query: String) : TransactionEvent()
    data object DismissDialog : TransactionEvent()
    data object ShowFilterDialog : TransactionEvent()
    data class ApplyFilter(val filter: TransactionFilter) : TransactionEvent()
    data object ClearFilters : TransactionEvent()
}

package com.financemanager.app.domain.usecase.recurring

import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.repository.RecurringTransactionRepository
import javax.inject.Inject

/**
 * Use case to update a recurring transaction
 */
class UpdateRecurringTransactionUseCase @Inject constructor(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(recurringTransaction: RecurringTransaction) {
        require(recurringTransaction.description.isNotBlank()) { "Description cannot be empty" }
        require(recurringTransaction.amount > 0) { "Amount must be greater than 0" }
        
        repository.updateRecurringTransaction(recurringTransaction)
    }
}

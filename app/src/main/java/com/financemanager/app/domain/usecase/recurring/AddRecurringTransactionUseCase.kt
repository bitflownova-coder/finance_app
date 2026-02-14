package com.financemanager.app.domain.usecase.recurring

import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.repository.RecurringTransactionRepository
import javax.inject.Inject

/**
 * Use case to add a recurring transaction
 */
class AddRecurringTransactionUseCase @Inject constructor(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(recurringTransaction: RecurringTransaction): Long {
        require(recurringTransaction.description.isNotBlank()) { "Description cannot be empty" }
        require(recurringTransaction.amount > 0) { "Amount must be greater than 0" }
        require(recurringTransaction.startDate <= recurringTransaction.nextOccurrence) { "Start date must be before or equal to next occurrence" }
        
        if (recurringTransaction.endDate != null) {
            require(recurringTransaction.endDate > recurringTransaction.startDate) { "End date must be after start date" }
        }
        
        return repository.addRecurringTransaction(recurringTransaction)
    }
}

package com.financemanager.app.domain.usecase.recurring

import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.repository.RecurringTransactionRepository
import javax.inject.Inject

/**
 * Use case to delete a recurring transaction
 */
class DeleteRecurringTransactionUseCase @Inject constructor(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(recurringTransaction: RecurringTransaction) {
        repository.deleteRecurringTransaction(recurringTransaction)
    }
}

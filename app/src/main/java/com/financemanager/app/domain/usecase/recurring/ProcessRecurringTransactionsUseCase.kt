package com.financemanager.app.domain.usecase.recurring

import com.financemanager.app.domain.repository.RecurringTransactionRepository
import javax.inject.Inject

/**
 * Use case to process due recurring transactions and generate actual transactions
 */
class ProcessRecurringTransactionsUseCase @Inject constructor(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(): Int {
        return repository.processDueRecurringTransactions()
    }
}

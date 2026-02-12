package com.financemanager.app.domain.usecase.recurring

import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.repository.RecurringTransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get active recurring transactions
 */
class GetRecurringTransactionsUseCase @Inject constructor(
    private val repository: RecurringTransactionRepository
) {
    operator fun invoke(userId: Long): Flow<List<RecurringTransaction>> {
        return repository.getActiveRecurringTransactions(userId)
    }
    
    fun getAll(userId: Long): Flow<List<RecurringTransaction>> {
        return repository.getAllRecurringTransactions(userId)
    }
}

package com.financemanager.app.domain.usecase.transaction

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all transactions for a user
 */
class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(userId: Long): Flow<List<Transaction>> {
        return repository.getTransactions(userId)
    }
}

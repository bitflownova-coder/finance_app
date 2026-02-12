package com.financemanager.app.domain.usecase.transaction

import com.financemanager.app.domain.repository.TransactionRepository
import com.financemanager.app.util.Result
import javax.inject.Inject

/**
 * Use case for deleting a transaction
 */
class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactionId: Long): Result<Unit> {
        return try {
            repository.deleteTransaction(transactionId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

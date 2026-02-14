package com.financemanager.app.domain.usecase.transaction

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.repository.TransactionRepository
import com.financemanager.app.util.Result
import javax.inject.Inject

/**
 * Use case for adding a new transaction
 */
class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Result<Long> {
        return try {
            // Validate transaction data
            if (transaction.amount <= 0) {
                return Result.Error(Exception("Amount must be greater than 0"))
            }
            
            if (transaction.description.isBlank()) {
                return Result.Error(Exception("Description is required"))
            }
            
            // Add transaction (includes balance update)
            val transactionId = repository.addTransaction(transaction)
            Result.Success(transactionId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

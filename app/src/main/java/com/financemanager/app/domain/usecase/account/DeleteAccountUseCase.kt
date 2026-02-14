package com.financemanager.app.domain.usecase.account

import com.financemanager.app.domain.repository.BankAccountRepository
import com.financemanager.app.util.Result
import javax.inject.Inject

/**
 * Use case for deleting a bank account
 */
class DeleteAccountUseCase @Inject constructor(
    private val repository: BankAccountRepository
) {
    suspend operator fun invoke(accountId: Long): Result<Unit> {
        return try {
            repository.deleteAccount(accountId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

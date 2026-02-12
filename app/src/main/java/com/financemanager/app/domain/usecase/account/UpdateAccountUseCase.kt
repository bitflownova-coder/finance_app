package com.financemanager.app.domain.usecase.account

import com.financemanager.app.domain.model.BankAccount
import com.financemanager.app.domain.repository.BankAccountRepository
import com.financemanager.app.util.Result
import javax.inject.Inject

/**
 * Use case for updating an existing bank account
 */
class UpdateAccountUseCase @Inject constructor(
    private val repository: BankAccountRepository
) {
    suspend operator fun invoke(account: BankAccount): Result<Unit> {
        return try {
            // Validate account data
            if (account.accountName.isBlank()) {
                return Result.Error(Exception("Account name is required"))
            }
            
            if (account.accountNumber.isBlank()) {
                return Result.Error(Exception("Account number is required"))
            }
            
            if (account.bankName.isBlank()) {
                return Result.Error(Exception("Bank name is required"))
            }
            
            // Update account
            repository.updateAccount(account)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

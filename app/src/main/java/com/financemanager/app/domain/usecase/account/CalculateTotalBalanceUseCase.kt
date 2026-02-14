package com.financemanager.app.domain.usecase.account

import com.financemanager.app.domain.repository.BankAccountRepository
import javax.inject.Inject

/**
 * Use case for calculating total balance across all accounts
 */
class CalculateTotalBalanceUseCase @Inject constructor(
    private val repository: BankAccountRepository
) {
    suspend operator fun invoke(userId: Long): Double {
        return repository.getTotalBalance(userId)
    }
}

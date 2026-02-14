package com.financemanager.app.domain.usecase.transaction

import com.financemanager.app.domain.repository.TransactionRepository
import javax.inject.Inject

/**
 * Use case for getting monthly income
 */
class GetMonthlyIncomeUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(userId: Long, month: Int, year: Int): Double {
        return repository.getMonthlyIncome(userId, month, year)
    }
}

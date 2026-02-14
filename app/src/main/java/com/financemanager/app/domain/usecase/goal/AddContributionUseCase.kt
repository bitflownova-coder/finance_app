package com.financemanager.app.domain.usecase.goal

import com.financemanager.app.domain.repository.SavingsGoalRepository
import javax.inject.Inject

class AddContributionUseCase @Inject constructor(
    private val repository: SavingsGoalRepository
) {
    suspend operator fun invoke(
        goalId: Long,
        amount: Double,
        note: String = "",
        transactionId: Long? = null
    ) {
        require(amount > 0) { "Contribution amount must be greater than 0" }
        
        repository.addContribution(goalId, amount, note, transactionId)
    }
}

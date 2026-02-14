package com.financemanager.app.domain.usecase.goal

import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.domain.repository.SavingsGoalRepository
import javax.inject.Inject

class DeleteGoalUseCase @Inject constructor(
    private val repository: SavingsGoalRepository
) {
    suspend operator fun invoke(goal: SavingsGoal) {
        repository.deleteGoal(goal)
    }
}

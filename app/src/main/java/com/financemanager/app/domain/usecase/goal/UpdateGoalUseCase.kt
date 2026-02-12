package com.financemanager.app.domain.usecase.goal

import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.domain.repository.SavingsGoalRepository
import javax.inject.Inject

class UpdateGoalUseCase @Inject constructor(
    private val repository: SavingsGoalRepository
) {
    suspend operator fun invoke(goal: SavingsGoal) {
        require(goal.name.isNotBlank()) { "Goal name cannot be empty" }
        require(goal.targetAmount > 0) { "Target amount must be greater than 0" }
        
        repository.updateGoal(goal)
    }
}

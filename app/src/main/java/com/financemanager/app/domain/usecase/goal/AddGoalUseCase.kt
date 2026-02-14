package com.financemanager.app.domain.usecase.goal

import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.domain.repository.SavingsGoalRepository
import javax.inject.Inject

class AddGoalUseCase @Inject constructor(
    private val repository: SavingsGoalRepository
) {
    suspend operator fun invoke(goal: SavingsGoal): Long {
        require(goal.name.isNotBlank()) { "Goal name cannot be empty" }
        require(goal.targetAmount > 0) { "Target amount must be greater than 0" }
        require(goal.targetDate.isAfter(java.time.LocalDate.now())) { 
            "Target date must be in the future" 
        }
        
        return repository.insertGoal(goal)
    }
}

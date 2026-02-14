package com.financemanager.app.domain.usecase.goal

import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.domain.repository.SavingsGoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveGoalsUseCase @Inject constructor(
    private val repository: SavingsGoalRepository
) {
    operator fun invoke(userId: Long): Flow<List<SavingsGoal>> {
        return repository.getActiveGoals(userId)
    }
}

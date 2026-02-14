package com.financemanager.app.domain.usecase.goal

import com.financemanager.app.domain.repository.SavingsGoalRepository
import javax.inject.Inject

data class GoalStatistics(
    val totalTargetAmount: Double,
    val totalSavedAmount: Double,
    val totalProgress: Int, // 0-100
    val activeGoalsCount: Int,
    val completedGoalsCount: Int,
    val remainingAmount: Double
)

class GetGoalStatisticsUseCase @Inject constructor(
    private val repository: SavingsGoalRepository
) {
    suspend operator fun invoke(userId: Long): GoalStatistics {
        val totalTarget = repository.getTotalTargetAmount(userId)
        val totalSaved = repository.getTotalSavedAmount(userId)
        val activeCount = repository.getActiveGoalsCount(userId)
        val completedCount = repository.getCompletedGoalsCount(userId)
        
        val progress = if (totalTarget > 0) {
            ((totalSaved / totalTarget) * 100).toInt().coerceIn(0, 100)
        } else 0
        
        val remaining = (totalTarget - totalSaved).coerceAtLeast(0.0)
        
        return GoalStatistics(
            totalTargetAmount = totalTarget,
            totalSavedAmount = totalSaved,
            totalProgress = progress,
            activeGoalsCount = activeCount,
            completedGoalsCount = completedCount,
            remainingAmount = remaining
        )
    }
}

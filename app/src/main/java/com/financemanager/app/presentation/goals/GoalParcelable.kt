package com.financemanager.app.presentation.goals

import android.os.Parcelable
import com.financemanager.app.domain.model.GoalCategory
import com.financemanager.app.domain.model.GoalPriority
import com.financemanager.app.domain.model.GoalStatus
import com.financemanager.app.domain.model.SavingsGoal
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class GoalParcelable(
    val goalId: Long,
    val userId: Long,
    val name: String,
    val description: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDateEpochDay: Long,
    val category: String,
    val priority: String,
    val status: String,
    val createdAt: Long,
    val isArchived: Boolean
) : Parcelable {
    
    fun toGoal(): SavingsGoal {
        return SavingsGoal(
            goalId = goalId,
            userId = userId,
            name = name,
            description = description,
            targetAmount = targetAmount,
            currentAmount = currentAmount,
            targetDate = LocalDate.ofEpochDay(targetDateEpochDay),
            category = GoalCategory.valueOf(category),
            priority = GoalPriority.valueOf(priority),
            status = GoalStatus.valueOf(status),
            createdAt = createdAt,
            isArchived = isArchived
        )
    }
    
    companion object {
        fun from(goal: SavingsGoal): GoalParcelable {
            return GoalParcelable(
                goalId = goal.goalId,
                userId = goal.userId,
                name = goal.name,
                description = goal.description,
                targetAmount = goal.targetAmount,
                currentAmount = goal.currentAmount,
                targetDateEpochDay = goal.targetDate.toEpochDay(),
                category = goal.category.name,
                priority = goal.priority.name,
                status = goal.status.name,
                createdAt = goal.createdAt,
                isArchived = goal.isArchived
            )
        }
    }
}

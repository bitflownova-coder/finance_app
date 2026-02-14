package com.financemanager.app.data.mapper

import com.financemanager.app.data.local.entities.GoalContributionEntity
import com.financemanager.app.data.local.entities.SavingsGoalEntity
import com.financemanager.app.domain.model.GoalCategory
import com.financemanager.app.domain.model.GoalContribution
import com.financemanager.app.domain.model.GoalPriority
import com.financemanager.app.domain.model.GoalStatus
import com.financemanager.app.domain.model.SavingsGoal
import java.time.LocalDate

object SavingsGoalMapper {
    
    fun toDomain(entity: SavingsGoalEntity): SavingsGoal {
        return SavingsGoal(
            goalId = entity.goalId,
            userId = entity.userId,
            name = entity.name,
            description = entity.description,
            targetAmount = entity.targetAmount,
            currentAmount = entity.currentAmount,
            targetDate = LocalDate.ofEpochDay(entity.targetDate),
            category = GoalCategory.valueOf(entity.category),
            priority = GoalPriority.valueOf(entity.priority),
            status = GoalStatus.valueOf(entity.status),
            createdAt = entity.createdAt,
            isArchived = entity.isArchived
        )
    }
    
    fun toEntity(domain: SavingsGoal): SavingsGoalEntity {
        return SavingsGoalEntity(
            goalId = domain.goalId,
            userId = domain.userId,
            name = domain.name,
            description = domain.description,
            targetAmount = domain.targetAmount,
            currentAmount = domain.currentAmount,
            targetDate = domain.targetDate.toEpochDay(),
            category = domain.category.name,
            priority = domain.priority.name,
            status = domain.status.name,
            createdAt = domain.createdAt,
            isArchived = domain.isArchived
        )
    }
    
    fun contributionToDomain(entity: GoalContributionEntity): GoalContribution {
        return GoalContribution(
            contributionId = entity.contributionId,
            goalId = entity.goalId,
            amount = entity.amount,
            note = entity.note,
            timestamp = entity.timestamp,
            transactionId = entity.transactionId
        )
    }
    
    fun contributionToEntity(domain: GoalContribution): GoalContributionEntity {
        return GoalContributionEntity(
            contributionId = domain.contributionId,
            goalId = domain.goalId,
            amount = domain.amount,
            note = domain.note,
            timestamp = domain.timestamp,
            transactionId = domain.transactionId
        )
    }
}

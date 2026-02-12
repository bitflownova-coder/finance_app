package com.financemanager.app.data.mapper

import com.financemanager.app.data.local.entities.BudgetEntity
import com.financemanager.app.domain.model.Budget
import com.financemanager.app.domain.model.BudgetPeriodType
import com.financemanager.app.domain.model.TransactionCategory

/**
 * Mapper to convert between BudgetEntity (data layer) and Budget (domain layer)
 */
object BudgetMapper {
    
    fun toDomain(entity: BudgetEntity): Budget {
        return Budget(
            budgetId = entity.budgetId,
            userId = entity.userId,
            budgetName = entity.budgetName,
            totalBudget = entity.totalBudget,
            spentAmount = entity.spentAmount,
            periodType = BudgetPeriodType.fromString(entity.periodType),
            periodStart = entity.periodStart,
            periodEnd = entity.periodEnd,
            category = entity.category?.let { TransactionCategory.fromString(it) },
            alertThreshold = entity.alertThreshold,
            isActive = entity.isActive,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(budget: Budget): BudgetEntity {
        return BudgetEntity(
            budgetId = budget.budgetId,
            userId = budget.userId,
            budgetName = budget.budgetName,
            totalBudget = budget.totalBudget,
            spentAmount = budget.spentAmount,
            periodType = budget.periodType.name,
            periodStart = budget.periodStart,
            periodEnd = budget.periodEnd,
            category = budget.category?.name,
            alertThreshold = budget.alertThreshold,
            isActive = budget.isActive,
            createdAt = budget.createdAt,
            updatedAt = budget.updatedAt
        )
    }
}

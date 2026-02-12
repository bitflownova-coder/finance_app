package com.financemanager.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model for Budget
 */
@Parcelize
data class Budget(
    val budgetId: Long = 0,
    val userId: Long,
    val budgetName: String,
    val totalBudget: Double,
    val spentAmount: Double = 0.0,
    val periodType: BudgetPeriodType,
    val periodStart: Long,
    val periodEnd: Long,
    val category: TransactionCategory? = null,
    val alertThreshold: Double = 0.8,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable {
    val remainingAmount: Double
        get() = totalBudget - spentAmount
    
    val percentageUsed: Double
        get() = if (totalBudget > 0) (spentAmount / totalBudget) * 100 else 0.0
    
    val isExceeded: Boolean
        get() = spentAmount >= totalBudget
    
    val isNearLimit: Boolean
        get() = spentAmount >= (totalBudget * alertThreshold)
}

enum class BudgetPeriodType {
    MONTHLY,
    YEARLY;
    
    companion object {
        fun fromString(value: String): BudgetPeriodType {
            return values().find { it.name == value.uppercase() } ?: MONTHLY
        }
    }
}

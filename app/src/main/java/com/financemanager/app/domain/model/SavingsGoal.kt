package com.financemanager.app.domain.model

import java.time.LocalDate

/**
 * Domain model representing a savings goal.
 */
data class SavingsGoal(
    val goalId: Long = 0,
    val userId: Long,
    val name: String,
    val description: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val targetDate: LocalDate,
    val category: GoalCategory,
    val priority: GoalPriority,
    val status: GoalStatus,
    val createdAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
) {
    /**
     * Progress percentage (0-100)
     */
    val progressPercentage: Int
        get() = if (targetAmount > 0) {
            ((currentAmount / targetAmount) * 100).coerceIn(0.0, 100.0).toInt()
        } else 0

    /**
     * Remaining amount to reach goal
     */
    val remainingAmount: Double
        get() = (targetAmount - currentAmount).coerceAtLeast(0.0)

    /**
     * Days remaining until target date
     */
    val daysRemaining: Long
        get() = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), targetDate)

    /**
     * Suggested monthly contribution to reach goal
     */
    val suggestedMonthlyContribution: Double
        get() {
            val monthsRemaining = daysRemaining / 30.0
            return if (monthsRemaining > 0) remainingAmount / monthsRemaining else remainingAmount
        }

    /**
     * Check if goal is overdue
     */
    val isOverdue: Boolean
        get() = LocalDate.now().isAfter(targetDate) && currentAmount < targetAmount

    /**
     * Check if goal is achieved
     */
    val isAchieved: Boolean
        get() = currentAmount >= targetAmount
}

/**
 * Goal categories for organization
 */
enum class GoalCategory(val displayName: String, val icon: String) {
    EMERGENCY_FUND("Emergency Fund", "🛡️"),
    VACATION("Vacation", "✈️"),
    GADGET("Gadget/Electronics", "📱"),
    VEHICLE("Vehicle", "🚗"),
    HOME("Home/Property", "🏠"),
    EDUCATION("Education", "🎓"),
    WEDDING("Wedding", "💍"),
    RETIREMENT("Retirement", "🌴"),
    INVESTMENT("Investment", "💰"),
    DEBT_PAYOFF("Debt Payoff", "💳"),
    OTHER("Other", "🎯")
}

/**
 * Priority levels for goals
 */
enum class GoalPriority(val displayName: String) {
    HIGH("High Priority"),
    MEDIUM("Medium Priority"),
    LOW("Low Priority")
}

/**
 * Goal status tracking
 */
enum class GoalStatus(val displayName: String) {
    ACTIVE("Active"),
    PAUSED("Paused"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled")
}

/**
 * Goal contribution record
 */
data class GoalContribution(
    val contributionId: Long = 0,
    val goalId: Long,
    val amount: Double,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val transactionId: Long? = null // Optional link to transaction
)

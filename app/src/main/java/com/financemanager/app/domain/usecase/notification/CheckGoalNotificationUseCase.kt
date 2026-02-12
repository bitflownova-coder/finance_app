package com.financemanager.app.domain.usecase.notification

import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.util.NotificationHelper
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

/**
 * Use case to check goal progress and trigger notifications
 */
class CheckGoalNotificationUseCase @Inject constructor(
    private val notificationHelper: NotificationHelper
) {
    
    operator fun invoke(goal: SavingsGoal) {
        when {
            // Goal achieved
            goal.isAchieved && goal.status == com.financemanager.app.domain.model.GoalStatus.ACTIVE -> {
                notificationHelper.showGoalAchievedNotification(
                    goalName = goal.name,
                    targetAmount = formatCurrency(goal.targetAmount)
                )
            }
            
            // Goal due soon (within 7 days) and not achieved
            goal.daysRemaining in 1..7 && !goal.isAchieved -> {
                val dailyNeeded = goal.remainingAmount / goal.daysRemaining
                notificationHelper.showGoalDueSoonNotification(
                    goalName = goal.name,
                    daysLeft = goal.daysRemaining.toInt(),
                    remainingAmount = formatCurrency(goal.remainingAmount),
                    suggestedAmount = formatCurrency(dailyNeeded)
                )
            }
        }
    }
    
    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }
}

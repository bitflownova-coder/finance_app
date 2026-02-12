package com.financemanager.app.domain.usecase.notification

import com.financemanager.app.domain.model.Budget
import com.financemanager.app.util.NotificationHelper
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

/**
 * Use case to check budget status and trigger notifications
 */
class CheckBudgetNotificationUseCase @Inject constructor(
    private val notificationHelper: NotificationHelper
) {
    
    operator fun invoke(budget: Budget) {
        val percentage = budget.percentageUsed.toInt()
        val spentAmount = formatCurrency(budget.spentAmount)
        val limitAmount = formatCurrency(budget.totalBudget)
        val categoryName = budget.category?.displayName ?: "General"
        
        when {
            // Exceeded budget (100%+)
            percentage >= 100 -> {
                val exceededBy = formatCurrency(budget.spentAmount - budget.totalBudget)
                notificationHelper.showBudgetExceededNotification(
                    categoryName = categoryName,
                    spentAmount = spentAmount,
                    limitAmount = limitAmount,
                    exceededBy = exceededBy
                )
            }
            
            // Warning threshold (80-99%)
            percentage >= 80 -> {
                notificationHelper.showBudgetWarningNotification(
                    categoryName = categoryName,
                    spentAmount = spentAmount,
                    limitAmount = limitAmount,
                    percentage = percentage
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

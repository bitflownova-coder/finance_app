package com.financemanager.app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.financemanager.app.R
import com.financemanager.app.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        // Notification Channels
        const val CHANNEL_BUDGET_ID = "budget_alerts"
        const val CHANNEL_TRANSACTION_ID = "transaction_alerts"
        const val CHANNEL_GOAL_ID = "goal_alerts"
        const val CHANNEL_RECURRING_ID = "recurring_alerts"
        const val CHANNEL_GENERAL_ID = "general_alerts"
        
        // Notification IDs
        const val NOTIFICATION_ID_BUDGET_WARNING = 1001
        const val NOTIFICATION_ID_BUDGET_EXCEEDED = 1002
        const val NOTIFICATION_ID_TRANSACTION = 1003
        const val NOTIFICATION_ID_GOAL_ACHIEVED = 1004
        const val NOTIFICATION_ID_GOAL_DUE_SOON = 1005
        const val NOTIFICATION_ID_RECURRING = 1006
    }
    
    init {
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_BUDGET_ID,
                    "Budget Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for budget limits and warnings"
                    enableLights(true)
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_TRANSACTION_ID,
                    "Transaction Updates",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications for transactions and balance updates"
                },
                NotificationChannel(
                    CHANNEL_GOAL_ID,
                    "Goal Achievements",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for savings goal milestones"
                    enableLights(true)
                },
                NotificationChannel(
                    CHANNEL_RECURRING_ID,
                    "Recurring Transactions",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notifications for recurring transactions"
                },
                NotificationChannel(
                    CHANNEL_GENERAL_ID,
                    "General Notifications",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "General app notifications"
                }
            )
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channels.forEach { notificationManager.createNotificationChannel(it) }
        }
    }
    
    /**
     * Show budget warning notification (80% threshold)
     */
    fun showBudgetWarningNotification(categoryName: String, spentAmount: String, limitAmount: String, percentage: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_BUDGET_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: Replace with proper icon
            .setContentTitle("⚠️ Budget Alert: $categoryName")
            .setContentText("You've spent $spentAmount of $limitAmount ($percentage%)")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("You've spent $spentAmount out of your $limitAmount budget for $categoryName ($percentage%). Consider reducing spending in this category."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_BUDGET_WARNING, notification)
    }
    
    /**
     * Show budget exceeded notification (100% threshold)
     */
    fun showBudgetExceededNotification(categoryName: String, spentAmount: String, limitAmount: String, exceededBy: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_BUDGET_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🚨 Budget Exceeded: $categoryName")
            .setContentText("You've exceeded your budget by $exceededBy!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("You've spent $spentAmount, which exceeds your $limitAmount budget for $categoryName by $exceededBy. Review your spending immediately."))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_BUDGET_EXCEEDED, notification)
    }
    
    /**
     * Show transaction added notification
     */
    fun showTransactionNotification(type: String, amount: String, category: String, remainingBalance: String) {
        val emoji = if (type == "DEBIT") "💸" else "💰"
        val action = if (type == "DEBIT") "spent" else "received"
        
        val notification = NotificationCompat.Builder(context, CHANNEL_TRANSACTION_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("$emoji Transaction $action")
            .setContentText("$amount for $category")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("You've $action $amount for $category. Remaining balance: $remainingBalance"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_TRANSACTION, notification)
    }
    
    /**
     * Show goal achieved notification
     */
    fun showGoalAchievedNotification(goalName: String, targetAmount: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_GOAL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🎉 Goal Achieved!")
            .setContentText("Congratulations! You've reached your $goalName goal!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("🎉 Congratulations! You've successfully saved $targetAmount and achieved your \"$goalName\" goal. Great job on staying committed to your financial goals!"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_GOAL_ACHIEVED, notification)
    }
    
    /**
     * Show goal due soon notification (within 7 days)
     */
    fun showGoalDueSoonNotification(goalName: String, daysLeft: Int, remainingAmount: String, suggestedAmount: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_GOAL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("⏰ Goal Deadline Approaching")
            .setContentText("$goalName is due in $daysLeft days")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Your \"$goalName\" goal is due in $daysLeft days. You still need $remainingAmount. Suggested daily contribution: $suggestedAmount"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_GOAL_DUE_SOON, notification)
    }
    
    /**
     * Show recurring transaction processed notification
     */
    fun showRecurringTransactionNotification(description: String, amount: String, frequency: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_RECURRING_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🔄 Recurring Transaction Processed")
            .setContentText("$description: $amount")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Your $frequency recurring transaction \"$description\" of $amount has been automatically processed."))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()
        
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_RECURRING, notification)
    }
    
    /**
     * Show monthly report available notification
     */
    fun showMonthlyReportNotification(month: String, income: String, expenses: String, savings: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_GENERAL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("📊 Monthly Report Available")
            .setContentText("Your $month financial summary is ready")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Your $month report: Income: $income | Expenses: $expenses | Savings: $savings. Tap to view detailed analysis."))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .setContentIntent(createPendingIntent())
            .build()
        
        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
    }
    
    /**
     * Check if notifications are enabled
     */
    fun areNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
    
    /**
     * Cancel a specific notification
     */
    fun cancelNotification(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }
    
    /**
     * Cancel all notifications
     */
    fun cancelAllNotifications() {
        NotificationManagerCompat.from(context).cancelAll()
    }
    
    /**
     * Create pending intent to open main activity
     */
    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        
        return PendingIntent.getActivity(context, 0, intent, flags)
    }
}

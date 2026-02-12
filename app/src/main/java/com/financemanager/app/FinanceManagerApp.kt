package com.financemanager.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.financemanager.app.util.NotificationScheduler
import com.financemanager.app.worker.RecurringTransactionWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Application class for Finance Manager
 * Initializes Hilt dependency injection, notification channels, and background workers
 */
@HiltAndroidApp
class FinanceManagerApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        scheduleRecurringTransactionWorker()
        notificationScheduler.scheduleNotificationChecks()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    /**
     * Schedule periodic worker to process recurring transactions
     * Runs once daily at approximately the same time
     */
    private fun scheduleRecurringTransactionWorker() {
        val workRequest = PeriodicWorkRequestBuilder<RecurringTransactionWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            RecurringTransactionWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP, // Keep existing work if already scheduled
            workRequest
        )
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_TRANSACTIONS,
                    "Transactions",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Transaction notifications"
                },
                NotificationChannel(
                    CHANNEL_BUDGET,
                    "Budget Alerts",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Budget alert notifications"
                },
                NotificationChannel(
                    CHANNEL_REMINDERS,
                    "Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Payment and bill reminders"
                },
                NotificationChannel(
                    CHANNEL_RECURRING,
                    "Recurring Transactions",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Recurring transaction processing notifications"
                },
                NotificationChannel(
                    CHANNEL_GOALS,
                    "Savings Goals",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Savings goal achievements and reminders"
                }
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            channels.forEach { channel ->
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }

    companion object {
        const val CHANNEL_TRANSACTIONS = "transactions_channel"
        const val CHANNEL_BUDGET = "budget_channel"
        const val CHANNEL_REMINDERS = "reminders_channel"
        const val CHANNEL_RECURRING = "recurring_channel"
        const val CHANNEL_GOALS = "goals_channel"
    }
}

package com.financemanager.app.util

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.financemanager.app.worker.NotificationWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Schedules periodic notification checks
 */
@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val workManager = WorkManager.getInstance(context)
    
    companion object {
        private const val NOTIFICATION_WORK_NAME = "notification_check_work"
        private const val NOTIFICATION_CHECK_INTERVAL_HOURS = 6L
    }
    
    /**
     * Schedule periodic notification checks
     */
    fun scheduleNotificationChecks() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()
        
        val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            NOTIFICATION_CHECK_INTERVAL_HOURS,
            TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }
    
    /**
     * Cancel notification checks
     */
    fun cancelNotificationChecks() {
        workManager.cancelUniqueWork(NOTIFICATION_WORK_NAME)
    }
}

package com.financemanager.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.financemanager.app.domain.usecase.recurring.ProcessRecurringTransactionsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Background worker to process recurring transactions automatically
 * Runs daily to check for due recurring transactions and generate actual transactions
 */
@HiltWorker
class RecurringTransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val processRecurringTransactionsUseCase: ProcessRecurringTransactionsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Process all due recurring transactions
            val generatedCount = processRecurringTransactionsUseCase()
            
            // Log success
            android.util.Log.d(
                TAG,
                "RecurringTransactionWorker: Successfully processed $generatedCount transactions"
            )
            
            // TODO: Show notification if transactions were generated
            // if (generatedCount > 0) {
            //     showNotification(generatedCount)
            // }
            
            Result.success()
        } catch (e: Exception) {
            android.util.Log.e(
                TAG,
                "RecurringTransactionWorker: Failed to process recurring transactions",
                e
            )
            
            // Retry with exponential backoff
            if (runAttemptCount < MAX_RETRY_ATTEMPTS) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    // TODO: Add notification when implementing Phase 14 (notifications)
    // private fun showNotification(count: Int) {
    //     val notificationManager = applicationContext.getSystemService(
    //         Context.NOTIFICATION_SERVICE
    //     ) as NotificationManager
    //     
    //     // Create notification channel (Android O+)
    //     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //         val channel = NotificationChannel(
    //             CHANNEL_ID,
    //             "Recurring Transactions",
    //             NotificationManager.IMPORTANCE_DEFAULT
    //         )
    //         notificationManager.createNotificationChannel(channel)
    //     }
    //     
    //     // Build notification
    //     val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
    //         .setSmallIcon(R.drawable.ic_notification)
    //         .setContentTitle("Recurring Transactions Processed")
    //         .setContentText("$count transaction(s) added automatically")
    //         .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    //         .setAutoCancel(true)
    //         .build()
    //     
    //     notificationManager.notify(NOTIFICATION_ID, notification)
    // }

    companion object {
        private const val TAG = "RecurringTransactionWorker"
        private const val MAX_RETRY_ATTEMPTS = 3
        private const val CHANNEL_ID = "recurring_transactions_channel"
        private const val NOTIFICATION_ID = 1001
        
        const val WORK_NAME = "recurring_transaction_processing"
    }
}

package com.financemanager.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.financemanager.app.domain.repository.BudgetRepository
import com.financemanager.app.domain.repository.SavingsGoalRepository
import com.financemanager.app.util.NotificationHelper
import com.financemanager.app.util.SessionManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.NumberFormat
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Background worker to check for notification triggers
 * Runs periodically to check budgets and goals
 */
@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val budgetRepository: BudgetRepository,
    private val savingsGoalRepository: SavingsGoalRepository,
    private val sessionManager: SessionManager,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {
    
    override suspend fun doWork(): Result {
        return try {
            val userId = sessionManager.getUserId() ?: 0L
            if (userId == 0L) {
                return Result.success()
            }
            
            // Check budgets
            checkBudgetAlerts(userId)
            
            // Check goals
            checkGoalAlerts(userId)
            
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
    
    private suspend fun checkBudgetAlerts(userId: Long) {
        // This would require collecting Flow, which is async
        // In practice, you'd need to get a snapshot or use a suspend function
        // For now, skip or implement with proper Flow collection
        // Future enhancement: Add budget alert checking logic
    }
    
    private suspend fun checkGoalAlerts(userId: Long) {
        // Get goals that are due soon (within 7 days)
        val goals = savingsGoalRepository.getActiveGoals(userId)
        
        // Note: Since getActiveGoals returns Flow, we can't directly use it in Worker
        // Future enhancement: Add suspend functions in repository for one-time checks
        // Or collect flow in a coroutine scope
        
        // For now, this is a placeholder for the structure
    }
    
    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        formatter.maximumFractionDigits = 0
        return formatter.format(amount)
    }
}

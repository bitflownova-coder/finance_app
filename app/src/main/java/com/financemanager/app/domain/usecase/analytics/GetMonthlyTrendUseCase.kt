package com.financemanager.app.domain.usecase.analytics

import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

/**
 * Use case for getting monthly income vs expense trend
 * Returns list of months with income and expense totals
 */
class GetMonthlyTrendUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    data class MonthlyData(
        val month: Int,
        val year: Int,
        val income: Double,
        val expense: Double,
        val monthName: String
    ) {
        val net: Double get() = income - expense
    }
    
    suspend operator fun invoke(userId: Long, numberOfMonths: Int = 6): List<MonthlyData> {
        val result = mutableListOf<MonthlyData>()
        val calendar = Calendar.getInstance()
        
        // Start from current month and go back
        for (i in 0 until numberOfMonths) {
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)
            
            // Get start and end of month
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            val startTime = calendar.timeInMillis
            
            calendar.add(Calendar.MONTH, 1)
            val endTime = calendar.timeInMillis
            
            // Get transactions for this month
            val transactions = transactionRepository.getTransactionsByDateRange(userId, startTime, endTime).first()
            
            val income = transactions
                .filter { it.transactionType == com.financemanager.app.domain.model.TransactionType.CREDIT }
                .sumOf { it.amount }
            
            val expense = transactions
                .filter { it.transactionType == com.financemanager.app.domain.model.TransactionType.DEBIT }
                .sumOf { it.amount }
            
            val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
                ?: "Month"
            
            result.add(0, MonthlyData(month, year, income, expense, monthName))
            
            // Move to previous month
            calendar.add(Calendar.MONTH, -2)
        }
        
        return result
    }
}

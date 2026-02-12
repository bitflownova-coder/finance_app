package com.financemanager.app.domain.usecase.analytics

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionType
import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

/**
 * Use case for getting financial summary statistics
 */
class GetFinancialSummaryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    data class FinancialSummary(
        val totalIncome: Double,
        val totalExpense: Double,
        val netSavings: Double,
        val savingsRate: Double,
        val averageDailyExpense: Double,
        val largestExpense: Transaction?,
        val largestIncome: Transaction?,
        val transactionCount: Int
    )
    
    suspend operator fun invoke(userId: Long, month: Int, year: Int): FinancialSummary {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startTime = calendar.timeInMillis
        
        calendar.add(Calendar.MONTH, 1)
        val endTime = calendar.timeInMillis
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        
        val transactions = transactionRepository.getTransactionsByDateRange(userId, startTime, endTime).first()
        
        val income = transactions.filter { it.transactionType == TransactionType.CREDIT }
        val expenses = transactions.filter { it.transactionType == TransactionType.DEBIT }
        
        val totalIncome = income.sumOf { it.amount }
        val totalExpense = expenses.sumOf { it.amount }
        val netSavings = totalIncome - totalExpense
        val savingsRate = if (totalIncome > 0) (netSavings / totalIncome) * 100 else 0.0
        val averageDailyExpense = totalExpense / daysInMonth
        
        return FinancialSummary(
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            netSavings = netSavings,
            savingsRate = savingsRate,
            averageDailyExpense = averageDailyExpense,
            largestExpense = expenses.maxByOrNull { it.amount },
            largestIncome = income.maxByOrNull { it.amount },
            transactionCount = transactions.size
        )
    }
}

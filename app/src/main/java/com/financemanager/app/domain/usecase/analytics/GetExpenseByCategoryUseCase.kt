package com.financemanager.app.domain.usecase.analytics

import com.financemanager.app.domain.model.TransactionCategory
import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

/**
 * Use case for getting expense breakdown by category
 * Returns map of category to total amount spent
 */
class GetExpenseByCategoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(userId: Long, month: Int, year: Int): Map<TransactionCategory, Double> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startTime = calendar.timeInMillis
        
        calendar.add(Calendar.MONTH, 1)
        val endTime = calendar.timeInMillis
        
        val transactions = transactionRepository.getTransactionsByDateRange(userId, startTime, endTime).first()
        
        // Group by category and sum amounts for DEBIT transactions
        return transactions
            .filter { it.transactionType == com.financemanager.app.domain.model.TransactionType.DEBIT }
            .groupBy { it.category }
            .mapValues { (_, txns) -> 
                txns.sumOf { it.amount }
            }
    }
    
    suspend fun getYearlyExpenseByCategory(userId: Long, year: Int): Map<TransactionCategory, Double> {
        val calendar = Calendar.getInstance()
        calendar.set(year, 0, 1, 0, 0, 0)
        val startTime = calendar.timeInMillis
        
        calendar.add(Calendar.YEAR, 1)
        val endTime = calendar.timeInMillis
        
        val transactions = transactionRepository.getTransactionsByDateRange(userId, startTime, endTime).first()
        
        return transactions
            .filter { it.transactionType == com.financemanager.app.domain.model.TransactionType.DEBIT }
            .groupBy { it.category }
            .mapValues { (_, txns) -> 
                txns.sumOf { it.amount }
            }
    }
}

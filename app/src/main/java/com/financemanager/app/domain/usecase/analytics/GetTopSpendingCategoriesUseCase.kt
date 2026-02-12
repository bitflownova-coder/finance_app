package com.financemanager.app.domain.usecase.analytics

import com.financemanager.app.domain.model.TransactionType
import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.util.*
import javax.inject.Inject

/**
 * Use case for getting top spending categories
 * Returns list of categories sorted by amount spent (highest first)
 */
class GetTopSpendingCategoriesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    data class CategorySpending(
        val categoryName: String,
        val amount: Double,
        val percentage: Double,
        val transactionCount: Int
    )
    
    suspend operator fun invoke(userId: Long, month: Int, year: Int, limit: Int = 5): List<CategorySpending> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startTime = calendar.timeInMillis
        
        calendar.add(Calendar.MONTH, 1)
        val endTime = calendar.timeInMillis
        
        val transactions = transactionRepository.getTransactionsByDateRange(userId, startTime, endTime).first()
        
        val expenses = transactions.filter { it.transactionType == TransactionType.DEBIT }
        val totalExpense = expenses.sumOf { it.amount }
        
        if (totalExpense == 0.0) {
            return emptyList()
        }
        
        return expenses
            .groupBy { it.category }
            .map { (category, categoryTransactions) ->
                val amount = categoryTransactions.sumOf { it.amount }
                CategorySpending(
                    categoryName = category.displayName,
                    amount = amount,
                    percentage = (amount / totalExpense) * 100,
                    transactionCount = categoryTransactions.size
                )
            }
            .sortedByDescending { it.amount }
            .take(limit)
    }
}

package com.financemanager.app.domain.usecase

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Use case for learning from user's transaction history
 * Analyzes patterns to improve category predictions
 */
class LearnFromTransactionHistoryUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(userId: Long): Map<String, List<String>> {
        val transactions = transactionRepository.getTransactions(userId).first()
        
        // Group transactions by category
        val categoryPatterns = mutableMapOf<String, MutableSet<String>>()
        
        transactions.forEach { transaction ->
            val keywords = extractKeywords(transaction.description)
            categoryPatterns.getOrPut(transaction.category.displayName) { mutableSetOf() }
                .addAll(keywords)
        }
        
        return categoryPatterns.mapValues { it.value.toList() }
    }
    
    private fun extractKeywords(description: String): List<String> {
        // Simple keyword extraction - split by spaces and filter
        return description.lowercase()
            .split(Regex("\\s+"))
            .filter { it.length > 3 } // Minimum word length
            .distinct()
    }
    
    /**
     * Get most common category for similar descriptions
     */
    suspend fun getMostCommonCategory(
        userId: Long,
        description: String
    ): String? {
        val transactions = transactionRepository.getTransactions(userId).first()
        val keywords = extractKeywords(description)
        
        if (keywords.isEmpty()) return null
        
        // Find transactions with similar keywords
        val similarTransactions = transactions.filter { transaction ->
            val transactionKeywords = extractKeywords(transaction.description)
            transactionKeywords.any { it in keywords }
        }
        
        // Return most common category
        return similarTransactions
            .groupBy { it.category.displayName }
            .maxByOrNull { it.value.size }
            ?.key
    }
}

package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.CategoryInsight
import com.financemanager.app.domain.model.Insight
import com.financemanager.app.domain.model.MonthlySummary
import com.financemanager.app.domain.model.SpendingPattern
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for financial insights and analytics
 */
interface InsightRepository {
    
    /**
     * Generate personalized insights for user
     */
    suspend fun generateInsights(userId: Long): List<Insight>
    
    /**
     * Get spending patterns by category
     */
    suspend fun getSpendingPatterns(userId: Long, startDate: Long, endDate: Long): List<SpendingPattern>
    
    /**
     * Get monthly summary
     */
    suspend fun getMonthlySummary(userId: Long, month: Int, year: Int): MonthlySummary
    
    /**
     * Get category-wise insights
     */
    suspend fun getCategoryInsights(userId: Long, month: Int, year: Int): List<CategoryInsight>
    
    /**
     * Predict next month's expenses based on historical data
     */
    suspend fun predictNextMonthExpense(userId: Long): Double
    
    /**
     * Get spending trend over time
     */
    fun getSpendingTrend(userId: Long, months: Int): Flow<List<Pair<String, Double>>>
    
    /**
     * Detect unusual transactions
     */
    suspend fun detectUnusualActivity(userId: Long): List<Insight>
    
    /**
     * Get savings opportunities
     */
    suspend fun getSavingsOpportunities(userId: Long): List<Insight>
}

package com.financemanager.app.domain.usecase.insights

import com.financemanager.app.domain.model.CategoryInsight
import com.financemanager.app.domain.repository.InsightRepository
import javax.inject.Inject

/**
 * Use case to get category insights
 */
class GetCategoryInsightsUseCase @Inject constructor(
    private val repository: InsightRepository
) {
    suspend operator fun invoke(userId: Long, month: Int, year: Int): List<CategoryInsight> {
        return repository.getCategoryInsights(userId, month, year)
    }
}

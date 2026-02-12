package com.financemanager.app.domain.usecase.insights

import com.financemanager.app.domain.model.Insight
import com.financemanager.app.domain.repository.InsightRepository
import javax.inject.Inject

/**
 * Use case to generate financial insights
 */
class GenerateInsightsUseCase @Inject constructor(
    private val repository: InsightRepository
) {
    suspend operator fun invoke(userId: Long): List<Insight> {
        return repository.generateInsights(userId)
    }
}

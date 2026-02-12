package com.financemanager.app.domain.model

/**
 * Category prediction result with confidence score
 */
data class CategoryPrediction(
    val category: String,
    val confidence: Float,
    val matchedKeywords: List<String> = emptyList()
)

/**
 * Category pattern for machine learning
 */
data class CategoryPattern(
    val category: String,
    val keywords: List<String>,
    val weight: Float = 1.0f
)

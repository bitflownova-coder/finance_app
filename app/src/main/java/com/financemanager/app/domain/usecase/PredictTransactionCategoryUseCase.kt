package com.financemanager.app.domain.usecase

import com.financemanager.app.domain.model.CategoryPattern
import com.financemanager.app.domain.model.CategoryPrediction
import javax.inject.Inject

/**
 * Use case for predicting transaction category based on description
 * Uses keyword matching with confidence scoring
 * In production, this would use TensorFlow Lite with a trained model
 */
class PredictTransactionCategoryUseCase @Inject constructor() {
    
    private val categoryPatterns = listOf(
        // Food & Dining
        CategoryPattern(
            "Food & Dining",
            listOf(
                "restaurant", "cafe", "coffee", "starbucks", "mcdonald", "pizza", "burger",
                "food", "lunch", "dinner", "breakfast", "zomato", "swiggy", "dominos",
                "kfc", "subway", "dunkin", "dining", "meal", "eat", "snack"
            ),
            weight = 1.0f
        ),
        
        // Shopping
        CategoryPattern(
            "Shopping",
            listOf(
                "amazon", "flipkart", "myntra", "ajio", "mall", "store", "shop",
                "purchase", "buy", "retail", "market", "clothing", "fashion",
                "electronics", "apparels", "accessories", "supermarket"
            ),
            weight = 1.0f
        ),
        
        // Transportation
        CategoryPattern(
            "Transportation",
            listOf(
                "uber", "ola", "cab", "taxi", "metro", "bus", "train", "petrol",
                "fuel", "parking", "toll", "rapido", "auto", "transport", "travel",
                "railway", "flight", "ticket"
            ),
            weight = 1.0f
        ),
        
        // Entertainment
        CategoryPattern(
            "Entertainment",
            listOf(
                "movie", "cinema", "netflix", "spotify", "amazon prime", "hotstar",
                "entertainment", "theatre", "pvr", "inox", "concert", "show",
                "game", "gaming", "youtube premium", "subscription"
            ),
            weight = 1.0f
        ),
        
        // Utilities
        CategoryPattern(
            "Utilities",
            listOf(
                "electricity", "water", "gas", "internet", "wifi", "broadband",
                "mobile recharge", "phone bill", "utility", "bill payment",
                "airtel", "jio", "vodafone", "bsnl", "tata sky"
            ),
            weight = 1.0f
        ),
        
        // Healthcare
        CategoryPattern(
            "Healthcare",
            listOf(
                "hospital", "doctor", "clinic", "medicine", "pharmacy", "medical",
                "health", "apollo", "max", "fortis", "lab", "diagnostic",
                "prescription", "consultation", "treatment", "insurance"
            ),
            weight = 1.0f
        ),
        
        // Groceries
        CategoryPattern(
            "Groceries",
            listOf(
                "grocery", "supermarket", "blinkit", "grofers", "bigbasket",
                "dmart", "reliance fresh", "more", "vegetables", "fruits",
                "provisions", "dairy", "essentials"
            ),
            weight = 1.0f
        ),
        
        // Education
        CategoryPattern(
            "Education",
            listOf(
                "school", "college", "university", "course", "tuition", "book",
                "education", "study", "learning", "training", "coaching",
                "udemy", "coursera", "byju", "unacademy", "fees"
            ),
            weight = 1.0f
        ),
        
        // Investment
        CategoryPattern(
            "Investment",
            listOf(
                "stock", "mutual fund", "investment", "shares", "equity", "sip",
                "zerodha", "groww", "upstox", "etmoney", "trading", "portfolio",
                "gold", "fd", "ppf", "nps"
            ),
            weight = 1.0f
        ),
        
        // Salary/Income
        CategoryPattern(
            "Salary",
            listOf(
                "salary", "income", "payment received", "credit", "earning",
                "wages", "compensation", "bonus", "incentive"
            ),
            weight = 1.0f
        )
    )
    
    operator fun invoke(description: String, amount: Double? = null): List<CategoryPrediction> {
        val normalizedDescription = description.lowercase().trim()
        
        if (normalizedDescription.isEmpty()) {
            return emptyList()
        }
        
        val predictions = mutableMapOf<String, MutableList<String>>()
        
        // Match keywords
        for (pattern in categoryPatterns) {
            val matchedKeywords = pattern.keywords.filter { keyword ->
                normalizedDescription.contains(keyword.lowercase())
            }
            
            if (matchedKeywords.isNotEmpty()) {
                predictions.getOrPut(pattern.category) { mutableListOf() }
                    .addAll(matchedKeywords)
            }
        }
        
        // Calculate confidence scores
        val results = predictions.map { (category, keywords) ->
            val matchCount = keywords.size
            val uniqueMatches = keywords.toSet().size
            
            // Confidence based on number of unique keyword matches
            val baseConfidence = (uniqueMatches * 0.3f).coerceAtMost(1.0f)
            
            // Bonus for multiple matches
            val matchBonus = if (matchCount > 1) 0.2f else 0.0f
            
            // Bonus for exact match
            val exactMatchBonus = if (keywords.any { 
                normalizedDescription == it.lowercase() 
            }) 0.3f else 0.0f
            
            val confidence = (baseConfidence + matchBonus + exactMatchBonus).coerceIn(0f, 1f)
            
            CategoryPrediction(
                category = category,
                confidence = confidence,
                matchedKeywords = keywords.distinct()
            )
        }
        
        return results.sortedByDescending { it.confidence }
    }
    
    /**
     * Get the best category prediction
     */
    fun getBestPrediction(description: String, amount: Double? = null): CategoryPrediction? {
        return invoke(description, amount).firstOrNull()
    }
    
    /**
     * Check if prediction confidence is high enough to auto-assign
     */
    fun isConfidentPrediction(prediction: CategoryPrediction, threshold: Float = 0.7f): Boolean {
        return prediction.confidence >= threshold
    }
}

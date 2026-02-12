package com.financemanager.app.domain.model

/**
 * Data class representing transaction filter criteria
 */
data class TransactionFilter(
    val searchQuery: String? = null,
    val categories: List<TransactionCategory>? = null,
    val transactionTypes: List<TransactionType>? = null,
    val accountIds: List<Long>? = null,
    val minAmount: Double? = null,
    val maxAmount: Double? = null,
    val startDate: Long? = null,
    val endDate: Long? = null,
    val sortBy: SortBy = SortBy.DATE_DESC
) {
    enum class SortBy {
        DATE_ASC,
        DATE_DESC,
        AMOUNT_ASC,
        AMOUNT_DESC,
        CATEGORY
    }
    
    /**
     * Check if any filters are applied
     */
    fun hasFilters(): Boolean {
        return searchQuery?.isNotBlank() == true ||
                !categories.isNullOrEmpty() ||
                !transactionTypes.isNullOrEmpty() ||
                !accountIds.isNullOrEmpty() ||
                minAmount != null ||
                maxAmount != null ||
                startDate != null ||
                endDate != null
    }
    
    /**
     * Get count of active filters
     */
    fun getActiveFilterCount(): Int {
        var count = 0
        if (searchQuery?.isNotBlank() == true) count++
        if (!categories.isNullOrEmpty()) count++
        if (!transactionTypes.isNullOrEmpty()) count++
        if (!accountIds.isNullOrEmpty()) count++
        if (minAmount != null || maxAmount != null) count++
        if (startDate != null || endDate != null) count++
        return count
    }
    
    companion object {
        val EMPTY = TransactionFilter()
    }
}

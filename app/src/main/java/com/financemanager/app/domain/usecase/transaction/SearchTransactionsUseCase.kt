package com.financemanager.app.domain.usecase.transaction

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionFilter
import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for searching and filtering transactions
 * Supports multiple filter criteria and sorting options
 */
class SearchTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(userId: Long, filter: TransactionFilter): Flow<List<Transaction>> {
        return transactionRepository.getTransactions(userId)
            .map { transactions ->
                var filtered = transactions
                
                // Apply search query
                filter.searchQuery?.let { query ->
                    if (query.isNotBlank()) {
                        filtered = filtered.filter { transaction ->
                            transaction.description.contains(query, ignoreCase = true) ||
                                    transaction.category.name.contains(query, ignoreCase = true)
                        }
                    }
                }
                
                // Apply category filter
                filter.categories?.let { categories ->
                    if (categories.isNotEmpty()) {
                        filtered = filtered.filter { it.category in categories }
                    }
                }
                
                // Apply transaction type filter
                filter.transactionTypes?.let { types ->
                    if (types.isNotEmpty()) {
                        filtered = filtered.filter { it.transactionType in types }
                    }
                }
                
                // Apply account filter
                filter.accountIds?.let { accountIds ->
                    if (accountIds.isNotEmpty()) {
                        filtered = filtered.filter { it.accountId in accountIds }
                    }
                }
                
                // Apply amount range filter
                filter.minAmount?.let { min ->
                    filtered = filtered.filter { it.amount >= min }
                }
                filter.maxAmount?.let { max ->
                    filtered = filtered.filter { it.amount <= max }
                }
                
                // Apply date range filter
                filter.startDate?.let { start ->
                    filtered = filtered.filter { it.timestamp >= start }
                }
                filter.endDate?.let { end ->
                    filtered = filtered.filter { it.timestamp <= end }
                }
                
                // Apply sorting
                when (filter.sortBy) {
                    TransactionFilter.SortBy.DATE_ASC -> {
                        filtered = filtered.sortedBy { it.timestamp }
                    }
                    TransactionFilter.SortBy.DATE_DESC -> {
                        filtered = filtered.sortedByDescending { it.timestamp }
                    }
                    TransactionFilter.SortBy.AMOUNT_ASC -> {
                        filtered = filtered.sortedBy { it.amount }
                    }
                    TransactionFilter.SortBy.AMOUNT_DESC -> {
                        filtered = filtered.sortedByDescending { it.amount }
                    }
                    TransactionFilter.SortBy.CATEGORY -> {
                        filtered = filtered.sortedBy { it.category.name }
                    }
                }
                
                filtered
            }
    }
}

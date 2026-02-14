package com.financemanager.app.domain.usecase.recurring

import com.financemanager.app.domain.repository.RecurringTransactionRepository
import javax.inject.Inject

/**
 * Use case to toggle recurring transaction active status
 */
class ToggleRecurringActiveUseCase @Inject constructor(
    private val repository: RecurringTransactionRepository
) {
    suspend operator fun invoke(recurringId: Long, isActive: Boolean) {
        repository.toggleActiveStatus(recurringId, isActive)
    }
}

package com.financemanager.app.domain.usecase.account

import com.financemanager.app.domain.model.BankAccount
import com.financemanager.app.domain.repository.BankAccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for getting all bank accounts for a user
 */
class GetAccountsUseCase @Inject constructor(
    private val repository: BankAccountRepository
) {
    operator fun invoke(userId: Long): Flow<List<BankAccount>> {
        return repository.getAccounts(userId)
    }
}

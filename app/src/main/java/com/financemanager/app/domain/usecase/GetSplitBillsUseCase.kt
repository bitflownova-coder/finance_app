package com.financemanager.app.domain.usecase

import com.financemanager.app.domain.model.SplitBill
import com.financemanager.app.domain.repository.SplitBillRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSplitBillsUseCase @Inject constructor(
    private val repository: SplitBillRepository
) {
    operator fun invoke(userId: Long): Flow<List<SplitBill>> {
        return repository.getSplitBills(userId)
    }
    
    fun getUnSettled(userId: Long): Flow<List<SplitBill>> {
        return repository.getUnSettledBills(userId)
    }
}

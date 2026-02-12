package com.financemanager.app.domain.usecase

import com.financemanager.app.domain.model.SplitBill
import com.financemanager.app.domain.repository.SplitBillRepository
import javax.inject.Inject

class AddSplitBillUseCase @Inject constructor(
    private val repository: SplitBillRepository
) {
    suspend operator fun invoke(splitBill: SplitBill): Long {
        // Validate total amount matches sum of participant shares
        val totalShares = splitBill.participants.sumOf { it.shareAmount }
        require(totalShares == splitBill.totalAmount) {
            "Total shares ($totalShares) must equal total amount (${splitBill.totalAmount})"
        }
        
        return repository.addSplitBill(splitBill)
    }
}

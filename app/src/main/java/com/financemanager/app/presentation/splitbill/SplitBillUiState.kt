package com.financemanager.app.presentation.splitbill

import com.financemanager.app.domain.model.Participant
import com.financemanager.app.domain.model.SplitBill
import com.financemanager.app.domain.model.SplitType

enum class BillFilter {
    ALL,
    PENDING,
    SETTLED
}

data class SplitBillUiState(
    val isLoading: Boolean = false,
    val splitBills: List<SplitBill> = emptyList(),
    val unSettledBills: List<SplitBill> = emptyList(),
    val unSettledCount: Int = 0,
    val totalUnSettledAmount: Double = 0.0,
    val selectedBill: SplitBill? = null,
    val showAddDialog: Boolean = false,
    val showDetailsDialog: Boolean = false,
    val showSendReminderDialog: Boolean = false,
    val errorMessage: String? = null,
    val currentFilter: BillFilter = BillFilter.ALL
)

sealed class SplitBillEvent {
    object LoadSplitBills : SplitBillEvent()
    data class SelectBill(val bill: SplitBill) : SplitBillEvent()
    data class AddSplitBill(
        val transactionId: Long? = null, // Optional - not all split bills link to transactions
        val userId: Long, // Will be replaced with actual userId in ViewModel
        val totalAmount: Double,
        val description: String,
        val splitType: SplitType,
        val participants: List<Participant>
    ) : SplitBillEvent()
    data class MarkParticipantPaid(val participantId: Long) : SplitBillEvent()
    data class MarkAsSettled(val splitId: Long) : SplitBillEvent()
    data class DeleteBill(val bill: SplitBill) : SplitBillEvent()
    data class SendReminder(val participant: Participant) : SplitBillEvent()
    data class FilterChanged(val filter: BillFilter) : SplitBillEvent()
    object ShowAddDialog : SplitBillEvent()
    object DismissDialog : SplitBillEvent()
    object DismissError : SplitBillEvent()
}

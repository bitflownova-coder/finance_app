package com.financemanager.app.domain.model

/**
 * Split bill with multiple participants
 */
data class SplitBill(
    val splitId: Long = 0,
    val transactionId: Long,
    val userId: Long,
    val totalAmount: Double,
    val description: String,
    val splitType: SplitType,
    val participants: List<Participant> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val isSettled: Boolean = false
)

enum class SplitType {
    EQUAL,
    CUSTOM,
    PERCENTAGE
}

data class Participant(
    val participantId: Long = 0,
    val splitId: Long,
    val name: String,
    val phoneNumber: String? = null,
    val shareAmount: Double,
    val isPaid: Boolean = false,
    val paidAt: Long? = null
)

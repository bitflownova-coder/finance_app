package com.financemanager.app.domain.model

/**
 * Domain model for transaction receipt
 */
data class Receipt(
    val receiptId: Long = 0,
    val transactionId: Long,
    val imageUri: String,
    val thumbnailUri: String? = null,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Receipt with OCR extracted data (future enhancement)
 */
data class ReceiptOcrData(
    val receiptId: Long,
    val merchantName: String? = null,
    val totalAmount: Double? = null,
    val date: String? = null,
    val items: List<String> = emptyList(),
    val confidence: Float = 0f
)

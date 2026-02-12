package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {
    suspend fun addReceipt(receipt: Receipt): Long
    suspend fun updateReceipt(receipt: Receipt)
    suspend fun deleteReceipt(receipt: Receipt)
    suspend fun getReceiptById(receiptId: Long): Receipt?
    fun getReceiptsByTransactionId(transactionId: Long): Flow<List<Receipt>>
    suspend fun getFirstReceiptByTransactionId(transactionId: Long): Receipt?
    suspend fun deleteReceiptFile(imageUri: String)
}

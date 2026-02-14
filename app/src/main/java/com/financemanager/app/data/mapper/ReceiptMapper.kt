package com.financemanager.app.data.mapper

import com.financemanager.app.data.local.entities.ReceiptEntity
import com.financemanager.app.domain.model.Receipt

object ReceiptMapper {
    
    fun toDomain(entity: ReceiptEntity): Receipt {
        return Receipt(
            receiptId = entity.receiptId,
            transactionId = entity.transactionId,
            imageUri = entity.imageUri,
            thumbnailUri = entity.thumbnailUri,
            note = entity.note,
            createdAt = entity.createdAt
        )
    }
    
    fun toEntity(domain: Receipt): ReceiptEntity {
        return ReceiptEntity(
            receiptId = domain.receiptId,
            transactionId = domain.transactionId,
            imageUri = domain.imageUri,
            thumbnailUri = domain.thumbnailUri,
            note = domain.note,
            createdAt = domain.createdAt
        )
    }
}

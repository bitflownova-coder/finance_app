package com.financemanager.app.data.mapper

import com.financemanager.app.data.local.entities.TransactionEntity
import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionCategory
import com.financemanager.app.domain.model.TransactionType

/**
 * Mapper to convert between TransactionEntity (data layer) and Transaction (domain layer)
 */
object TransactionMapper {
    
    fun toDomain(entity: TransactionEntity): Transaction {
        return Transaction(
            transactionId = entity.transactionId,
            userId = entity.userId,
            accountId = entity.accountId,
            amount = entity.amount,
            transactionType = TransactionType.fromString(entity.transactionType),
            category = TransactionCategory.fromString(entity.category),
            description = entity.description,
            timestamp = entity.timestamp,
            receiptPath = entity.receiptPath,
            notes = entity.notes,
            isRecurring = entity.isRecurring,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(transaction: Transaction): TransactionEntity {
        return TransactionEntity(
            transactionId = transaction.transactionId,
            userId = transaction.userId,
            accountId = transaction.accountId,
            amount = transaction.amount,
            transactionType = transaction.transactionType.name,
            category = transaction.category.name,
            description = transaction.description,
            timestamp = transaction.timestamp,
            receiptPath = transaction.receiptPath,
            notes = transaction.notes,
            isRecurring = transaction.isRecurring,
            createdAt = transaction.createdAt,
            updatedAt = transaction.updatedAt
        )
    }
}

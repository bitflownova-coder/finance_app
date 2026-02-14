package com.financemanager.app.data.mapper

import com.financemanager.app.data.local.entities.RecurringTransactionEntity
import com.financemanager.app.domain.model.RecurringFrequency
import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.model.TransactionCategory
import com.financemanager.app.domain.model.TransactionType

/**
 * Mapper between RecurringTransactionEntity and RecurringTransaction domain model
 */
object RecurringTransactionMapper {
    
    fun toDomain(entity: RecurringTransactionEntity): RecurringTransaction {
        return RecurringTransaction(
            recurringId = entity.recurringId,
            userId = entity.userId,
            accountId = entity.accountId,
            amount = entity.amount,
            type = TransactionType.valueOf(entity.type),
            category = TransactionCategory.fromString(entity.category),
            description = entity.description,
            frequency = RecurringFrequency.fromString(entity.frequency),
            startDate = entity.startDate,
            endDate = entity.endDate,
            nextOccurrence = entity.nextOccurrence,
            isActive = entity.isActive,
            lastGenerated = entity.lastGenerated,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(recurringTransaction: RecurringTransaction): RecurringTransactionEntity {
        return RecurringTransactionEntity(
            recurringId = recurringTransaction.recurringId,
            userId = recurringTransaction.userId,
            accountId = recurringTransaction.accountId,
            amount = recurringTransaction.amount,
            type = recurringTransaction.type.name,
            category = recurringTransaction.category.name,
            description = recurringTransaction.description,
            frequency = recurringTransaction.frequency.name,
            startDate = recurringTransaction.startDate,
            endDate = recurringTransaction.endDate,
            nextOccurrence = recurringTransaction.nextOccurrence,
            isActive = recurringTransaction.isActive,
            lastGenerated = recurringTransaction.lastGenerated,
            createdAt = recurringTransaction.createdAt,
            updatedAt = recurringTransaction.updatedAt
        )
    }
}

package com.financemanager.app.data.mapper

import com.financemanager.app.data.local.entities.BankAccountEntity
import com.financemanager.app.domain.model.AccountType
import com.financemanager.app.domain.model.BankAccount

/**
 * Mapper to convert between BankAccountEntity (data layer) and BankAccount (domain layer)
 */
object BankAccountMapper {
    
    fun toDomain(entity: BankAccountEntity): BankAccount {
        return BankAccount(
            accountId = entity.accountId,
            userId = entity.userId,
            accountName = entity.accountName,
            accountNumber = entity.accountNumber,
            bankName = entity.bankName,
            ifscCode = entity.ifscCode,
            accountType = AccountType.fromString(entity.accountType),
            balance = entity.balance,
            currency = entity.currency,
            isPrimary = entity.isPrimary,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }
    
    fun toEntity(account: BankAccount): BankAccountEntity {
        return BankAccountEntity(
            accountId = account.accountId,
            userId = account.userId,
            accountName = account.accountName,
            accountNumber = account.accountNumber,
            bankName = account.bankName,
            ifscCode = account.ifscCode,
            accountType = account.accountType.name,
            balance = account.balance,
            currency = account.currency,
            isPrimary = account.isPrimary,
            createdAt = account.createdAt,
            updatedAt = account.updatedAt
        )
    }
}

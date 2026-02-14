package com.financemanager.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model for Bank Account
 */
@Parcelize
data class BankAccount(
    val accountId: Long = 0,
    val userId: Long,
    val accountName: String,
    val accountNumber: String,
    val bankName: String,
    val ifscCode: String? = null,
    val accountType: AccountType,
    val balance: Double = 0.0,
    val currency: String = "INR",
    val isPrimary: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class AccountType {
    SAVINGS,
    CURRENT,
    WALLET,
    CASH;
    
    companion object {
        fun fromString(value: String): AccountType {
            return values().find { it.name == value.uppercase() } ?: SAVINGS
        }
    }
}

package com.financemanager.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model for Transaction
 */
@Parcelize
data class Transaction(
    val transactionId: Long = 0,
    val userId: Long,
    val accountId: Long,
    val amount: Double,
    val transactionType: TransactionType,
    val category: TransactionCategory,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val receiptPath: String? = null,
    val notes: String? = null,
    val isRecurring: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class TransactionType {
    DEBIT,
    CREDIT;
    
    companion object {
        fun fromString(value: String): TransactionType {
            return values().find { it.name == value.uppercase() } ?: DEBIT
        }
    }
}

enum class TransactionCategory(val displayName: String, val icon: String) {
    FOOD("Food & Dining", "🍔"),
    TRANSPORT("Transport", "🚗"),
    SHOPPING("Shopping", "🛍️"),
    ENTERTAINMENT("Entertainment", "🎬"),
    BILLS("Bills & Utilities", "💡"),
    HEALTHCARE("Healthcare", "🏥"),
    EDUCATION("Education", "📚"),
    SALARY("Salary", "💰"),
    BUSINESS("Business", "💼"),
    INVESTMENT("Investment", "📈"),
    GIFT("Gift", "🎁"),
    OTHER("Other", "📦");
    
    companion object {
        fun fromString(value: String): TransactionCategory {
            return values().find { it.name == value.uppercase() } ?: OTHER
        }
        
        fun getExpenseCategories(): List<TransactionCategory> {
            return listOf(FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, BILLS, HEALTHCARE, EDUCATION, OTHER)
        }
        
        fun getIncomeCategories(): List<TransactionCategory> {
            return listOf(SALARY, BUSINESS, INVESTMENT, GIFT, OTHER)
        }
    }
}

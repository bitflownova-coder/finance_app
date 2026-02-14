package com.financemanager.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model for Recurring Transaction
 */
@Parcelize
data class RecurringTransaction(
    val recurringId: Long = 0,
    val userId: Long,
    val accountId: Long,
    val amount: Double,
    val type: TransactionType,
    val category: TransactionCategory,
    val description: String,
    val frequency: RecurringFrequency,
    val startDate: Long,
    val endDate: Long? = null, // null means no end date
    val nextOccurrence: Long,
    val isActive: Boolean = true,
    val lastGenerated: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable

enum class RecurringFrequency {
    DAILY,
    WEEKLY,
    BIWEEKLY,
    MONTHLY,
    QUARTERLY,
    YEARLY;
    
    val displayName: String
        get() = when (this) {
            DAILY -> "Daily"
            WEEKLY -> "Weekly"
            BIWEEKLY -> "Bi-weekly"
            MONTHLY -> "Monthly"
            QUARTERLY -> "Quarterly"
            YEARLY -> "Yearly"
        }
    
    companion object {
        fun fromString(value: String): RecurringFrequency {
            return values().find { it.name == value.uppercase() } ?: MONTHLY
        }
    }
}

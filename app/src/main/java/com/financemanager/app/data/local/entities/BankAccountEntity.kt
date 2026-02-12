package com.financemanager.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Bank account entity for Room database
 * Stores user bank account information and balance
 */
@Entity(
    tableName = "bank_accounts",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["account_number"], unique = true)
    ]
)
data class BankAccountEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    val accountId: Long = 0,
    
    @ColumnInfo(name = "user_id")
    val userId: Long,
    
    @ColumnInfo(name = "account_name")
    val accountName: String,
    
    @ColumnInfo(name = "account_number")
    val accountNumber: String,
    
    @ColumnInfo(name = "bank_name")
    val bankName: String,
    
    @ColumnInfo(name = "ifsc_code")
    val ifscCode: String? = null,
    
    @ColumnInfo(name = "account_type")
    val accountType: String, // SAVINGS, CURRENT, WALLET, CASH
    
    @ColumnInfo(name = "balance")
    val balance: Double = 0.0,
    
    @ColumnInfo(name = "currency")
    val currency: String = "INR",
    
    @ColumnInfo(name = "is_primary")
    val isPrimary: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

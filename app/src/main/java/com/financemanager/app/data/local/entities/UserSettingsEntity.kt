package com.financemanager.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity for user settings
 */
@Entity(
    tableName = "user_settings",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_id")]
)
data class UserSettingsEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Long,
    
    @ColumnInfo(name = "theme")
    val theme: String = "SYSTEM",
    
    @ColumnInfo(name = "currency")
    val currency: String = "INR",
    
    @ColumnInfo(name = "currency_symbol")
    val currencySymbol: String = "₹",
    
    @ColumnInfo(name = "notifications_enabled")
    val notificationsEnabled: Boolean = true,
    
    @ColumnInfo(name = "budget_alerts_enabled")
    val budgetAlertsEnabled: Boolean = true,
    
    @ColumnInfo(name = "transaction_reminders_enabled")
    val transactionRemindersEnabled: Boolean = true,
    
    @ColumnInfo(name = "biometric_enabled")
    val biometricEnabled: Boolean = false,
    
    @ColumnInfo(name = "auto_backup_enabled")
    val autoBackupEnabled: Boolean = false,
    
    @ColumnInfo(name = "last_backup_time")
    val lastBackupTime: Long? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

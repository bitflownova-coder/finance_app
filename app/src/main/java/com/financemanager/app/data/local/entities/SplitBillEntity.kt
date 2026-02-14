package com.financemanager.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "split_bills",
    indices = [
        Index("transaction_id"),
        Index("user_id"),
        Index("is_settled")
    ],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SplitBillEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "split_id")
    val splitId: Long = 0,
    
    @ColumnInfo(name = "transaction_id")
    val transactionId: Long? = null, // Optional - not all split bills link to transactions
    
    @ColumnInfo(name = "user_id")
    val userId: Long,
    
    @ColumnInfo(name = "total_amount")
    val totalAmount: Double,
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "split_type")
    val splitType: String,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    
    @ColumnInfo(name = "is_settled")
    val isSettled: Boolean = false
)

@Entity(
    tableName = "split_participants",
    indices = [
        Index("split_id"),
        Index("is_paid")
    ],
    foreignKeys = [
        ForeignKey(
            entity = SplitBillEntity::class,
            parentColumns = ["split_id"],
            childColumns = ["split_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ParticipantEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "participant_id")
    val participantId: Long = 0,
    
    @ColumnInfo(name = "split_id")
    val splitId: Long,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,
    
    @ColumnInfo(name = "share_amount")
    val shareAmount: Double,
    
    @ColumnInfo(name = "is_paid")
    val isPaid: Boolean = false,
    
    @ColumnInfo(name = "paid_at")
    val paidAt: Long? = null
)

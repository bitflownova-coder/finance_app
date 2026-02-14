package com.financemanager.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "receipts",
    indices = [
        Index("transaction_id"),
        Index("created_at")
    ],
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["transaction_id"],
            childColumns = ["transaction_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReceiptEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "receipt_id")
    val receiptId: Long = 0,
    
    @ColumnInfo(name = "transaction_id")
    val transactionId: Long,
    
    @ColumnInfo(name = "image_uri")
    val imageUri: String,
    
    @ColumnInfo(name = "thumbnail_uri")
    val thumbnailUri: String? = null,
    
    @ColumnInfo(name = "note")
    val note: String = "",
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

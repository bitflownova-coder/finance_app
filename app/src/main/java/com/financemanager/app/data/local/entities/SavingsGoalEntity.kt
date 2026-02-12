package com.financemanager.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "savings_goals",
    indices = [
        Index("user_id"),
        Index("target_date"),
        Index("status"),
        Index("is_archived")
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
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    val goalId: Long = 0,
    
    @ColumnInfo(name = "user_id")
    val userId: Long,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "target_amount")
    val targetAmount: Double,
    
    @ColumnInfo(name = "current_amount")
    val currentAmount: Double = 0.0,
    
    @ColumnInfo(name = "target_date")
    val targetDate: Long, // LocalDate as epoch day
    
    @ColumnInfo(name = "category")
    val category: String,
    
    @ColumnInfo(name = "priority")
    val priority: String,
    
    @ColumnInfo(name = "status")
    val status: String,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "is_archived")
    val isArchived: Boolean = false
)

@Entity(
    tableName = "goal_contributions",
    indices = [
        Index("goal_id"),
        Index("timestamp"),
        Index("transaction_id")
    ],
    foreignKeys = [
        ForeignKey(
            entity = SavingsGoalEntity::class,
            parentColumns = ["goal_id"],
            childColumns = ["goal_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["transaction_id"],
            childColumns = ["transaction_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class GoalContributionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contribution_id")
    val contributionId: Long = 0,
    
    @ColumnInfo(name = "goal_id")
    val goalId: Long,
    
    @ColumnInfo(name = "amount")
    val amount: Double,
    
    @ColumnInfo(name = "note")
    val note: String = "",
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "transaction_id")
    val transactionId: Long? = null
)

package com.financemanager.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Budget entity for Room database
 * Stores user budget information and spending limits
 */
@Entity(
    tableName = "budgets",
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
        Index(value = ["period_start", "period_end"])
    ]
)
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "budget_id")
    val budgetId: Long = 0,
    
    @ColumnInfo(name = "user_id")
    val userId: Long,
    
    @ColumnInfo(name = "budget_name")
    val budgetName: String,
    
    @ColumnInfo(name = "total_budget")
    val totalBudget: Double,
    
    @ColumnInfo(name = "spent_amount")
    val spentAmount: Double = 0.0,
    
    @ColumnInfo(name = "period_type")
    val periodType: String, // MONTHLY, YEARLY
    
    @ColumnInfo(name = "period_start")
    val periodStart: Long,
    
    @ColumnInfo(name = "period_end")
    val periodEnd: Long,
    
    @ColumnInfo(name = "category")
    val category: String? = null, // Optional: category-specific budget
    
    @ColumnInfo(name = "alert_threshold")
    val alertThreshold: Double = 0.8, // Alert at 80%
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

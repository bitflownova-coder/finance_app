package com.financemanager.app.domain.model

/**
 * Domain model for User
 */
data class User(
    val userId: Long = 0,
    val fullName: String,
    val phone: String,
    val upiId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

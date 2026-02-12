package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.User
import com.financemanager.app.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for User operations
 */
interface UserRepository {
    
    suspend fun createUser(fullName: String, phone: String): Result<Long>
    
    suspend fun setupPin(userId: Long, pin: String): Result<Unit>
    
    suspend fun verifyPin(userId: Long, pin: String): Result<User>
    
    suspend fun getUserById(userId: Long): Result<User>
    
    suspend fun getFirstActiveUser(): Result<User>
    
    suspend fun hasAnyUser(): Boolean
    
    suspend fun updateUser(user: User): Result<Unit>
    
    fun observeUser(userId: Long): Flow<User?>
}

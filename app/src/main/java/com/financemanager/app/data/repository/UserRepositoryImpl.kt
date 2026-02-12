package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.UserDao
import com.financemanager.app.data.local.entities.UserEntity
import com.financemanager.app.data.mapper.UserMapper
import com.financemanager.app.di.IoDispatcher
import com.financemanager.app.domain.model.User
import com.financemanager.app.domain.repository.UserRepository
import com.financemanager.app.util.Result
import com.financemanager.app.util.SecurityUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    
    override suspend fun createUser(fullName: String, phone: String): Result<Long> =
        withContext(ioDispatcher) {
            try {
                val entity = UserEntity(
                    fullName = fullName,
                    phone = phone
                )
                val userId = userDao.insert(entity)
                Result.Success(userId)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    
    override suspend fun setupPin(userId: Long, pin: String): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                val pinHash = SecurityUtils.hashPassword(pin)
                userDao.updatePin(userId, pinHash)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    
    override suspend fun verifyPin(userId: Long, pin: String): Result<User> =
        withContext(ioDispatcher) {
            try {
                val userEntity = userDao.getUserById(userId)
                    ?: return@withContext Result.Error(Exception("User not found"))
                
                if (userEntity.pinHash.isBlank()) {
                    return@withContext Result.Error(Exception("PIN not set"))
                }
                
                if (!SecurityUtils.verifyPassword(pin, userEntity.pinHash)) {
                    return@withContext Result.Error(Exception("Invalid PIN"))
                }
                
                if (!userEntity.isActive) {
                    return@withContext Result.Error(Exception("Account is inactive"))
                }
                
                Result.Success(UserMapper.toDomain(userEntity))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    
    override suspend fun getUserById(userId: Long): Result<User> =
        withContext(ioDispatcher) {
            try {
                val userEntity = userDao.getUserById(userId)
                    ?: return@withContext Result.Error(Exception("User not found"))
                Result.Success(UserMapper.toDomain(userEntity))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    
    override suspend fun getFirstActiveUser(): Result<User> =
        withContext(ioDispatcher) {
            try {
                val userEntity = userDao.getFirstActiveUser()
                    ?: return@withContext Result.Error(Exception("No user found"))
                Result.Success(UserMapper.toDomain(userEntity))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    
    override suspend fun hasAnyUser(): Boolean =
        withContext(ioDispatcher) {
            userDao.hasAnyUser() > 0
        }
    
    override suspend fun updateUser(user: User): Result<Unit> =
        withContext(ioDispatcher) {
            try {
                val existingEntity = userDao.getUserById(user.userId)
                    ?: return@withContext Result.Error(Exception("User not found"))
                val updatedEntity = UserMapper.toEntity(
                    user.copy(updatedAt = System.currentTimeMillis()),
                    existingEntity.pinHash
                )
                userDao.update(updatedEntity)
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    
    override fun observeUser(userId: Long): Flow<User?> {
        return userDao.observeUser(userId).map { entity ->
            entity?.let { UserMapper.toDomain(it) }
        }
    }
}

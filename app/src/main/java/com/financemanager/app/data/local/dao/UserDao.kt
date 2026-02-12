package com.financemanager.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financemanager.app.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for User operations
 */
@Dao
interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long
    
    @Update
    suspend fun update(user: UserEntity)
    
    @Delete
    suspend fun delete(user: UserEntity)
    
    @Query("SELECT * FROM users WHERE user_id = :userId")
    suspend fun getUserById(userId: Long): UserEntity?
    
    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): UserEntity?
    
    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun observeUser(userId: Long): Flow<UserEntity?>
    
    @Query("SELECT * FROM users WHERE is_active = 1")
    fun getAllActiveUsers(): Flow<List<UserEntity>>
    
    @Query("SELECT COUNT(*) FROM users WHERE phone = :phone")
    suspend fun phoneExists(phone: String): Int
    
    @Query("SELECT COUNT(*) FROM users LIMIT 1")
    suspend fun hasAnyUser(): Int
    
    @Query("SELECT * FROM users WHERE is_active = 1 LIMIT 1")
    suspend fun getFirstActiveUser(): UserEntity?
    
    @Query("UPDATE users SET pin_hash = :pinHash, updated_at = :timestamp WHERE user_id = :userId")
    suspend fun updatePin(userId: Long, pinHash: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE users SET updated_at = :timestamp WHERE user_id = :userId")
    suspend fun updateTimestamp(userId: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM users")
    suspend fun deleteAll()
}

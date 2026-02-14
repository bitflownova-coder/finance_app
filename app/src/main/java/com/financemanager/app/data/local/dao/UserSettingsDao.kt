package com.financemanager.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financemanager.app.data.local.entities.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for User Settings operations
 */
@Dao
interface UserSettingsDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: UserSettingsEntity)
    
    @Update
    suspend fun update(settings: UserSettingsEntity)
    
    @Query("SELECT * FROM user_settings WHERE user_id = :userId")
    fun getSettings(userId: Long): Flow<UserSettingsEntity?>
    
    @Query("SELECT * FROM user_settings WHERE user_id = :userId")
    suspend fun getSettingsOnce(userId: Long): UserSettingsEntity?
    
    @Query("UPDATE user_settings SET theme = :theme, updated_at = :timestamp WHERE user_id = :userId")
    suspend fun updateTheme(userId: Long, theme: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE user_settings SET currency = :currency, currency_symbol = :symbol, updated_at = :timestamp WHERE user_id = :userId")
    suspend fun updateCurrency(userId: Long, currency: String, symbol: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE user_settings SET notifications_enabled = :enabled, updated_at = :timestamp WHERE user_id = :userId")
    suspend fun updateNotifications(userId: Long, enabled: Boolean, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE user_settings SET budget_alerts_enabled = :enabled, updated_at = :timestamp WHERE user_id = :userId")
    suspend fun updateBudgetAlerts(userId: Long, enabled: Boolean, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE user_settings SET biometric_enabled = :enabled, updated_at = :timestamp WHERE user_id = :userId")
    suspend fun updateBiometric(userId: Long, enabled: Boolean, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE user_settings SET auto_backup_enabled = :enabled, last_backup_time = :backupTime, updated_at = :timestamp WHERE user_id = :userId")
    suspend fun updateBackupSettings(userId: Long, enabled: Boolean, backupTime: Long?, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM user_settings WHERE user_id = :userId")
    suspend fun delete(userId: Long)
}

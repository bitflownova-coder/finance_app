package com.financemanager.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financemanager.app.data.local.entities.BankAccountEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Bank Account operations
 */
@Dao
interface BankAccountDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: BankAccountEntity): Long
    
    @Update
    suspend fun update(account: BankAccountEntity)
    
    @Delete
    suspend fun delete(account: BankAccountEntity)
    
    @Query("SELECT * FROM bank_accounts WHERE account_id = :accountId")
    suspend fun getAccountById(accountId: Long): BankAccountEntity?
    
    @Query("SELECT * FROM bank_accounts WHERE user_id = :userId ORDER BY is_primary DESC, created_at DESC")
    fun getAccountsByUserId(userId: Long): Flow<List<BankAccountEntity>>
    
    @Query("SELECT * FROM bank_accounts WHERE user_id = :userId AND is_primary = 1 LIMIT 1")
    suspend fun getPrimaryAccount(userId: Long): BankAccountEntity?
    
    @Query("SELECT SUM(balance) FROM bank_accounts WHERE user_id = :userId")
    suspend fun getTotalBalance(userId: Long): Double?
    
    @Query("SELECT SUM(balance) FROM bank_accounts WHERE user_id = :userId")
    fun getTotalBalanceFlow(userId: Long): Flow<Double?>
    
    @Query("UPDATE bank_accounts SET balance = balance + :amount, updated_at = :timestamp WHERE account_id = :accountId")
    suspend fun increaseBalance(accountId: Long, amount: Double, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE bank_accounts SET balance = balance - :amount, updated_at = :timestamp WHERE account_id = :accountId")
    suspend fun decreaseBalance(accountId: Long, amount: Double, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE bank_accounts SET is_primary = 0 WHERE user_id = :userId")
    suspend fun clearPrimaryAccounts(userId: Long)
    
    @Query("DELETE FROM bank_accounts WHERE user_id = :userId")
    suspend fun deleteAllByUserId(userId: Long)
}

package com.financemanager.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financemanager.app.data.local.entities.ReceiptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(receipt: ReceiptEntity): Long
    
    @Update
    suspend fun update(receipt: ReceiptEntity)
    
    @Delete
    suspend fun delete(receipt: ReceiptEntity)
    
    @Query("SELECT * FROM receipts WHERE receipt_id = :receiptId")
    suspend fun getReceiptById(receiptId: Long): ReceiptEntity?
    
    @Query("SELECT * FROM receipts WHERE transaction_id = :transactionId")
    fun getReceiptsByTransactionId(transactionId: Long): Flow<List<ReceiptEntity>>
    
    @Query("SELECT * FROM receipts WHERE transaction_id = :transactionId LIMIT 1")
    suspend fun getFirstReceiptByTransactionId(transactionId: Long): ReceiptEntity?
    
    @Query("SELECT COUNT(*) FROM receipts WHERE transaction_id = :transactionId")
    suspend fun getReceiptCountForTransaction(transactionId: Long): Int
    
    @Query("DELETE FROM receipts WHERE receipt_id = :receiptId")
    suspend fun deleteById(receiptId: Long)
}

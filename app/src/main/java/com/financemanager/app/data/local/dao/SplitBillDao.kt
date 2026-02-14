package com.financemanager.app.data.local.dao

import androidx.room.*
import com.financemanager.app.data.local.entities.ParticipantEntity
import com.financemanager.app.data.local.entities.SplitBillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SplitBillDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSplitBill(splitBill: SplitBillEntity): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipants(participants: List<ParticipantEntity>)
    
    @Update
    suspend fun updateSplitBill(splitBill: SplitBillEntity)
    
    @Update
    suspend fun updateParticipant(participant: ParticipantEntity)
    
    @Delete
    suspend fun deleteSplitBill(splitBill: SplitBillEntity)
    
    @Query("SELECT * FROM split_bills WHERE user_id = :userId ORDER BY created_at DESC")
    fun getSplitBills(userId: Long): Flow<List<SplitBillEntity>>
    
    @Query("SELECT * FROM split_bills WHERE split_id = :splitId")
    suspend fun getSplitBillById(splitId: Long): SplitBillEntity?
    
    @Query("SELECT * FROM split_participants WHERE split_id = :splitId")
    suspend fun getParticipants(splitId: Long): List<ParticipantEntity>
    
    @Query("SELECT * FROM split_participants WHERE split_id = :splitId")
    fun getParticipantsFlow(splitId: Long): Flow<List<ParticipantEntity>>
    
    @Query("SELECT * FROM split_bills WHERE user_id = :userId AND is_settled = 0")
    fun getUnSettledBills(userId: Long): Flow<List<SplitBillEntity>>
    
    @Query("SELECT COUNT(*) FROM split_bills WHERE user_id = :userId AND is_settled = 0")
    fun getUnSettledCount(userId: Long): Flow<Int>
    
    @Query("SELECT SUM(total_amount) FROM split_bills WHERE user_id = :userId AND is_settled = 0")
    suspend fun getTotalUnSettledAmount(userId: Long): Double?
    
    @Query("UPDATE split_bills SET is_settled = 1 WHERE split_id = :splitId")
    suspend fun markAsSettled(splitId: Long)
    
    @Query("UPDATE split_participants SET is_paid = 1, paid_at = :paidAt WHERE participant_id = :participantId")
    suspend fun markParticipantAsPaid(participantId: Long, paidAt: Long)
    
    @Transaction
    suspend fun insertSplitBillWithParticipants(
        splitBill: SplitBillEntity,
        participants: List<ParticipantEntity>
    ): Long {
        val splitId = insertSplitBill(splitBill)
        val participantsWithSplitId = participants.map { it.copy(splitId = splitId) }
        insertParticipants(participantsWithSplitId)
        return splitId
    }
    
    @Transaction
    suspend fun checkAndUpdateSettledStatus(splitId: Long) {
        val participants = getParticipants(splitId)
        if (participants.all { it.isPaid }) {
            markAsSettled(splitId)
        }
    }
}

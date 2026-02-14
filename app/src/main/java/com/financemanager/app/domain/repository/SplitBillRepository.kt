package com.financemanager.app.domain.repository

import com.financemanager.app.domain.model.SplitBill
import kotlinx.coroutines.flow.Flow

interface SplitBillRepository {
    
    fun getSplitBills(userId: Long): Flow<List<SplitBill>>
    
    fun getUnSettledBills(userId: Long): Flow<List<SplitBill>>
    
    fun getUnSettledCount(userId: Long): Flow<Int>
    
    suspend fun getSplitBillById(splitId: Long): SplitBill?
    
    suspend fun addSplitBill(splitBill: SplitBill): Long
    
    suspend fun updateSplitBill(splitBill: SplitBill)
    
    suspend fun deleteSplitBill(splitBill: SplitBill)
    
    suspend fun getTotalUnSettledAmount(userId: Long): Double
    
    suspend fun markParticipantAsPaid(participantId: Long)
    
    suspend fun markAsSettled(splitId: Long)
}

package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.SplitBillDao
import com.financemanager.app.data.mapper.SplitBillMapper
import com.financemanager.app.di.IoDispatcher
import com.financemanager.app.domain.model.SplitBill
import com.financemanager.app.domain.repository.SplitBillRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplitBillRepositoryImpl @Inject constructor(
    private val splitBillDao: SplitBillDao,
    private val mapper: SplitBillMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SplitBillRepository {
    
    override fun getSplitBills(userId: Long): Flow<List<SplitBill>> {
        return splitBillDao.getSplitBills(userId).map { bills ->
            bills.map { bill ->
                val participants = splitBillDao.getParticipants(bill.splitId)
                mapper.toDomain(bill, participants)
            }
        }
    }
    
    override fun getUnSettledBills(userId: Long): Flow<List<SplitBill>> {
        return splitBillDao.getUnSettledBills(userId).map { bills ->
            bills.map { bill ->
                val participants = splitBillDao.getParticipants(bill.splitId)
                mapper.toDomain(bill, participants)
            }
        }
    }
    
    override fun getUnSettledCount(userId: Long): Flow<Int> {
        return splitBillDao.getUnSettledCount(userId)
    }
    
    override suspend fun getSplitBillById(splitId: Long): SplitBill? = withContext(ioDispatcher) {
        val bill = splitBillDao.getSplitBillById(splitId) ?: return@withContext null
        val participants = splitBillDao.getParticipants(splitId)
        mapper.toDomain(bill, participants)
    }
    
    override suspend fun addSplitBill(splitBill: SplitBill): Long = withContext(ioDispatcher) {
        val entity = mapper.toEntity(splitBill)
        val participantEntities = splitBill.participants.map { mapper.participantToEntity(it) }
        splitBillDao.insertSplitBillWithParticipants(entity, participantEntities)
    }
    
    override suspend fun updateSplitBill(splitBill: SplitBill) = withContext(ioDispatcher) {
        splitBillDao.updateSplitBill(mapper.toEntity(splitBill))
    }
    
    override suspend fun deleteSplitBill(splitBill: SplitBill) = withContext(ioDispatcher) {
        splitBillDao.deleteSplitBill(mapper.toEntity(splitBill))
    }
    
    override suspend fun getTotalUnSettledAmount(userId: Long): Double = withContext(ioDispatcher) {
        splitBillDao.getTotalUnSettledAmount(userId) ?: 0.0
    }
    
    override suspend fun markParticipantAsPaid(participantId: Long) {
        withContext(ioDispatcher) {
            splitBillDao.markParticipantAsPaid(participantId, System.currentTimeMillis())
            // Get the split ID from the participant and check if all are paid
            val participant = splitBillDao.getParticipants(0).find { it.participantId == participantId }
            participant?.let {
                splitBillDao.checkAndUpdateSettledStatus(it.splitId)
            }
        }
    }
    
    override suspend fun markAsSettled(splitId: Long) = withContext(ioDispatcher) {
        splitBillDao.markAsSettled(splitId)
    }
}

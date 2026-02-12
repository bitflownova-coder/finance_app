package com.financemanager.app.data.repository

import android.content.Context
import com.financemanager.app.data.local.dao.ReceiptDao
import com.financemanager.app.data.mapper.ReceiptMapper
import com.financemanager.app.domain.model.Receipt
import com.financemanager.app.domain.repository.ReceiptRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

class ReceiptRepositoryImpl @Inject constructor(
    private val receiptDao: ReceiptDao,
    @ApplicationContext private val context: Context
) : ReceiptRepository {
    
    override suspend fun addReceipt(receipt: Receipt): Long {
        val entity = ReceiptMapper.toEntity(receipt)
        return receiptDao.insert(entity)
    }
    
    override suspend fun updateReceipt(receipt: Receipt) {
        val entity = ReceiptMapper.toEntity(receipt)
        receiptDao.update(entity)
    }
    
    override suspend fun deleteReceipt(receipt: Receipt) {
        val entity = ReceiptMapper.toEntity(receipt)
        receiptDao.delete(entity)
        
        // Delete physical file
        deleteReceiptFile(receipt.imageUri)
        receipt.thumbnailUri?.let { deleteReceiptFile(it) }
    }
    
    override suspend fun getReceiptById(receiptId: Long): Receipt? {
        return receiptDao.getReceiptById(receiptId)?.let { ReceiptMapper.toDomain(it) }
    }
    
    override fun getReceiptsByTransactionId(transactionId: Long): Flow<List<Receipt>> {
        return receiptDao.getReceiptsByTransactionId(transactionId).map { entities ->
            entities.map { ReceiptMapper.toDomain(it) }
        }
    }
    
    override suspend fun getFirstReceiptByTransactionId(transactionId: Long): Receipt? {
        return receiptDao.getFirstReceiptByTransactionId(transactionId)?.let { 
            ReceiptMapper.toDomain(it) 
        }
    }
    
    override suspend fun deleteReceiptFile(imageUri: String) {
        try {
            val file = File(imageUri)
            if (file.exists()) {
                file.delete()
            }
        } catch (e: Exception) {
            // Log error but don't throw - file might already be deleted
        }
    }
}

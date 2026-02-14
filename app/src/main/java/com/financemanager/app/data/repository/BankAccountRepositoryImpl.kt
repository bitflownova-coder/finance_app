package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.BankAccountDao
import com.financemanager.app.data.mapper.BankAccountMapper
import com.financemanager.app.di.IoDispatcher
import com.financemanager.app.domain.model.BankAccount
import com.financemanager.app.domain.repository.BankAccountRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of BankAccountRepository
 */
@Singleton
class BankAccountRepositoryImpl @Inject constructor(
    private val bankAccountDao: BankAccountDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : BankAccountRepository {
    
    override fun getAccounts(userId: Long): Flow<List<BankAccount>> {
        return bankAccountDao.getAccountsByUserId(userId)
            .map { entities ->
                entities.map { BankAccountMapper.toDomain(it) }
            }
    }
    
    override suspend fun getAccountById(accountId: Long): BankAccount? = withContext(ioDispatcher) {
        bankAccountDao.getAccountById(accountId)?.let {
            BankAccountMapper.toDomain(it)
        }
    }
    
    override suspend fun addAccount(account: BankAccount): Long = withContext(ioDispatcher) {
        val entity = BankAccountMapper.toEntity(account)
        bankAccountDao.insert(entity)
    }
    
    override suspend fun updateAccount(account: BankAccount) = withContext(ioDispatcher) {
        val entity = BankAccountMapper.toEntity(account)
        bankAccountDao.update(entity)
    }
    
    override suspend fun deleteAccount(accountId: Long): Unit = withContext(ioDispatcher) {
        bankAccountDao.getAccountById(accountId)?.let { entity ->
            bankAccountDao.delete(entity)
        }
    }
    
    override suspend fun getTotalBalance(userId: Long): Double = withContext(ioDispatcher) {
        bankAccountDao.getTotalBalance(userId) ?: 0.0
    }
    
    override suspend fun setPrimaryAccount(accountId: Long, userId: Long): Unit = withContext(ioDispatcher) {
        // Clear all primary flags for this user
        bankAccountDao.clearPrimaryAccounts(userId)
        
        // Set the new primary account
        bankAccountDao.getAccountById(accountId)?.let { account ->
            bankAccountDao.update(account.copy(isPrimary = true, updatedAt = System.currentTimeMillis()))
        }
    }
}

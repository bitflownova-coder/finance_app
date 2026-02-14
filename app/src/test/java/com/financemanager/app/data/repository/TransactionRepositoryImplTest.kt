package com.financemanager.app.data.repository

import com.financemanager.app.data.local.dao.TransactionDao
import com.financemanager.app.data.mapper.TransactionMapper
import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionType
import com.financemanager.app.data.local.entities.TransactionEntity
import com.financemanager.app.util.DispatcherProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionRepositoryImplTest {
    
    private lateinit var repository: TransactionRepositoryImpl
    private lateinit var transactionDao: TransactionDao
    private lateinit var mapper: TransactionMapper
    private lateinit var dispatchers: DispatcherProvider
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        transactionDao = mockk()
        mapper = TransactionMapper()
        dispatchers = mockk {
            coEvery { io } returns testDispatcher
        }
        
        repository = TransactionRepositoryImpl(transactionDao, mapper, dispatchers)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `getTransactions returns mapped transactions`() = runTest {
        // Given
        val userId = 1L
        val entities = listOf(
            TransactionEntity(
                transactionId = 1,
                userId = userId,
                accountId = 1,
                amount = 500.0,
                transactionType = "DEBIT",
                category = "Food",
                description = "Lunch",
                timestamp = System.currentTimeMillis()
            )
        )
        
        coEvery { transactionDao.getTransactionsByUserId(userId) } returns flowOf(entities)
        
        // When
        val result = repository.getTransactions(userId).first()
        
        // Then
        assertEquals(1, result.size)
        assertEquals(500.0, result.first().amount, 0.01)
        assertEquals("Food", result.first().category)
    }
    
    @Test
    fun `addTransaction inserts entity and returns id`() = runTest {
        // Given
        val transaction = Transaction(
            userId = 1,
            accountId = 1,
            amount = 500.0,
            type = TransactionType.DEBIT,
            category = "Food",
            description = "Lunch",
            timestamp = System.currentTimeMillis()
        )
        
        coEvery { transactionDao.insert(any()) } returns 1L
        
        // When
        val result = repository.addTransaction(transaction)
        
        // Then
        assertEquals(1L, result)
        coVerify { transactionDao.insert(any()) }
    }
}

package com.financemanager.app.domain.usecase.transaction

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionCategory
import com.financemanager.app.domain.model.TransactionType
import com.financemanager.app.domain.repository.TransactionRepository
import com.financemanager.app.util.Result
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for AddTransactionUseCase
 */
class AddTransactionUseCaseTest {
    
    private lateinit var repository: TransactionRepository
    private lateinit var useCase: AddTransactionUseCase
    
    @Before
    fun setup() {
        repository = mock()
        useCase = AddTransactionUseCase(repository)
    }
    
    @Test
    fun `addTransaction with valid data should return success`() = runTest {
        // Given
        val transaction = createValidTransaction()
        val expectedId = 123L
        whenever(repository.addTransaction(any())).thenReturn(expectedId)
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedId, (result as Result.Success).data)
        verify(repository).addTransaction(transaction)
    }
    
    @Test
    fun `addTransaction with zero amount should return error`() = runTest {
        // Given
        val transaction = createValidTransaction().copy(amount = 0.0)
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Error)
        assertEquals("Amount must be greater than 0", (result as Result.Error).exception.message)
    }
    
    @Test
    fun `addTransaction with negative amount should return error`() = runTest {
        // Given
        val transaction = createValidTransaction().copy(amount = -100.0)
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Error)
        assertEquals("Amount must be greater than 0", (result as Result.Error).exception.message)
    }
    
    @Test
    fun `addTransaction with blank description should return error`() = runTest {
        // Given
        val transaction = createValidTransaction().copy(description = "")
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Error)
        assertEquals("Description is required", (result as Result.Error).exception.message)
    }
    
    @Test
    fun `addTransaction with whitespace description should return error`() = runTest {
        // Given
        val transaction = createValidTransaction().copy(description = "   ")
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Error)
        assertEquals("Description is required", (result as Result.Error).exception.message)
    }
    
    @Test
    fun `addTransaction when repository throws exception should return error`() = runTest {
        // Given
        val transaction = createValidTransaction()
        val exception = Exception("Database error")
        whenever(repository.addTransaction(any())).thenThrow(exception)
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Error)
        assertEquals("Database error", (result as Result.Error).exception.message)
    }
    
    @Test
    fun `addTransaction with valid CREDIT transaction should succeed`() = runTest {
        // Given
        val transaction = createValidTransaction().copy(
            transactionType = TransactionType.CREDIT,
            amount = 5000.0,
            category = TransactionCategory.SALARY
        )
        val expectedId = 456L
        whenever(repository.addTransaction(any())).thenReturn(expectedId)
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedId, (result as Result.Success).data)
    }
    
    @Test
    fun `addTransaction with large amount should succeed`() = runTest {
        // Given
        val transaction = createValidTransaction().copy(amount = 1000000.0)
        val expectedId = 789L
        whenever(repository.addTransaction(any())).thenReturn(expectedId)
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedId, (result as Result.Success).data)
    }
    
    @Test
    fun `addTransaction with minimum valid amount should succeed`() = runTest {
        // Given
        val transaction = createValidTransaction().copy(amount = 0.01)
        val expectedId = 111L
        whenever(repository.addTransaction(any())).thenReturn(expectedId)
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedId, (result as Result.Success).data)
    }
    
    private fun createValidTransaction() = Transaction(
        transactionId = 0L,
        userId = 1L,
        accountId = 1L,
        amount = 100.0,
        transactionType = TransactionType.DEBIT,
        category = TransactionCategory.FOOD,
        description = "Test transaction",
        timestamp = System.currentTimeMillis()
    )
}

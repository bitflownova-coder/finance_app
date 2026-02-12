package com.financemanager.app.domain.usecase

import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionType
import com.financemanager.app.domain.repository.BudgetRepository
import com.financemanager.app.domain.repository.TransactionRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AddTransactionUseCaseTest {
    
    private lateinit var useCase: AddTransactionUseCase
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var budgetRepository: BudgetRepository
    
    @Before
    fun setup() {
        transactionRepository = mockk(relaxed = true)
        budgetRepository = mockk(relaxed = true)
        useCase = AddTransactionUseCase(transactionRepository, budgetRepository)
    }
    
    @Test
    fun `invoke with valid transaction succeeds`() = runTest {
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
        
        coEvery { transactionRepository.addTransaction(any()) } returns 1L
        
        // When
        val result = useCase(transaction)
        
        // Then
        assertEquals(1L, result)
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `invoke with negative amount throws exception`() = runTest {
        // Given
        val transaction = Transaction(
            userId = 1,
            accountId = 1,
            amount = -100.0,
            type = TransactionType.DEBIT,
            category = "Food",
            description = "Invalid",
            timestamp = System.currentTimeMillis()
        )
        
        // When
        useCase(transaction)
        
        // Then - exception expected
    }
    
    @Test(expected = IllegalArgumentException::class)
    fun `invoke with empty description throws exception`() = runTest {
        // Given
        val transaction = Transaction(
            userId = 1,
            accountId = 1,
            amount = 100.0,
            type = TransactionType.DEBIT,
            category = "Food",
            description = "",
            timestamp = System.currentTimeMillis()
        )
        
        // When
        useCase(transaction)
        
        // Then - exception expected
    }
}

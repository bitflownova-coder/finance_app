package com.financemanager.app.domain.usecase

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class PredictTransactionCategoryUseCaseTest {
    
    private lateinit var useCase: PredictTransactionCategoryUseCase
    
    @Before
    fun setup() {
        useCase = PredictTransactionCategoryUseCase()
    }
    
    @Test
    fun `predicts Food category for restaurant description`() {
        // When
        val predictions = useCase("Dinner at McDonald's")
        
        // Then
        assertFalse(predictions.isEmpty())
        assertEquals("Food & Dining", predictions.first().category)
        assertTrue(predictions.first().confidence > 0.5f)
    }
    
    @Test
    fun `predicts Shopping category for Amazon purchase`() {
        // When
        val predictions = useCase("Amazon purchase electronics")
        
        // Then
        assertFalse(predictions.isEmpty())
        assertEquals("Shopping", predictions.first().category)
    }
    
    @Test
    fun `predicts Transportation category for Uber ride`() {
        // When
        val predictions = useCase("Uber ride to office")
        
        // Then
        assertFalse(predictions.isEmpty())
        assertEquals("Transportation", predictions.first().category)
    }
    
    @Test
    fun `returns empty list for non-matching description`() {
        // When
        val predictions = useCase("xyz abc 123")
        
        // Then
        assertTrue(predictions.isEmpty())
    }
    
    @Test
    fun `getBestPrediction returns highest confidence category`() {
        // When
        val prediction = useCase.getBestPrediction("Starbucks coffee and restaurant dinner")
        
        // Then
        assertNotNull(prediction)
        assertEquals("Food & Dining", prediction?.category)
    }
    
    @Test
    fun `isConfidentPrediction returns true for high confidence`() {
        // Given
        val predictions = useCase("McDonald's restaurant")
        val bestPrediction = predictions.first()
        
        // When
        val isConfident = useCase.isConfidentPrediction(bestPrediction, threshold = 0.3f)
        
        // Then
        assertTrue(isConfident)
    }
    
    @Test
    fun `handles empty description`() {
        // When
        val predictions = useCase("")
        
        // Then
        assertTrue(predictions.isEmpty())
    }
    
    @Test
    fun `handles mixed case description`() {
        // When
        val predictions = useCase("UBER Ride")
        
        // Then
        assertFalse(predictions.isEmpty())
        assertEquals("Transportation", predictions.first().category)
    }
}

package com.financemanager.app.util

import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class DateUtilsTest {
    
    @Test
    fun `getStartOfMonth returns correct timestamp`() {
        // Given
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 15, 10, 30, 45)
        val date = calendar.time
        
        // When
        val startOfMonth = DateUtils.getStartOfMonth(date)
        
        // Then
        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = startOfMonth
        
        assertEquals(1, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(0, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, resultCalendar.get(Calendar.MINUTE))
        assertEquals(0, resultCalendar.get(Calendar.SECOND))
    }
    
    @Test
    fun `getEndOfMonth returns correct timestamp`() {
        // Given
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 15)
        val date = calendar.time
        
        // When
        val endOfMonth = DateUtils.getEndOfMonth(date)
        
        // Then
        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = endOfMonth
        
        val lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        assertEquals(lastDay, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(23, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, resultCalendar.get(Calendar.MINUTE))
    }
    
    @Test
    fun `getStartOfDay returns correct timestamp`() {
        // Given
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 15, 14, 30, 45)
        val date = calendar.time
        
        // When
        val startOfDay = DateUtils.getStartOfDay(date)
        
        // Then
        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = startOfDay
        
        assertEquals(15, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(0, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(0, resultCalendar.get(Calendar.MINUTE))
        assertEquals(0, resultCalendar.get(Calendar.SECOND))
    }
    
    @Test
    fun `getEndOfDay returns correct timestamp`() {
        // Given
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 15, 10, 30)
        val date = calendar.time
        
        // When
        val endOfDay = DateUtils.getEndOfDay(date)
        
        // Then
        val resultCalendar = Calendar.getInstance()
        resultCalendar.timeInMillis = endOfDay
        
        assertEquals(15, resultCalendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(23, resultCalendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(59, resultCalendar.get(Calendar.MINUTE))
    }
    
    @Test
    fun `formatCurrency formats correctly`() {
        // When
        val result = DateUtils.formatCurrency(12345.67)
        
        // Then
        assertTrue(result.contains("12,345") || result.contains("12345"))
        assertTrue(result.contains("67"))
    }
    
    @Test
    fun `formatDate formats timestamp correctly`() {
        // Given
        val calendar = Calendar.getInstance()
        calendar.set(2026, Calendar.FEBRUARY, 8, 0, 0, 0)
        val timestamp = calendar.timeInMillis
        
        // When
        val result = DateUtils.formatDate(timestamp)
        
        // Then
        assertTrue(result.contains("Feb") || result.contains("02"))
        assertTrue(result.contains("08") || result.contains("8"))
        assertTrue(result.contains("2026"))
    }
}

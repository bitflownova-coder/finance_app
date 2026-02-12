package com.financemanager.app.domain.usecase.calendar

import com.financemanager.app.domain.model.CalendarDay
import com.financemanager.app.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GenerateCalendarUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    
    suspend operator fun invoke(userId: Long, yearMonth: YearMonth): List<CalendarDay> {
        val firstOfMonth = yearMonth.atDay(1)
        val lastOfMonth = yearMonth.atEndOfMonth()
        
        // Get first day of calendar (might be from previous month)
        val startDate = firstOfMonth.minusDays(firstOfMonth.dayOfWeek.value.toLong() % 7)
        
        // Get last day of calendar (might be from next month)
        val endDate = lastOfMonth.plusDays((6 - lastOfMonth.dayOfWeek.value.toLong() % 7))
        
        // Get transactions for the period
        val transactions = transactionRepository.getTransactionsByDateRange(
            userId,
            startDate.toEpochDay() * 86400000,
            endDate.toEpochDay() * 86400000 + 86399999
        ).first()
        
        // Group transactions by date
        val transactionsByDate = transactions.groupBy { 
            java.time.Instant.ofEpochMilli(it.timestamp)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
        }
        
        // Generate calendar days
        val days = mutableListOf<CalendarDay>()
        var currentDate = startDate
        
        while (currentDate <= endDate) {
            val dayTransactions = transactionsByDate[currentDate] ?: emptyList()
            
            val income = dayTransactions
                .filter { it.transactionType == com.financemanager.app.domain.model.TransactionType.CREDIT }
                .sumOf { it.amount }
            
            val expense = dayTransactions
                .filter { it.transactionType == com.financemanager.app.domain.model.TransactionType.DEBIT }
                .sumOf { it.amount }
            
            days.add(CalendarDay(
                date = currentDate,
                isCurrentMonth = YearMonth.from(currentDate) == yearMonth,
                isToday = currentDate == LocalDate.now(),
                transactionCount = dayTransactions.size,
                totalIncome = income,
                totalExpense = expense,
                hasTransactions = dayTransactions.isNotEmpty()
            ))
            
            currentDate = currentDate.plusDays(1)
        }
        
        return days
    }
}

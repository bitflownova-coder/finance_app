package com.financemanager.app.presentation.calendar

import com.financemanager.app.domain.model.CalendarDay
import com.financemanager.app.domain.model.Transaction
import java.time.YearMonth

data class CalendarUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val calendarDays: List<CalendarDay> = emptyList(),
    val selectedDate: CalendarDay? = null,
    val transactionsForSelectedDate: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class CalendarEvent {
    data class LoadCalendar(val userId: Long, val yearMonth: YearMonth) : CalendarEvent()
    data class SelectDate(val day: CalendarDay) : CalendarEvent()
    object PreviousMonth : CalendarEvent()
    object NextMonth : CalendarEvent()
    object Today : CalendarEvent()
    object DismissError : CalendarEvent()
}

package com.financemanager.app.domain.model

import java.time.LocalDate

/**
 * Calendar day with transaction summary
 */
data class CalendarDay(
    val date: LocalDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean,
    val transactionCount: Int = 0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val hasTransactions: Boolean = false
) {
    val netAmount: Double
        get() = totalIncome - totalExpense
}

/**
 * Calendar month data
 */
data class CalendarMonth(
    val year: Int,
    val month: Int,
    val days: List<CalendarDay>
)

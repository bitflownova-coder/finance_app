package com.financemanager.app.presentation.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.calendar.GenerateCalendarUseCase
import com.financemanager.app.domain.repository.TransactionRepository
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val generateCalendarUseCase: GenerateCalendarUseCase,
    private val transactionRepository: TransactionRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        loadCalendar(YearMonth.now())
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.LoadCalendar -> loadCalendar(event.yearMonth)
            is CalendarEvent.SelectDate -> selectDate(event.day)
            is CalendarEvent.PreviousMonth -> navigateToPreviousMonth()
            is CalendarEvent.NextMonth -> navigateToNextMonth()
            is CalendarEvent.Today -> navigateToToday()
            is CalendarEvent.DismissError -> dismissError()
        }
    }

    private fun loadCalendar(yearMonth: YearMonth) {
        val userId = sessionManager.getUserId() ?: 0L
        if (userId == 0L) {
            _uiState.update { it.copy(error = "User not logged in") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, yearMonth = yearMonth) }
            try {
                val calendarDays = generateCalendarUseCase(userId, yearMonth)
                _uiState.update { 
                    it.copy(
                        calendarDays = calendarDays,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load calendar"
                    )
                }
            }
        }
    }

    private fun selectDate(day: com.financemanager.app.domain.model.CalendarDay) {
        val userId = sessionManager.getUserId() ?: 0L
        if (userId == 0L) return

        viewModelScope.launch {
            _uiState.update { it.copy(selectedDate = day, isLoading = true) }
            try {
                val startOfDay = day.date.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                val endOfDay = day.date.atTime(23, 59, 59).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
                
                transactionRepository.getTransactionsByDateRange(userId, startOfDay, endOfDay)
                    .first()
                    .let { transactions ->
                        _uiState.update { 
                            it.copy(
                                transactionsForSelectedDate = transactions,
                                isLoading = false
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load transactions"
                    )
                }
            }
        }
    }

    private fun navigateToPreviousMonth() {
        val newYearMonth = _uiState.value.yearMonth.minusMonths(1)
        loadCalendar(newYearMonth)
    }

    private fun navigateToNextMonth() {
        val newYearMonth = _uiState.value.yearMonth.plusMonths(1)
        loadCalendar(newYearMonth)
    }

    private fun navigateToToday() {
        val userId = sessionManager.getUserId() ?: 0L
        if (userId == 0L) {
            _uiState.update { it.copy(error = "User not logged in") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, yearMonth = YearMonth.now()) }
            try {
                // Load calendar for current month
                val calendarDays = generateCalendarUseCase(userId, YearMonth.now())
                _uiState.update { 
                    it.copy(
                        calendarDays = calendarDays,
                        isLoading = false,
                        error = null
                    )
                }
                
                // Select today's date and load transactions
                val todayDate = java.time.LocalDate.now()
                calendarDays.find { it.date == todayDate }?.let { todayCalendarDay ->
                    selectDate(todayCalendarDay)
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load calendar"
                    )
                }
            }
        }
    }

    private fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}

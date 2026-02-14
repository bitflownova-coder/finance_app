package com.financemanager.app.presentation.splitbill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.SplitBill
import com.financemanager.app.domain.usecase.AddSplitBillUseCase
import com.financemanager.app.domain.usecase.GetSplitBillsUseCase
import com.financemanager.app.domain.usecase.MarkParticipantPaidUseCase
import com.financemanager.app.domain.repository.SplitBillRepository
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplitBillViewModel @Inject constructor(
    private val getSplitBillsUseCase: GetSplitBillsUseCase,
    private val addSplitBillUseCase: AddSplitBillUseCase,
    private val markParticipantPaidUseCase: MarkParticipantPaidUseCase,
    private val repository: SplitBillRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SplitBillUiState())
    val uiState: StateFlow<SplitBillUiState> = _uiState.asStateFlow()
    
    private val currentUserId: Long
        get() = sessionManager.getUserId() ?: 0L
    
    init {
        loadSplitBills()
    }
    
    fun onEvent(event: SplitBillEvent) {
        when (event) {
            is SplitBillEvent.LoadSplitBills -> loadSplitBills()
            is SplitBillEvent.SelectBill -> selectBill(event.bill)
            is SplitBillEvent.AddSplitBill -> addSplitBill(event)
            is SplitBillEvent.MarkParticipantPaid -> markParticipantPaid(event.participantId)
            is SplitBillEvent.MarkAsSettled -> markAsSettled(event.splitId)
            is SplitBillEvent.DeleteBill -> deleteBill(event.bill)
            is SplitBillEvent.SendReminder -> sendReminder(event.participant)
            is SplitBillEvent.FilterChanged -> {
                _uiState.update { it.copy(currentFilter = event.filter) }
            }
            is SplitBillEvent.ShowAddDialog -> {
                _uiState.update { it.copy(showAddDialog = true) }
            }
            is SplitBillEvent.DismissDialog -> {
                _uiState.update { 
                    it.copy(
                        showAddDialog = false,
                        showDetailsDialog = false,
                        showSendReminderDialog = false,
                        selectedBill = null
                    )
                }
            }
            is SplitBillEvent.DismissError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
        }
    }
    
    private fun loadSplitBills() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            combine(
                getSplitBillsUseCase(currentUserId),
                getSplitBillsUseCase.getUnSettled(currentUserId),
                repository.getUnSettledCount(currentUserId)
            ) { allBills, unSettledBills, unSettledCount ->
                Triple(allBills, unSettledBills, unSettledCount)
            }
            .catch { e ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load split bills"
                    )
                }
            }
            .collect { (allBills, unSettledBills, unSettledCount) ->
                val totalUnSettled = repository.getTotalUnSettledAmount(currentUserId)
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        splitBills = allBills,
                        unSettledBills = unSettledBills,
                        unSettledCount = unSettledCount,
                        totalUnSettledAmount = totalUnSettled
                    )
                }
            }
        }
    }
    
    private fun selectBill(bill: SplitBill) {
        _uiState.update { 
            it.copy(
                selectedBill = bill,
                showDetailsDialog = true
            )
        }
    }
    
    private fun addSplitBill(event: SplitBillEvent.AddSplitBill) {
        viewModelScope.launch {
            try {
                val splitBill = SplitBill(
                    transactionId = event.transactionId,
                    userId = currentUserId, // Use actual logged-in user ID
                    totalAmount = event.totalAmount,
                    description = event.description,
                    splitType = event.splitType,
                    participants = event.participants
                )
                
                addSplitBillUseCase(splitBill)
                loadSplitBills() // Reload to show the new bill
                
                _uiState.update { it.copy(showAddDialog = false) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = e.message ?: "Failed to add split bill")
                }
            }
        }
    }
    
    private fun markParticipantPaid(participantId: Long) {
        viewModelScope.launch {
            try {
                markParticipantPaidUseCase(participantId)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = e.message ?: "Failed to mark as paid")
                }
            }
        }
    }
    
    private fun markAsSettled(splitId: Long) {
        viewModelScope.launch {
            try {
                repository.markAsSettled(splitId)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = e.message ?: "Failed to mark as settled")
                }
            }
        }
    }
    
    private fun deleteBill(bill: SplitBill) {
        viewModelScope.launch {
            try {
                repository.deleteSplitBill(bill)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(errorMessage = e.message ?: "Failed to delete bill")
                }
            }
        }
    }
    
    private fun sendReminder(participant: com.financemanager.app.domain.model.Participant) {
        // TODO: Implement SMS/WhatsApp reminder functionality
        val message = "Hi ${participant.name}, reminder: you owe ₹${participant.shareAmount} for split bill"
        _uiState.update { 
            it.copy(
                showSendReminderDialog = false,
                errorMessage = "Reminder feature coming soon!"
            )
        }
    }
}

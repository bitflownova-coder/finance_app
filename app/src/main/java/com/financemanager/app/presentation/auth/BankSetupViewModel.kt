package com.financemanager.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.AccountType
import com.financemanager.app.domain.model.BankAccount
import com.financemanager.app.domain.usecase.account.AddAccountUseCase
import com.financemanager.app.util.Result
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Bank Setup screen (Step 2)
 */
@HiltViewModel
class BankSetupViewModel @Inject constructor(
    private val addAccountUseCase: AddAccountUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<BankSetupUiState>(BankSetupUiState.Initial)
    val uiState: StateFlow<BankSetupUiState> = _uiState.asStateFlow()
    
    fun setupBank(bankName: String, initialAmount: String) {
        val userId = sessionManager.getUserId()
        if (userId == null) {
            _uiState.value = BankSetupUiState.Error("Session expired. Please restart setup.")
            return
        }
        
        if (bankName.isBlank()) {
            _uiState.value = BankSetupUiState.Error("Bank name cannot be empty")
            return
        }
        
        val amount = initialAmount.toDoubleOrNull()
        if (amount == null || amount < 0) {
            _uiState.value = BankSetupUiState.Error("Please enter a valid amount")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = BankSetupUiState.Loading
            
            val account = BankAccount(
                userId = userId,
                accountName = "$bankName Savings",
                accountNumber = "XXXX",
                bankName = bankName.trim(),
                accountType = AccountType.SAVINGS,
                balance = amount,
                isPrimary = true
            )
            
            when (val result = addAccountUseCase(account)) {
                is Result.Success -> {
                    _uiState.value = BankSetupUiState.Success
                }
                is Result.Error -> {
                    _uiState.value = BankSetupUiState.Error(
                        result.exception.message ?: "Failed to add account"
                    )
                }
                is Result.Loading -> { }
            }
        }
    }
    
    fun resetState() {
        _uiState.value = BankSetupUiState.Initial
    }
}

sealed class BankSetupUiState {
    object Initial : BankSetupUiState()
    object Loading : BankSetupUiState()
    object Success : BankSetupUiState()
    data class Error(val message: String) : BankSetupUiState()
}

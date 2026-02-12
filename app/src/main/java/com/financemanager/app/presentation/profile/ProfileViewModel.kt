package com.financemanager.app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.account.AddAccountUseCase
import com.financemanager.app.domain.usecase.account.CalculateTotalBalanceUseCase
import com.financemanager.app.domain.usecase.account.DeleteAccountUseCase
import com.financemanager.app.domain.usecase.account.GetAccountsUseCase
import com.financemanager.app.domain.usecase.account.UpdateAccountUseCase
import com.financemanager.app.util.Result
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Profile screen
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val addAccountUseCase: AddAccountUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val calculateTotalBalanceUseCase: CalculateTotalBalanceUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadUserData()
    }
    
    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadUserData -> loadUserData()
            is ProfileEvent.AddAccountClicked -> showAddAccountDialog()
            is ProfileEvent.EditAccountClicked -> showEditAccountDialog(event.account)
            is ProfileEvent.DeleteAccountClicked -> deleteAccount(event.accountId)
            is ProfileEvent.SaveAccount -> saveAccount(event.account)
            is ProfileEvent.DismissDialog -> dismissDialog()
            is ProfileEvent.Logout -> logout()
        }
    }
    
    private fun loadUserData() {
        val userId = sessionManager.getUserId() ?: return
        val userName = sessionManager.getUserName() ?: ""
        val userPhone = sessionManager.getUserPhone() ?: ""
        
        _uiState.update { it.copy(
            userName = userName,
            userPhone = userPhone,
            isLoading = true
        )}
        
        // Load accounts
        viewModelScope.launch {
            getAccountsUseCase(userId).collect { accounts ->
                val totalBalance = calculateTotalBalanceUseCase(userId)
                _uiState.update { it.copy(
                    accounts = accounts,
                    totalBalance = totalBalance,
                    isLoading = false
                )}
            }
        }
    }
    
    private fun showAddAccountDialog() {
        _uiState.update { it.copy(
            isAccountDialogVisible = true,
            selectedAccount = null
        )}
    }
    
    private fun showEditAccountDialog(account: com.financemanager.app.domain.model.BankAccount) {
        _uiState.update { it.copy(
            isAccountDialogVisible = true,
            selectedAccount = account
        )}
    }
    
    private fun dismissDialog() {
        _uiState.update { it.copy(
            isAccountDialogVisible = false,
            selectedAccount = null,
            error = null
        )}
    }
    
    private fun saveAccount(account: com.financemanager.app.domain.model.BankAccount) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val result = if (account.accountId == 0L) {
                // Add new account
                addAccountUseCase(account.copy(userId = sessionManager.getUserId() ?: 0))
            } else {
                // Update existing account
                updateAccountUseCase(account)
            }
            
            when (result) {
                is Result.Success -> {
                    dismissDialog()
                    loadUserData()
                }
                is Result.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.exception.message
                    )}
                }
                is Result.Loading -> {
                    // Not used in this case
                }
            }
        }
    }
    
    private fun deleteAccount(accountId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            when (val result = deleteAccountUseCase(accountId)) {
                is Result.Success -> {
                    loadUserData()
                }
                is Result.Error -> {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = result.exception.message
                    )}
                }
                is Result.Loading -> {
                    // Not used in this case
                }
            }
        }
    }
    
    private fun logout() {
        sessionManager.clearSession()
    }
}

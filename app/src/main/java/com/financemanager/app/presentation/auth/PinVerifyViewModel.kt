package com.financemanager.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.auth.VerifyPinUseCase
import com.financemanager.app.util.Result
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for PIN Verify screen (returning user unlock)
 */
@HiltViewModel
class PinVerifyViewModel @Inject constructor(
    private val verifyPinUseCase: VerifyPinUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<PinVerifyUiState>(PinVerifyUiState.Initial)
    val uiState: StateFlow<PinVerifyUiState> = _uiState.asStateFlow()
    
    fun verifyPin(pin: String) {
        val userId = sessionManager.getUserId()
        if (userId == null) {
            _uiState.value = PinVerifyUiState.Error("No account found")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = PinVerifyUiState.Loading
            
            when (val result = verifyPinUseCase(userId, pin)) {
                is Result.Success -> {
                    _uiState.value = PinVerifyUiState.Success
                }
                is Result.Error -> {
                    _uiState.value = PinVerifyUiState.Error(
                        result.exception.message ?: "Incorrect PIN"
                    )
                }
                is Result.Loading -> { }
            }
        }
    }
    
    fun resetApp() {
        sessionManager.clearSession()
    }
    
    fun resetState() {
        _uiState.value = PinVerifyUiState.Initial
    }
}

sealed class PinVerifyUiState {
    object Initial : PinVerifyUiState()
    object Loading : PinVerifyUiState()
    object Success : PinVerifyUiState()
    data class Error(val message: String) : PinVerifyUiState()
}

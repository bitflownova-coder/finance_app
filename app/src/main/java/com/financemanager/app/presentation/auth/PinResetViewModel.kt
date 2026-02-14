package com.financemanager.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.UpdateUserPinUseCase
import com.financemanager.app.util.SecurityUtils
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for PIN reset/forgot PIN flow
 */
@HiltViewModel
class PinResetViewModel @Inject constructor(
    private val updateUserPinUseCase: UpdateUserPinUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<PinResetUiState>(PinResetUiState.Initial)
    val uiState: StateFlow<PinResetUiState> = _uiState.asStateFlow()
    
    private var newPin: String = ""
    
    fun setNewPin(pin: String) {
        newPin = pin
    }
    
    fun confirmPin(confirmPin: String) {
        if (newPin.isEmpty()) {
            _uiState.value = PinResetUiState.Error("Please enter new PIN first")
            return
        }
        
        if (newPin != confirmPin) {
            _uiState.value = PinResetUiState.Error("PINs do not match")
            return
        }
        
        if (newPin.length != 4) {
            _uiState.value = PinResetUiState.Error("PIN must be 4 digits")
            return
        }
        
        updatePin(newPin)
    }
    
    private fun updatePin(pin: String) {
        val userId = sessionManager.getUserId()
        if (userId == null) {
            _uiState.value = PinResetUiState.Error("User not found")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = PinResetUiState.Loading
            
            try {
                val hashedPin = SecurityUtils.hashPassword(pin)
                val result = updateUserPinUseCase(userId, hashedPin)
                
                when (result) {
                    is com.financemanager.app.util.Result.Success -> {
                        _uiState.value = PinResetUiState.Success
                    }
                    is com.financemanager.app.util.Result.Error -> {
                        _uiState.value = PinResetUiState.Error(
                            result.exception.message ?: "Failed to reset PIN"
                        )
                    }
                    is com.financemanager.app.util.Result.Loading -> {
                        // Already in loading state, do nothing
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PinResetUiState.Error(
                    e.message ?: "Failed to reset PIN"
                )
            }
        }
    }
    
    fun resetState() {
        _uiState.value = PinResetUiState.Initial
    }
}

/**
 * UI States for PIN reset
 */
sealed class PinResetUiState {
    object Initial : PinResetUiState()
    object Loading : PinResetUiState()
    object Success : PinResetUiState()
    data class Error(val message: String) : PinResetUiState()
}

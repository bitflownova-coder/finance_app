package com.financemanager.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.auth.SetupPinUseCase
import com.financemanager.app.util.Result
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for PIN Setup screen (Step 3)
 */
@HiltViewModel
class PinSetupViewModel @Inject constructor(
    private val setupPinUseCase: SetupPinUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<PinSetupUiState>(PinSetupUiState.Initial)
    val uiState: StateFlow<PinSetupUiState> = _uiState.asStateFlow()
    
    fun setupPin(pin: String, confirmPin: String) {
        val userId = sessionManager.getUserId()
        if (userId == null) {
            _uiState.value = PinSetupUiState.Error("Session expired. Please restart setup.")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = PinSetupUiState.Loading
            
            when (val result = setupPinUseCase(userId, pin, confirmPin)) {
                is Result.Success -> {
                    sessionManager.markSetupComplete()
                    _uiState.value = PinSetupUiState.Success
                }
                is Result.Error -> {
                    _uiState.value = PinSetupUiState.Error(
                        result.exception.message ?: "Failed to set PIN"
                    )
                }
                is Result.Loading -> { }
            }
        }
    }
    
    fun resetState() {
        _uiState.value = PinSetupUiState.Initial
    }
}

sealed class PinSetupUiState {
    object Initial : PinSetupUiState()
    object Loading : PinSetupUiState()
    object Success : PinSetupUiState()
    data class Error(val message: String) : PinSetupUiState()
}

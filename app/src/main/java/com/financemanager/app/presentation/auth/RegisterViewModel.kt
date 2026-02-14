package com.financemanager.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.usecase.auth.RegisterUseCase
import com.financemanager.app.util.Result
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Register screen (Step 1: Name + Phone)
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    
    fun register(fullName: String, phone: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            
            when (val result = registerUseCase(fullName, phone)) {
                is Result.Success -> {
                    sessionManager.saveUser(result.data, fullName, phone)
                    _uiState.value = RegisterUiState.Success(result.data)
                }
                is Result.Error -> {
                    _uiState.value = RegisterUiState.Error(
                        result.exception.message ?: "Registration failed"
                    )
                }
                is Result.Loading -> { }
            }
        }
    }
    
    fun resetState() {
        _uiState.value = RegisterUiState.Initial
    }
}

sealed class RegisterUiState {
    object Initial : RegisterUiState()
    object Loading : RegisterUiState()
    data class Success(val userId: Long) : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

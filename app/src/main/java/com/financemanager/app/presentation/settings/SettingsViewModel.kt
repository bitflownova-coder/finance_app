package com.financemanager.app.presentation.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.financemanager.app.domain.model.AppTheme
import com.financemanager.app.util.SessionManager
import com.financemanager.app.util.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Settings screen
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    application: Application
) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.UpdateTheme -> updateTheme(event.theme)
            is SettingsEvent.UpdateCurrency -> updateCurrency(event.currencyCode)
            is SettingsEvent.UpdateNotifications -> updateNotifications(event.enabled)
            is SettingsEvent.UpdateBudgetAlerts -> updateBudgetAlerts(event.enabled)
            is SettingsEvent.UpdateBiometric -> updateBiometric(event.enabled)
            is SettingsEvent.UpdateAutoBackup -> updateAutoBackup(event.enabled)
        }
    }
    
    private fun loadSettings() {
        val context = getApplication<Application>()
        val currentTheme = ThemeManager.getThemePreference(context)
        val theme = when (currentTheme) {
            ThemeManager.THEME_LIGHT -> AppTheme.LIGHT
            ThemeManager.THEME_DARK -> AppTheme.DARK
            else -> AppTheme.SYSTEM
        }
        
        _uiState.update {
            it.copy(
                theme = theme,
                currency = "INR",
                currencySymbol = "₹",
                notificationsEnabled = true,
                budgetAlertsEnabled = true,
                biometricEnabled = false,
                autoBackupEnabled = false
            )
        }
    }
    
    private fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            val context = getApplication<Application>()
            val themeValue = when (theme) {
                AppTheme.LIGHT -> ThemeManager.THEME_LIGHT
                AppTheme.DARK -> ThemeManager.THEME_DARK
                AppTheme.SYSTEM -> ThemeManager.THEME_SYSTEM
            }
            ThemeManager.setThemePreference(context, themeValue)
            _uiState.update { it.copy(theme = theme) }
        }
    }
    
    private fun updateCurrency(code: String) {
        val symbol = when (code) {
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "JPY" -> "¥"
            else -> "₹"
        }
        _uiState.update { it.copy(currency = code, currencySymbol = symbol) }
    }
    
    private fun updateNotifications(enabled: Boolean) {
        _uiState.update { it.copy(notificationsEnabled = enabled) }
    }
    
    private fun updateBudgetAlerts(enabled: Boolean) {
        _uiState.update { it.copy(budgetAlertsEnabled = enabled) }
    }
    
    private fun updateBiometric(enabled: Boolean) {
        _uiState.update { it.copy(biometricEnabled = enabled) }
    }
    
    private fun updateAutoBackup(enabled: Boolean) {
        _uiState.update { it.copy(autoBackupEnabled = enabled) }
        if (enabled) {
            performBackup()
        }
    }
    
    private fun performBackup() {
        viewModelScope.launch {
            // TODO: Implement backup logic
            _uiState.update { it.copy(lastBackupTime = System.currentTimeMillis()) }
        }
    }
}

data class SettingsUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val currency: String = "INR",
    val currencySymbol: String = "₹",
    val notificationsEnabled: Boolean = true,
    val budgetAlertsEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val autoBackupEnabled: Boolean = false,
    val lastBackupTime: Long? = null
)

sealed class SettingsEvent {
    data class UpdateTheme(val theme: AppTheme) : SettingsEvent()
    data class UpdateCurrency(val currencyCode: String) : SettingsEvent()
    data class UpdateNotifications(val enabled: Boolean) : SettingsEvent()
    data class UpdateBudgetAlerts(val enabled: Boolean) : SettingsEvent()
    data class UpdateBiometric(val enabled: Boolean) : SettingsEvent()
    data class UpdateAutoBackup(val enabled: Boolean) : SettingsEvent()
}

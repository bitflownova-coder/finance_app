package com.financemanager.app.presentation.profile

import com.financemanager.app.domain.model.BankAccount

/**
 * UI state for Profile screen
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val userPhone: String = "",
    val userName: String = "",
    val accounts: List<BankAccount> = emptyList(),
    val totalBalance: Double = 0.0,
    val error: String? = null,
    val isAccountDialogVisible: Boolean = false,
    val selectedAccount: BankAccount? = null
)

/**
 * Events that can be triggered from the Profile screen
 */
sealed class ProfileEvent {
    data object LoadUserData : ProfileEvent()
    data object AddAccountClicked : ProfileEvent()
    data class EditAccountClicked(val account: BankAccount) : ProfileEvent()
    data class DeleteAccountClicked(val accountId: Long) : ProfileEvent()
    data class SaveAccount(val account: BankAccount) : ProfileEvent()
    data object DismissDialog : ProfileEvent()
    data object Logout : ProfileEvent()
}

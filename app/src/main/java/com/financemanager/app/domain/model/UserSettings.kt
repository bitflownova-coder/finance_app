package com.financemanager.app.domain.model

/**
 * Domain model for user settings and preferences
 */
data class UserSettings(
    val userId: Long,
    val userName: String,
    val userEmail: String,
    val phoneNumber: String? = null,
    val profilePicturePath: String? = null,
    val theme: AppTheme = AppTheme.SYSTEM,
    val currency: String = "INR",
    val currencySymbol: String = "₹",
    val notificationsEnabled: Boolean = true,
    val budgetAlertsEnabled: Boolean = true,
    val transactionRemindersEnabled: Boolean = true,
    val biometricEnabled: Boolean = false,
    val autoBackupEnabled: Boolean = false,
    val lastBackupTime: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM;
    
    companion object {
        fun fromString(value: String): AppTheme {
            return values().find { it.name == value.uppercase() } ?: SYSTEM
        }
    }
}

data class CurrencyOption(
    val code: String,
    val name: String,
    val symbol: String
) {
    companion object {
        val SUPPORTED_CURRENCIES = listOf(
            CurrencyOption("INR", "Indian Rupee", "₹"),
            CurrencyOption("USD", "US Dollar", "$"),
            CurrencyOption("EUR", "Euro", "€"),
            CurrencyOption("GBP", "British Pound", "£"),
            CurrencyOption("JPY", "Japanese Yen", "¥"),
            CurrencyOption("AUD", "Australian Dollar", "A$"),
            CurrencyOption("CAD", "Canadian Dollar", "C$"),
            CurrencyOption("CNY", "Chinese Yuan", "¥")
        )
        
        fun getCurrency(code: String): CurrencyOption {
            return SUPPORTED_CURRENCIES.find { it.code == code } 
                ?: SUPPORTED_CURRENCIES.first()
        }
    }
}

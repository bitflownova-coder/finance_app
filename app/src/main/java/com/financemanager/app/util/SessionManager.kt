package com.financemanager.app.util

import android.content.SharedPreferences
import com.financemanager.app.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages user session and setup state
 */
@Singleton
class SessionManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    
    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_PHONE = "user_phone"
        private const val KEY_SETUP_COMPLETE = "setup_complete"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    /**
     * Save user after registration (step 1)
     */
    fun saveUser(userId: Long, name: String, phone: String) {
        sharedPreferences.edit().apply {
            putLong(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, name)
            putString(KEY_USER_PHONE, phone)
            apply()
        }
    }
    
    /**
     * Save session after full setup (after PIN set)
     */
    fun saveSession(user: User) {
        sharedPreferences.edit().apply {
            putLong(KEY_USER_ID, user.userId)
            putString(KEY_USER_NAME, user.fullName)
            putString(KEY_USER_PHONE, user.phone)
            putBoolean(KEY_SETUP_COMPLETE, true)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    /**
     * Mark setup as complete (after PIN is set)
     */
    fun markSetupComplete() {
        sharedPreferences.edit().apply {
            putBoolean(KEY_SETUP_COMPLETE, true)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }
    
    /**
     * Get current user ID (available after registration)
     */
    fun getUserId(): Long? {
        val id = sharedPreferences.getLong(KEY_USER_ID, -1)
        return if (id != -1L) id else null
    }
    
    /**
     * Get current user phone
     */
    fun getUserPhone(): String? {
        return sharedPreferences.getString(KEY_USER_PHONE, null)
    }
    
    /**
     * Get current user name
     */
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, null)
    }
    
    /**
     * Check if user has completed full setup
     */
    fun isSetupComplete(): Boolean {
        return sharedPreferences.getBoolean(KEY_SETUP_COMPLETE, false)
    }
    
    /**
     * Check if user is logged in (setup complete)
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Clear user session (logout / reset)
     */
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }
    
    /**
     * Refresh session - no-op for PIN-based auth
     */
    fun refreshSession() {
        // No session timeout for PIN-based auth
    }
}

package com.financemanager.app.util

import at.favre.lib.crypto.bcrypt.BCrypt

/**
 * Utility object for password hashing and verification using BCrypt
 */
object SecurityUtils {
    
    private const val BCRYPT_COST = 12
    
    /**
     * Hash a password using BCrypt with 12 rounds
     * @param password Plain text password
     * @return Hashed password string
     */
    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, password.toCharArray())
    }
    
    /**
     * Verify a password against its hash
     * @param password Plain text password to verify
     * @param passwordHash Stored hash to verify against
     * @return true if password matches, false otherwise
     */
    fun verifyPassword(password: String, passwordHash: String): Boolean {
        val result = BCrypt.verifyer().verify(password.toCharArray(), passwordHash)
        return result.verified
    }
    
    /**
     * Validate password strength
     * Must be at least 8 characters with 1 uppercase, 1 lowercase, 1 digit, and 1 special character
     */
    fun validatePasswordStrength(password: String): Boolean {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        return password.matches(Regex(passwordPattern))
    }
    
    /**
     * Validate email format
     */
    fun validateEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(Regex(emailPattern))
    }
}

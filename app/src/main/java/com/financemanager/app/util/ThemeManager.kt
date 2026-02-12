package com.financemanager.app.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

/**
 * Theme Manager to handle app theme switching
 */
object ThemeManager {
    
    private const val THEME_PREF = "theme_preferences"
    private const val THEME_KEY = "theme_preference"
    
    const val THEME_LIGHT = "light"
    const val THEME_DARK = "dark"
    const val THEME_SYSTEM = "system"
    
    /**
     * Apply theme based on saved preference
     */
    fun applyTheme(context: Context) {
        val theme = getThemePreference(context)
        applyTheme(theme)
    }
    
    /**
     * Apply specific theme
     */
    fun applyTheme(theme: String) {
        when (theme) {
            THEME_LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            THEME_DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            THEME_SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }
    
    /**
     * Get current theme preference
     */
    fun getThemePreference(context: Context): String {
        val prefs = context.getSharedPreferences(THEME_PREF, Context.MODE_PRIVATE)
        return prefs.getString(THEME_KEY, THEME_SYSTEM) ?: THEME_SYSTEM
    }
    
    /**
     * Save theme preference
     */
    fun setThemePreference(context: Context, theme: String) {
        val prefs = context.getSharedPreferences(THEME_PREF, Context.MODE_PRIVATE)
        prefs.edit().putString(THEME_KEY, theme).apply()
        applyTheme(theme)
    }
    
    /**
     * Check if dark mode is currently active
     */
    fun isDarkMode(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and 
            android.content.res.Configuration.UI_MODE_NIGHT_MASK) {
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }
    }
}

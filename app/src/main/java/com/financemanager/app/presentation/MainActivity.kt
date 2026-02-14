package com.financemanager.app.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.financemanager.app.R
import com.financemanager.app.databinding.ActivityMainBinding
import com.financemanager.app.util.SessionManager
import com.financemanager.app.util.ThemeManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main Activity hosting the navigation graph
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    @Inject
    lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch from splash theme to app theme
        setTheme(R.style.Theme_FinanceManager)
        
        // Apply theme before calling super.onCreate()
        ThemeManager.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupNavigation()
    }
    
    override fun onResume() {
        super.onResume()
        updateBottomNavigationVisibility()
    }
    
    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Set up bottom navigation if present
        val bottomNav: BottomNavigationView? = binding.bottomNavigation
        bottomNav?.setupWithNavController(navController)
        
        // Hide bottom nav on auth/setup screens
        val authDestinations = setOf(
            R.id.splashFragment,
            R.id.onboardingFragment,
            R.id.registerFragment,
            R.id.bankSetupFragment,
            R.id.pinSetupFragment,
            R.id.pinVerifyFragment
        )
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.isVisible = destination.id !in authDestinations
        }
    }
    
    private fun updateBottomNavigationVisibility() {
        // Handled by destination change listener
    }
}

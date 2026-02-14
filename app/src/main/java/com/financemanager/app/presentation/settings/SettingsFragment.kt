package com.financemanager.app.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentSettingsBinding
import com.financemanager.app.domain.model.AppTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Settings Fragment for app configuration
 */
@AndroidEntryPoint
class SettingsFragment : Fragment() {
    
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: SettingsViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeUiState()
    }
    
    private fun setupClickListeners() {
        binding.apply {
            cardTheme.setOnClickListener { showThemeDialog() }
            cardCurrency.setOnClickListener { showCurrencyDialog() }
            switchNotifications.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(SettingsEvent.UpdateNotifications(isChecked))
            }
            switchBudgetAlerts.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(SettingsEvent.UpdateBudgetAlerts(isChecked))
            }
            switchBiometric.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(SettingsEvent.UpdateBiometric(isChecked))
            }
            switchAutoBackup.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(SettingsEvent.UpdateAutoBackup(isChecked))
            }
            cardAbout.setOnClickListener { showAboutDialog() }
        }
    }
    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUi(state)
                }
            }
        }
    }
    
    private fun updateUi(state: SettingsUiState) {
        binding.apply {
            tvThemeValue.text = when (state.theme) {
                AppTheme.LIGHT -> "Light"
                AppTheme.DARK -> "Dark"
                AppTheme.SYSTEM -> "System Default"
            }
            
            tvCurrencyValue.text = "${state.currencySymbol} ${state.currency}"
            
            switchNotifications.isChecked = state.notificationsEnabled
            switchBudgetAlerts.isChecked = state.budgetAlertsEnabled
            switchBiometric.isChecked = state.biometricEnabled
            switchAutoBackup.isChecked = state.autoBackupEnabled
            
            tvLastBackupValue.text = if (state.lastBackupTime != null) {
                val date = Date(state.lastBackupTime)
                val format = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
                "Last backup: ${format.format(date)}"
            } else {
                "Last backup: Never"
            }
        }
    }
    
    private fun showThemeDialog() {
        val themes = arrayOf("Light", "Dark", "System Default")
        val currentTheme = viewModel.uiState.value.theme
        val checkedItem = when (currentTheme) {
            AppTheme.LIGHT -> 0
            AppTheme.DARK -> 1
            AppTheme.SYSTEM -> 2
        }
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Theme")
            .setSingleChoiceItems(themes, checkedItem) { dialog, which ->
                val selectedTheme = when (which) {
                    0 -> AppTheme.LIGHT
                    1 -> AppTheme.DARK
                    else -> AppTheme.SYSTEM
                }
                viewModel.onEvent(SettingsEvent.UpdateTheme(selectedTheme))
                dialog.dismiss()
            }
            .show()
    }
    
    private fun showCurrencyDialog() {
        val currencies = arrayOf(
            "₹ INR (Indian Rupee)",
            "$ USD (US Dollar)",
            "€ EUR (Euro)",
            "£ GBP (British Pound)",
            "¥ JPY (Japanese Yen)",
            "A$ AUD (Australian Dollar)",
            "C$ CAD (Canadian Dollar)",
            "¥ CNY (Chinese Yuan)"
        )
        val currencyCodes = arrayOf("INR", "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CNY")
        
        val currentCurrency = viewModel.uiState.value.currency
        val checkedItem = currencyCodes.indexOf(currentCurrency)
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Currency")
            .setSingleChoiceItems(currencies, checkedItem) { dialog, which ->
                viewModel.onEvent(SettingsEvent.UpdateCurrency(currencyCodes[which]))
                dialog.dismiss()
            }
            .show()
    }
    
    private fun showAboutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("About Finance Manager")
            .setMessage(
                "Finance Manager\n" +
                "Version 1.0.0\n\n" +
                "A comprehensive personal finance management app.\n\n" +
                "Features:\n" +
                "• Transaction tracking\n" +
                "• Budget management\n" +
                "• Analytics & reports\n" +
                "• Multi-account support\n" +
                "• Secure data encryption"
            )
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

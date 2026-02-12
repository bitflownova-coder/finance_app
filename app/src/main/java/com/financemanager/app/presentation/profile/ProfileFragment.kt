package com.financemanager.app.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

/**
 * Profile screen showing user info and bank accounts
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ProfileViewModel by viewModels()
    
    private val accountAdapter by lazy {
        BankAccountAdapter(
            onEditClick = { account ->
                viewModel.onEvent(ProfileEvent.EditAccountClicked(account))
            },
            onDeleteClick = { account ->
                showDeleteConfirmation(account.accountId, account.accountName)
            }
        )
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeUiState()
    }
    
    private fun setupRecyclerView() {
        binding.rvAccounts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = accountAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnAddAccount.setOnClickListener {
            viewModel.onEvent(ProfileEvent.AddAccountClicked)
        }
        
        binding.btnSettings.setOnClickListener {
            navigateToSettings()
        }
        
        binding.btnGoals.setOnClickListener {
            navigateToGoals()
        }
        
        binding.btnHelp.setOnClickListener {
            navigateToHelp()
        }
        
        binding.btnReports.setOnClickListener {
            navigateToReports()
        }
        
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
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
    
    private fun updateUi(state: ProfileUiState) {
        // Update user info
        binding.tvUserName.text = state.userName
        binding.tvUserEmail.text = state.userPhone
        binding.tvTotalBalance.text = formatCurrency(state.totalBalance)
        
        // Update accounts list
        accountAdapter.submitList(state.accounts)
        binding.tvEmptyAccounts.isVisible = state.accounts.isEmpty() && !state.isLoading
        binding.rvAccounts.isVisible = state.accounts.isNotEmpty()
        
        // Loading state
        binding.progressBar.isVisible = state.isLoading
        
        // Show account dialog
        if (state.isAccountDialogVisible) {
            showAccountDialog(state.selectedAccount)
        }
        
        // Show error
        state.error?.let { error ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Error")
                .setMessage(error)
                .setPositiveButton("OK", null)
                .show()
        }
    }
    
    private fun formatCurrency(amount: Double): String {
        return "₹ ${NumberFormat.getInstance(Locale("en", "IN")).format(amount)}"
    }
    
    private fun showAccountDialog(account: com.financemanager.app.domain.model.BankAccount?) {
        val dialog = AddEditAccountDialog.newInstance(account)
        dialog.onAccountSaved = { savedAccount ->
            viewModel.onEvent(ProfileEvent.SaveAccount(savedAccount))
        }
        dialog.show(childFragmentManager, "AddEditAccountDialog")
    }
    
    private fun showDeleteConfirmation(accountId: Long, accountName: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete '$accountName'?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.onEvent(ProfileEvent.DeleteAccountClicked(accountId))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                viewModel.onEvent(ProfileEvent.Logout)
                requireActivity().finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun navigateToSettings() {
        findNavController().navigate(R.id.action_profile_to_settings)
    }
    
    private fun navigateToGoals() {
        findNavController().navigate(R.id.goalsFragment)
    }
    
    private fun navigateToHelp() {
        findNavController().navigate(R.id.action_profile_to_help)
    }
    
    private fun navigateToReports() {
        findNavController().navigate(R.id.reportsFragment)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

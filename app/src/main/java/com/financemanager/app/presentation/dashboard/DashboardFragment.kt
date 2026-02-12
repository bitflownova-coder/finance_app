package com.financemanager.app.presentation.dashboard

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
import com.financemanager.app.databinding.FragmentDashboardBinding
import com.financemanager.app.presentation.transaction.AddEditTransactionDialog
import com.financemanager.app.presentation.transaction.TransactionEvent
import com.financemanager.app.presentation.transaction.TransactionViewModel
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

/**
 * Dashboard fragment showing financial overview including:
 * - Total balance across all accounts
 * - Monthly income and expenses
 * - Recent transactions (last 5)
 * - Quick add transaction button
 */
@AndroidEntryPoint
class DashboardFragment : Fragment() {
    
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    
    @Inject
    lateinit var sessionManager: SessionManager
    
    private val viewModel: DashboardViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        observeUiState()
    }
    
    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter { transaction ->
            // Navigate to transaction details or edit dialog
            showTransactionDialog(transaction)
        }
        
        binding.rvRecentTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
    
    private fun setupClickListeners() {
        // FAB to add new transaction
        binding.fabAddTransaction.setOnClickListener {
            showTransactionDialog(null)
        }
        
        // Profile icon
        binding.cardProfileIcon.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
        
        // View All transactions
        binding.tvViewAll.setOnClickListener {
            // Navigate to transactions screen
            findNavController().navigate(R.id.transactionFragment)
        }
        
        // Quick Actions
        binding.cardSplitBills.setOnClickListener {
            findNavController().navigate(R.id.splitBillFragment)
        }
        
        binding.cardCalendar.setOnClickListener {
            findNavController().navigate(R.id.calendarFragment)
        }
        
        binding.cardRecurring.setOnClickListener {
            findNavController().navigate(R.id.recurringFragment)
        }
        
        binding.cardGoals.setOnClickListener {
            findNavController().navigate(R.id.goalsFragment)
        }
    }
    
    private fun showTransactionDialog(transaction: com.financemanager.app.domain.model.Transaction?) {
        val dialog = AddEditTransactionDialog.newInstance(transaction)
        dialog.onTransactionSaved = { savedTransaction ->
            transactionViewModel.onEvent(TransactionEvent.SaveTransaction(savedTransaction))
            // Refresh dashboard after saving
            viewModel.onEvent(DashboardEvent.RefreshDashboard)
        }
        dialog.show(childFragmentManager, "AddEditTransactionDialog")
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
    
    private fun updateUi(state: DashboardUiState) {
        binding.apply {
            // Welcome message with user name
            val userName = sessionManager.getUserName() ?: "User"
            tvWelcome.text = "Hi, $userName!"
            
            // Total balance
            tvTotalBalance.text = formatCurrency(state.totalBalance)
            
            // Monthly stats
            tvMonthlyIncome.text = formatCurrency(state.monthlyIncome)
            tvMonthlyExpenses.text = formatCurrency(state.monthlyExpenses)
            
            // Total money in bank (same as total balance across all accounts)
            tvTotalMoneyMoved.text = formatCurrency(state.totalBalance)
            
            // Recent transactions
            if (state.recentTransactions.isEmpty()) {
                rvRecentTransactions.isVisible = false
                tvEmptyTransactions.isVisible = true
            } else {
                rvRecentTransactions.isVisible = true
                tvEmptyTransactions.isVisible = false
                transactionAdapter.submitList(state.recentTransactions)
            }
            
            // Error handling
            state.error?.let { error ->
                // Could show a Snackbar or error message
                // For now, errors are logged in ViewModel
            }
        }
    }
    
    private fun formatCurrency(amount: Double): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        return currencyFormat.format(amount)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.financemanager.app.presentation.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.databinding.FragmentTransactionBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Transaction list screen with advanced search and filtering
 */
@AndroidEntryPoint
class TransactionFragment : Fragment() {
    
    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: TransactionViewModel by viewModels()
    
    private val transactionAdapter by lazy {
        TransactionAdapter { transaction ->
            viewModel.onEvent(TransactionEvent.EditTransactionClicked(transaction))
        }
    }
    
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        setupSearchBar()
        observeUiState()
    }
    
    private fun setupRecyclerView() {
        binding.rvTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddTransaction.setOnClickListener {
            viewModel.onEvent(TransactionEvent.AddTransactionClicked)
        }
        
        binding.btnFilter.setOnClickListener {
            viewModel.onEvent(TransactionEvent.ShowFilterDialog)
        }
    }
    
    private fun setupSearchBar() {
        binding.editSearchQuery.addTextChangedListener { text ->
            viewModel.onEvent(TransactionEvent.SearchQueryChanged(text?.toString() ?: ""))
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
    
    private fun updateUi(state: TransactionUiState) {
        // Update transaction list
        transactionAdapter.submitList(state.transactions)
        binding.tvEmpty.isVisible = state.transactions.isEmpty() && !state.isLoading
        binding.rvTransactions.isVisible = state.transactions.isNotEmpty()
        
        // Loading state
        binding.progressBar.isVisible = state.isLoading
        
        // Update active filter chips
        updateFilterChips(state)
        
        // Show transaction dialog
        if (state.isDialogVisible) {
            showTransactionDialog(state.selectedTransaction)
        }
        
        // Show filter dialog
        if (state.isFilterDialogVisible) {
            showFilterDialog(state)
        }
        
        // Show error
        state.error?.let { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun updateFilterChips(state: TransactionUiState) {
        val filter = state.currentFilter
        val hasFilters = filter.hasFilters()
        
        binding.scrollFilterChips.isVisible = hasFilters
        binding.chipGroupActiveFilters.removeAllViews()
        
        if (!hasFilters) return
        
        // Add chips for active filters
        filter.categories?.let { categories ->
            if (categories.isNotEmpty()) {
                addFilterChip("Categories (${categories.size})")
            }
        }
        
        filter.transactionTypes?.let { types ->
            if (types.isNotEmpty()) {
                addFilterChip(types.joinToString(", ") { it.name })
            }
        }
        
        if (filter.minAmount != null || filter.maxAmount != null) {
            val min = filter.minAmount?.let { "₹$it" } ?: "Any"
            val max = filter.maxAmount?.let { "₹$it" } ?: "Any"
            addFilterChip("Amount: $min - $max")
        }
        
        if (filter.startDate != null || filter.endDate != null) {
            val start = filter.startDate?.let { dateFormat.format(Date(it)) } ?: "Any"
            val end = filter.endDate?.let { dateFormat.format(Date(it)) } ?: "Any"
            addFilterChip("Date: $start to $end")
        }
        
        // Add clear all chip
        if (hasFilters) {
            addClearAllChip()
        }
    }
    
    private fun addFilterChip(text: String) {
        val chip = Chip(requireContext()).apply {
            this.text = text
            isCloseIconVisible = false
            isClickable = false
        }
        binding.chipGroupActiveFilters.addView(chip)
    }
    
    private fun addClearAllChip() {
        val chip = Chip(requireContext()).apply {
            text = "Clear All"
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                viewModel.onEvent(TransactionEvent.ClearFilters)
            }
        }
        binding.chipGroupActiveFilters.addView(chip)
    }
    
    private fun showTransactionDialog(transaction: com.financemanager.app.domain.model.Transaction?) {
        val dialog = AddEditTransactionDialog.newInstance(transaction)
        dialog.onTransactionSaved = { savedTransaction ->
            viewModel.onEvent(TransactionEvent.SaveTransaction(savedTransaction))
        }
        dialog.show(childFragmentManager, "AddEditTransactionDialog")
        
        // Dismiss dialog state immediately to prevent re-showing
        viewModel.onEvent(TransactionEvent.DismissDialog)
    }
    
    private fun showFilterDialog(state: TransactionUiState) {
        val dialog = FilterDialogFragment.newInstance(state.currentFilter)
        dialog.onFilterApplied = { filter ->
            viewModel.onEvent(TransactionEvent.ApplyFilter(filter))
        }
        dialog.show(childFragmentManager, "FilterDialog")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.financemanager.app.presentation.budget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.databinding.FragmentBudgetBinding
import com.financemanager.app.domain.model.Budget
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Budget fragment for managing budgets
 * Displays list of budgets with progress bars and allows CRUD operations
 */
@AndroidEntryPoint
class BudgetFragment : Fragment() {
    
    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: BudgetViewModel by activityViewModels()
    private lateinit var budgetAdapter: BudgetAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeUiState()
    }
    
    private fun setupRecyclerView() {
        budgetAdapter = BudgetAdapter(
            onItemClick = { budget ->
                // Open edit dialog
                val dialog = AddEditBudgetDialog.newInstance(budget)
                dialog.show(childFragmentManager, "EditBudgetDialog")
            },
            onItemLongClick = { budget ->
                // Show delete confirmation
                showDeleteConfirmation(budget)
            }
        )
        
        binding.rvBudgets.apply {
            adapter = budgetAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddBudget.setOnClickListener {
            val dialog = AddEditBudgetDialog()
            dialog.show(childFragmentManager, "AddBudgetDialog")
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
    
    private fun updateUi(state: BudgetUiState) {
        binding.apply {
            // Loading state
            progressBar.isVisible = state.isLoading
            
            // Budgets list
            if (state.budgets.isEmpty() && !state.isLoading) {
                rvBudgets.isVisible = false
                layoutEmptyState.isVisible = true
            } else {
                rvBudgets.isVisible = true
                layoutEmptyState.isVisible = false
                budgetAdapter.submitList(state.budgets)
            }
            
            // Error handling
            state.error?.let { error ->
                Snackbar.make(root, error, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun showDeleteConfirmation(budget: Budget) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Budget")
            .setMessage("Are you sure you want to delete \"${budget.budgetName}\"?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.onEvent(BudgetEvent.DeleteBudget(budget))
                Snackbar.make(binding.root, "Budget deleted", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

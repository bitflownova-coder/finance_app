package com.financemanager.app.presentation.splitbill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.databinding.FragmentSplitBillBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class SplitBillFragment : Fragment() {
    
    private var _binding: FragmentSplitBillBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: SplitBillViewModel by viewModels()
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    
    private lateinit var adapter: SplitBillAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplitBillBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        observeUiState()
    }
    
    private fun setupRecyclerView() {
        adapter = SplitBillAdapter(
            onItemClick = { splitBill ->
                viewModel.onEvent(SplitBillEvent.SelectBill(splitBill))
            },
            onSendReminder = { splitBill ->
                // Show reminder options for this bill
                Snackbar.make(
                    binding.root,
                    "Reminder feature coming soon!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        )
        
        binding.recyclerViewSplitBills.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SplitBillFragment.adapter
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddSplit.setOnClickListener {
            viewModel.onEvent(SplitBillEvent.ShowAddDialog)
        }
        
        binding.chipAll.setOnClickListener {
            // Show all bills
        }
        
        binding.chipPending.setOnClickListener {
            // Show only pending bills
        }
        
        binding.chipSettled.setOnClickListener {
            // Show only settled bills
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
    
    private fun updateUi(state: SplitBillUiState) {
        // Update statistics
        binding.textTotalUnsettled.text = currencyFormat.format(state.totalUnSettledAmount)
        binding.textPendingCount.text = state.unSettledCount.toString()
        
        // Update list
        adapter.submitList(state.splitBills)
        
        // Show/hide empty state
        if (state.splitBills.isEmpty() && !state.isLoading) {
            binding.recyclerViewSplitBills.visibility = View.GONE
            binding.textEmptyState.visibility = View.VISIBLE
        } else {
            binding.recyclerViewSplitBills.visibility = View.VISIBLE
            binding.textEmptyState.visibility = View.GONE
        }
        
        // Loading state
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        
        // Error messages
        state.errorMessage?.let { message ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
            viewModel.onEvent(SplitBillEvent.DismissError)
        }
        
        // Show dialogs
        if (state.showAddDialog) {
            // Show add split bill dialog
            // TODO: Implement AddSplitBillDialog
        }
        
        if (state.showDetailsDialog && state.selectedBill != null) {
            // Show bill details dialog
            // TODO: Implement SplitBillDetailsDialog
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

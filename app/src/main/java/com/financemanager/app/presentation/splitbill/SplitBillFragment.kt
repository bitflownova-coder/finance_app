package com.financemanager.app.presentation.splitbill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentSplitBillBinding
import com.financemanager.app.domain.model.SplitBill
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            viewModel.onEvent(SplitBillEvent.FilterChanged(BillFilter.ALL))
        }
        
        binding.chipPending.setOnClickListener {
            viewModel.onEvent(SplitBillEvent.FilterChanged(BillFilter.PENDING))
        }
        
        binding.chipSettled.setOnClickListener {
            viewModel.onEvent(SplitBillEvent.FilterChanged(BillFilter.SETTLED))
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
        
        // Update filter chips appearance
        updateFilterChip(binding.chipAll, state.currentFilter == BillFilter.ALL)
        updateFilterChip(binding.chipPending, state.currentFilter == BillFilter.PENDING)
        updateFilterChip(binding.chipSettled, state.currentFilter == BillFilter.SETTLED)
        
        // Update list based on filter
        val filteredBills = when (state.currentFilter) {
            BillFilter.ALL -> state.splitBills
            BillFilter.PENDING -> state.splitBills.filter { !it.isSettled }
            BillFilter.SETTLED -> state.splitBills.filter { it.isSettled }
        }
        adapter.submitList(filteredBills)
        
        // Show/hide empty state
        if (filteredBills.isEmpty() && !state.isLoading) {
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
            val dialog = AddSplitBillDialog()
            dialog.show(childFragmentManager, "AddSplitBillDialog")
        }
        
        if (state.showDetailsDialog && state.selectedBill != null) {
            showBillDetailsDialog(state.selectedBill)
        }
    }
    
    private fun updateFilterChip(textView: TextView, isSelected: Boolean) {
        if (isSelected) {
            textView.setBackgroundResource(R.drawable.bg_vault_tab_selected)
            textView.setTextColor(resources.getColor(R.color.vault_brass, null))
            textView.setTypeface(null, android.graphics.Typeface.BOLD)
        } else {
            textView.setBackgroundResource(R.drawable.bg_vault_tab_unselected)
            textView.setTextColor(resources.getColor(R.color.on_surface_variant, null))
            textView.setTypeface(null, android.graphics.Typeface.NORMAL)
        }
    }
    
    private fun showBillDetailsDialog(bill: SplitBill) {
        val message = buildString {
            append("Description: ${bill.description}\n")
            append("Total Amount: ₹${String.format("%.2f", bill.totalAmount)}\n")
            append("Split Type: ${bill.splitType.name}\n")
            append("Status: ${if (bill.isSettled) "Settled" else "Pending"}\n\n")
            append("Participants (${bill.participants.size}):\n")
            bill.participants.forEachIndexed { index, participant ->
                append("${index + 1}. ${participant.name}\n")
                append("   Amount: ₹${String.format("%.2f", participant.shareAmount)}")
                append(" - ${if (participant.isPaid) "✓ Paid" else "✗ Pending"}\n")
            }
        }
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Split Bill Details")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                viewModel.onEvent(SplitBillEvent.DismissDialog)
            }
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

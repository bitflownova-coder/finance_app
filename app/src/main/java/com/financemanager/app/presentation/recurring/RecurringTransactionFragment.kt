package com.financemanager.app.presentation.recurring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentRecurringBinding
import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.util.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragment for displaying and managing recurring transactions
 */
@AndroidEntryPoint
class RecurringTransactionFragment : Fragment() {

    private var _binding: FragmentRecurringBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecurringTransactionViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var adapter: RecurringTransactionAdapter
    private var showAll = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecurringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupToolbar()
        setupRecyclerView()
        setupClickListeners()
        setupTabs()
        observeUiState()
        
        // Initial load
        loadRecurringTransactions()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_process_now -> {
                    processRecurringNow()
                    true
                }
                R.id.action_refresh -> {
                    loadRecurringTransactions()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = RecurringTransactionAdapter(
            onItemClick = { recurring ->
                showRecurringDetails(recurring)
            },
            onEditClick = { recurring ->
                showAddEditDialog(recurring)
            },
            onDeleteClick = { recurring ->
                confirmDelete(recurring)
            },
            onToggleActive = { recurring, isActive ->
                viewModel.toggleActiveStatus(recurring.recurringId, isActive)
            }
        )
        
        binding.recyclerViewRecurring.apply {
            this.adapter = this@RecurringTransactionFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupClickListeners() {
        binding.fabAdd.setOnClickListener {
            showAddEditDialog(null)
        }
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showAll = tab?.position == 1
                loadRecurringTransactions()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadRecurringTransactions() {
        val userId = sessionManager.getUserId() ?: return
        viewModel.loadRecurringTransactions(userId.toLong(), showAll)
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe UI state
                launch {
                    viewModel.uiState.collect { state ->
                        updateUi(state)
                    }
                }
                
                // Observe events
                launch {
                    viewModel.events.collect { event ->
                        event?.let {
                            handleEvent(it)
                            viewModel.clearEvent()
                        }
                    }
                }
            }
        }
    }

    private fun updateUi(state: RecurringTransactionUiState) {
        // Loading state
        binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
        
        // Empty state
        val isEmpty = state.recurringTransactions.isEmpty() && !state.isLoading
        binding.layoutEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewRecurring.visibility = if (isEmpty) View.GONE else View.VISIBLE
        
        // Update list
        adapter.submitList(state.recurringTransactions)
        
        // Error
        state.error?.let { error ->
            Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry) {
                    loadRecurringTransactions()
                }
                .show()
            viewModel.clearError()
        }
    }

    private fun handleEvent(event: RecurringTransactionEvent) {
        when (event) {
            is RecurringTransactionEvent.TransactionAdded -> {
                Snackbar.make(
                    binding.root,
                    R.string.recurring_transaction_added,
                    Snackbar.LENGTH_SHORT
                ).show()
                loadRecurringTransactions()
            }
            is RecurringTransactionEvent.TransactionUpdated -> {
                Snackbar.make(
                    binding.root,
                    R.string.recurring_transaction_updated,
                    Snackbar.LENGTH_SHORT
                ).show()
                loadRecurringTransactions()
            }
            is RecurringTransactionEvent.TransactionDeleted -> {
                Snackbar.make(
                    binding.root,
                    R.string.recurring_transaction_deleted,
                    Snackbar.LENGTH_SHORT
                ).show()
                loadRecurringTransactions()
            }
            is RecurringTransactionEvent.ActiveStatusToggled -> {
                val message = if (event.isActive) {
                    R.string.recurring_transaction_activated
                } else {
                    R.string.recurring_transaction_paused
                }
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
            is RecurringTransactionEvent.TransactionsProcessed -> {
                val message = getString(
                    R.string.recurring_transactions_processed,
                    event.count
                )
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                loadRecurringTransactions()
            }
            is RecurringTransactionEvent.ShowError -> {
                Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun showAddEditDialog(recurringTransaction: RecurringTransaction?) {
        val dialog = AddEditRecurringDialog.newInstance(recurringTransaction)
        dialog.setOnSaveListener { recurring ->
            if (recurringTransaction == null) {
                // Add new
                viewModel.addRecurringTransaction(recurring)
            } else {
                // Update existing
                viewModel.updateRecurringTransaction(recurring)
            }
        }
        dialog.show(childFragmentManager, "add_edit_recurring")
    }

    private fun showRecurringDetails(recurringTransaction: RecurringTransaction) {
        // Navigate to details screen or show bottom sheet
        // For now, just edit
        showAddEditDialog(recurringTransaction)
    }

    private fun confirmDelete(recurringTransaction: RecurringTransaction) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_recurring_transaction_title)
            .setMessage(R.string.delete_recurring_transaction_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteRecurringTransaction(recurringTransaction)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun processRecurringNow() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.process_recurring_now_title)
            .setMessage(R.string.process_recurring_now_message)
            .setPositiveButton(R.string.process) { _, _ ->
                viewModel.processNow()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

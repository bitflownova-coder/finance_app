package com.financemanager.app.presentation.insights

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
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentInsightsBinding
import com.financemanager.app.util.SessionManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Fragment displaying smart insights
 */
@AndroidEntryPoint
class InsightsFragment : Fragment() {

    private var _binding: FragmentInsightsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InsightViewModel by viewModels()
    
    @Inject
    lateinit var sessionManager: SessionManager
    
    private lateinit var insightAdapter: InsightAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInsightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeUiState()
        loadData()
    }

    private fun setupRecyclerView() {
        insightAdapter = InsightAdapter(
            onInsightClick = { insight ->
                // Handle insight click, navigate to details or action
                when {
                    insight.actionable && insight.actionText != null -> {
                        handleInsightAction(insight)
                    }
                }
            }
        )

        binding.recyclerViewInsights.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = insightAdapter
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            loadData()
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

    private fun updateUi(state: InsightUiState) {
        binding.swipeRefresh.isRefreshing = state.isLoading

        if (state.error != null) {
            showError(state.error)
            viewModel.clearError()
        }

        // Update monthly summary
        state.monthlySummary?.let { summary ->
            binding.textTotalIncome.text = getString(
                R.string.currency_format, 
                summary.totalIncome
            )
            binding.textTotalExpense.text = getString(
                R.string.currency_format, 
                summary.totalExpense
            )
            binding.textNetSavings.text = getString(
                R.string.currency_format, 
                summary.netSavings
            )
            binding.textSavingsRate.text = getString(
                R.string.percentage_format, 
                summary.savingsRate
            )
            
            // Show comparison with last month
            if (summary.comparisonWithLastMonth != null) {
                val comparison = summary.comparisonWithLastMonth!!
                binding.textIncomeChange.text = getString(
                    R.string.percentage_format, 
                    comparison.incomeChange
                )
                binding.textExpenseChange.text = getString(
                    R.string.percentage_format, 
                    comparison.expenseChange
                )
                
                // Set colors based on change
                binding.textIncomeChange.setTextColor(
                    if (comparison.incomeChange >= 0) 
                        requireContext().getColor(R.color.success)
                    else 
                        requireContext().getColor(R.color.error)
                )
                
                binding.textExpenseChange.setTextColor(
                    if (comparison.expenseChange <= 0) 
                        requireContext().getColor(R.color.success)
                    else 
                        requireContext().getColor(R.color.warning)
                )
            }
        }

        // Update predicted expense
        if (state.predictedExpense > 0) {
            binding.textPredictedExpense.text = getString(
                R.string.currency_format, 
                state.predictedExpense
            )
        }

        // Update insights list
        insightAdapter.submitList(state.insights)

        // Show/hide empty state
        if (state.hasData) {
            binding.recyclerViewInsights.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        } else if (!state.isLoading) {
            binding.recyclerViewInsights.visibility = View.GONE
            binding.layoutEmpty.visibility = View.VISIBLE
        }

        // Update insight counts
        binding.textCriticalCount.text = state.criticalInsights.size.toString()
        binding.textHighCount.text = state.highPriorityInsights.size.toString()
        binding.textMediumCount.text = state.mediumPriorityInsights.size.toString()
    }

    private fun loadData() {
        try {
            val userId = sessionManager.getUserId()
            if (userId != null) {
                viewModel.loadAllData(userId)
            } else {
                showError("Please log in to view insights")
            }
        } catch (e: Exception) {
            android.util.Log.e("InsightsFragment", "Error loading data", e)
            showError("Error loading data: ${e.message}")
        }
    }

    private fun handleInsightAction(insight: com.financemanager.app.domain.model.Insight) {
        // Navigate to appropriate screen based on insight type
        when (insight.type) {
            com.financemanager.app.domain.model.InsightType.BUDGET_WARNING -> {
                // Navigate to budget screen
                Snackbar.make(
                    binding.root,
                    "Navigate to budget details",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            com.financemanager.app.domain.model.InsightType.SAVINGS_OPPORTUNITY -> {
                // Navigate to category or budget creation
                Snackbar.make(
                    binding.root,
                    "Navigate to create budget",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            com.financemanager.app.domain.model.InsightType.UNUSUAL_ACTIVITY -> {
                // Navigate to transaction details
                Snackbar.make(
                    binding.root,
                    "Navigate to transaction details",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else -> {
                // Default action
            }
        }
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

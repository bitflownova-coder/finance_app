package com.financemanager.app.presentation.goals

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.databinding.FragmentGoalsBinding
import com.financemanager.app.domain.model.SavingsGoal
import com.financemanager.app.util.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class GoalsFragment : Fragment() {
    
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: GoalsViewModel by viewModels()
    
    @Inject
    lateinit var sessionManager: SessionManager
    
    private lateinit var goalsAdapter: GoalsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        observeUiState()
        
        // Load data
        val userId = sessionManager.getUserId() ?: 0L
        viewModel.onEvent(GoalEvent.LoadGoals(userId))
        viewModel.onEvent(GoalEvent.LoadStatistics(userId))
    }
    
    private fun setupRecyclerView() {
        goalsAdapter = GoalsAdapter(
            onGoalClick = { goal ->
                viewModel.onEvent(GoalEvent.EditGoalClicked(goal))
            },
            onContributeClick = { goal ->
                viewModel.onEvent(GoalEvent.AddContributionClicked(goal))
            },
            onLongClick = { goal ->
                showGoalOptionsDialog(goal)
            }
        )
        
        binding.rvGoals.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = goalsAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAddGoal.setOnClickListener {
            val userId = sessionManager.getUserId() ?: 0L
            viewModel.onEvent(GoalEvent.AddGoalClicked(userId))
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
    
    private fun updateUi(state: GoalsUiState) {
        // Loading state
        binding.progressBar.isVisible = state.isLoading
        
        // Goals list
        goalsAdapter.submitList(state.goals)
        binding.tvEmptyState.isVisible = state.goals.isEmpty() && !state.isLoading
        binding.rvGoals.isVisible = state.goals.isNotEmpty()
        
        // Statistics
        state.statistics?.let { stats ->
            binding.apply {
                tvTotalSaved.text = formatCurrency(stats.totalSavedAmount)
                tvTotalTarget.text = "of ${formatCurrency(stats.totalTargetAmount)}"
                tvOverallProgress.text = "${stats.totalProgress}%"
                progressOverall.progress = stats.totalProgress
                tvActiveGoals.text = "${stats.activeGoalsCount} Active"
                tvCompletedGoals.text = "${stats.completedGoalsCount} Completed"
                
                // Show/hide stats card
                cardStatistics.isVisible = stats.activeGoalsCount > 0 || stats.completedGoalsCount > 0
            }
        }
        
        // Dialogs
        if (state.isAddEditDialogVisible && state.selectedGoal != null) {
            showAddEditGoalDialog(state.selectedGoal)
        }
        
        if (state.isContributionDialogVisible && state.selectedGoal != null) {
            showContributionDialog(state.selectedGoal)
        }
        
        // Error
        state.error?.let { error ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Error")
                .setMessage(error)
                .setPositiveButton("OK") { _, _ ->
                    viewModel.onEvent(GoalEvent.DismissError)
                }
                .show()
        }
    }
    
    private fun showAddEditGoalDialog(goal: SavingsGoal) {
        val dialog = AddEditGoalDialog.newInstance(goal)
        dialog.onGoalSaved = { savedGoal ->
            viewModel.onEvent(GoalEvent.SaveGoal(savedGoal))
        }
        dialog.show(childFragmentManager, "AddEditGoalDialog")
    }
    
    private fun showContributionDialog(goal: SavingsGoal) {
        val dialog = AddContributionDialog.newInstance(goal)
        dialog.onContributionSaved = { amount, note ->
            viewModel.onEvent(GoalEvent.SaveContribution(goal.goalId, amount, note))
        }
        dialog.show(childFragmentManager, "AddContributionDialog")
    }
    
    private fun showGoalOptionsDialog(goal: SavingsGoal) {
        val options = if (goal.isAchieved) {
            arrayOf("Edit", "Archive", "Delete")
        } else {
            arrayOf("Edit", "Contribute", "Archive", "Delete")
        }
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(goal.name)
            .setItems(options) { _, which ->
                when (options[which]) {
                    "Edit" -> viewModel.onEvent(GoalEvent.EditGoalClicked(goal))
                    "Contribute" -> viewModel.onEvent(GoalEvent.AddContributionClicked(goal))
                    "Archive" -> showArchiveConfirmation(goal)
                    "Delete" -> showDeleteConfirmation(goal)
                }
            }
            .show()
    }
    
    private fun showDeleteConfirmation(goal: SavingsGoal) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Goal")
            .setMessage("Are you sure you want to delete '${goal.name}'? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.onEvent(GoalEvent.DeleteGoalClicked(goal))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun showArchiveConfirmation(goal: SavingsGoal) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Archive Goal")
            .setMessage("Archive '${goal.name}'? You can unarchive it later.")
            .setPositiveButton("Archive") { _, _ ->
                viewModel.onEvent(GoalEvent.ArchiveGoal(goal.goalId))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getInstance(Locale("en", "IN"))
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 2
        return "₹${formatter.format(amount)}"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

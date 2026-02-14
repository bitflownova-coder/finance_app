package com.financemanager.app.presentation.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.R
import com.financemanager.app.databinding.ItemGoalBinding
import com.financemanager.app.domain.model.SavingsGoal
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class GoalsAdapter(
    private val onGoalClick: (SavingsGoal) -> Unit,
    private val onContributeClick: (SavingsGoal) -> Unit,
    private val onLongClick: (SavingsGoal) -> Unit
) : ListAdapter<SavingsGoal, GoalsAdapter.GoalViewHolder>(GoalDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding = ItemGoalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GoalViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class GoalViewHolder(
        private val binding: ItemGoalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onGoalClick(getItem(position))
                }
            }
            
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onLongClick(getItem(position))
                    true
                } else false
            }
            
            binding.btnContribute.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onContributeClick(getItem(position))
                }
            }
        }
        
        fun bind(goal: SavingsGoal) {
            binding.apply {
                // Goal name and icon
                tvGoalName.text = goal.name
                tvGoalIcon.text = goal.category.icon
                
                // Description
                tvGoalDescription.text = goal.description
                tvGoalDescription.visibility = if (goal.description.isNotBlank()) View.VISIBLE else View.GONE
                
                // Amounts
                tvCurrentAmount.text = formatCurrency(goal.currentAmount)
                tvTargetAmount.text = "of ${formatCurrency(goal.targetAmount)}"
                tvRemainingAmount.text = "₹${formatCurrency(goal.remainingAmount)} to go"
                
                // Progress
                progressBar.progress = goal.progressPercentage
                tvProgress.text = "${goal.progressPercentage}%"
                
                // Target date
                val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
                tvTargetDate.text = "Target: ${goal.targetDate.format(dateFormatter)}"
                
                // Days remaining
                val daysText = when {
                    goal.isAchieved -> "✅ Goal Achieved!"
                    goal.isOverdue -> "⚠️ Overdue"
                    goal.daysRemaining <= 7 -> "⏰ ${goal.daysRemaining} days left"
                    goal.daysRemaining <= 30 -> "${goal.daysRemaining} days left"
                    else -> "${goal.daysRemaining / 30} months left"
                }
                tvDaysRemaining.text = daysText
                
                // Priority badge
                chipPriority.text = goal.priority.displayName
                chipPriority.setBackgroundColor(
                    ContextCompat.getColor(root.context,
                        when (goal.priority) {
                            com.financemanager.app.domain.model.GoalPriority.HIGH -> R.color.expense_red
                            com.financemanager.app.domain.model.GoalPriority.MEDIUM -> R.color.vault_brass_dim
                            com.financemanager.app.domain.model.GoalPriority.LOW -> R.color.income_green
                        }
                    )
                )
                
                // Category chip
                chipCategory.text = goal.category.displayName
                
                // Contribute button visibility
                btnContribute.visibility = if (goal.isAchieved) View.GONE else View.VISIBLE
                
                // Monthly suggestion
                if (!goal.isAchieved && goal.daysRemaining > 0) {
                    tvMonthlySuggestion.text = "Suggested: ${formatCurrency(goal.suggestedMonthlyContribution)}/month"
                    tvMonthlySuggestion.visibility = View.VISIBLE
                } else {
                    tvMonthlySuggestion.visibility = View.GONE
                }
            }
        }
        
        private fun formatCurrency(amount: Double): String {
            val formatter = NumberFormat.getInstance(Locale("en", "IN"))
            formatter.minimumFractionDigits = 0
            formatter.maximumFractionDigits = 2
            return "₹${formatter.format(amount)}"
        }
    }
}

class GoalDiffCallback : DiffUtil.ItemCallback<SavingsGoal>() {
    override fun areItemsTheSame(oldItem: SavingsGoal, newItem: SavingsGoal): Boolean {
        return oldItem.goalId == newItem.goalId
    }
    
    override fun areContentsTheSame(oldItem: SavingsGoal, newItem: SavingsGoal): Boolean {
        return oldItem == newItem
    }
}

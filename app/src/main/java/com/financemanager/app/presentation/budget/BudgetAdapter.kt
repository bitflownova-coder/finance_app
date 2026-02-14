package com.financemanager.app.presentation.budget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.R
import com.financemanager.app.databinding.ItemBudgetBinding
import com.financemanager.app.domain.model.Budget
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying budgets in a RecyclerView
 */
class BudgetAdapter(
    private val onItemClick: (Budget) -> Unit,
    private val onItemLongClick: (Budget) -> Unit
) : ListAdapter<Budget, BudgetAdapter.BudgetViewHolder>(BudgetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BudgetViewHolder(
        private val binding: ItemBudgetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
            
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemLongClick(getItem(position))
                    true
                } else {
                    false
                }
            }
        }

        fun bind(budget: Budget) {
            binding.apply {
                // Budget name
                tvBudgetName.text = budget.budgetName

                // Category
                val categoryText = budget.category?.displayName ?: "All Categories"
                tvCategory.text = categoryText

                // Period
                val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
                val periodText = "• ${dateFormat.format(Date(budget.periodStart))} - ${dateFormat.format(Date(budget.periodEnd))}"
                tvPeriod.text = periodText

                // Progress bar
                val progress = budget.percentageUsed.toInt().coerceIn(0, 100)
                progressBar.progress = progress
                
                // Set progress bar color based on status
                val progressColor = when {
                    budget.isExceeded -> ContextCompat.getColor(root.context, R.color.expense_red)
                    budget.isNearLimit -> ContextCompat.getColor(root.context, R.color.warning)
                    else -> ContextCompat.getColor(root.context, R.color.success)
                }
                progressBar.setIndicatorColor(progressColor)

                // Spent amount
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                tvSpent.text = currencyFormat.format(budget.spentAmount)

                // Total budget
                tvTotal.text = "of ${currencyFormat.format(budget.totalBudget)}"

                // Status chip
                val statusText = when {
                    budget.isExceeded -> "⚠️ Over budget"
                    budget.isNearLimit -> "⚠️ ${progress}% used"
                    else -> "✓ ${progress}% used"
                }
                chipStatus.text = statusText
                
                chipStatus.setBackgroundColor(
                    when {
                        budget.isExceeded -> ContextCompat.getColor(root.context, R.color.expense_red)
                        budget.isNearLimit -> ContextCompat.getColor(root.context, R.color.vault_brass_dim)
                        else -> ContextCompat.getColor(root.context, R.color.income_green)
                    }
                )
            }
        }
    }

    private class BudgetDiffCallback : DiffUtil.ItemCallback<Budget>() {
        override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem.budgetId == newItem.budgetId
        }

        override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
            return oldItem == newItem
        }
    }
}

package com.financemanager.app.presentation.recurring

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.R
import com.financemanager.app.databinding.ItemRecurringTransactionBinding
import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.model.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying recurring transactions in RecyclerView
 */
class RecurringTransactionAdapter(
    private val onItemClick: (RecurringTransaction) -> Unit,
    private val onEditClick: (RecurringTransaction) -> Unit,
    private val onDeleteClick: (RecurringTransaction) -> Unit,
    private val onToggleActive: (RecurringTransaction, Boolean) -> Unit
) : ListAdapter<RecurringTransaction, RecurringTransactionAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecurringTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemRecurringTransactionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recurringTransaction: RecurringTransaction) {
            binding.apply {
                // Description
                textDescription.text = recurringTransaction.description
                
                // Amount with color based on type
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
                val amountText = currencyFormat.format(recurringTransaction.amount)
                textAmount.text = amountText
                
                val amountColor = if (recurringTransaction.type == TransactionType.CREDIT) {
                    ContextCompat.getColor(root.context, R.color.success)
                } else {
                    ContextCompat.getColor(root.context, R.color.error)
                }
                textAmount.setTextColor(amountColor)
                
                // Category
                textCategory.text = recurringTransaction.category.name.lowercase()
                    .replaceFirstChar { it.uppercase() }
                
                // Frequency
                textFrequency.text = recurringTransaction.frequency.name.lowercase()
                    .replaceFirstChar { it.uppercase() }
                
                // Next occurrence
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                textNextOccurrence.text = "Next: ${dateFormat.format(Date(recurringTransaction.nextOccurrence))}"
                
                // End date (if set)
                if (recurringTransaction.endDate != null) {
                    textEndDate.text = "Ends: ${dateFormat.format(Date(recurringTransaction.endDate))}"
                    textEndDate.visibility = android.view.View.VISIBLE
                } else {
                    textEndDate.visibility = android.view.View.GONE
                }
                
                // Active switch
                switchActive.isChecked = recurringTransaction.isActive
                switchActive.setOnCheckedChangeListener { _, isChecked ->
                    onToggleActive(recurringTransaction, isChecked)
                }
                
                // Status indicator
                val statusColor = if (recurringTransaction.isActive) {
                    ContextCompat.getColor(root.context, R.color.success)
                } else {
                    ContextCompat.getColor(root.context, R.color.on_surface_variant)
                }
                viewStatusIndicator.setBackgroundColor(statusColor)
                
                // Click listeners
                root.setOnClickListener {
                    onItemClick(recurringTransaction)
                }
                
                btnEdit.setOnClickListener {
                    onEditClick(recurringTransaction)
                }
                
                btnDelete.setOnClickListener {
                    onDeleteClick(recurringTransaction)
                }
                
                // Icon based on type
                val iconRes = if (recurringTransaction.type == TransactionType.CREDIT) {
                    R.drawable.ic_income
                } else {
                    R.drawable.ic_expense
                }
                iconType.setImageResource(iconRes)
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<RecurringTransaction>() {
        override fun areItemsTheSame(
            oldItem: RecurringTransaction,
            newItem: RecurringTransaction
        ): Boolean {
            return oldItem.recurringId == newItem.recurringId
        }

        override fun areContentsTheSame(
            oldItem: RecurringTransaction,
            newItem: RecurringTransaction
        ): Boolean {
            return oldItem == newItem
        }
    }
}

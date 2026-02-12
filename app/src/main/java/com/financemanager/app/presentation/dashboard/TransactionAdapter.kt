package com.financemanager.app.presentation.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.R
import com.financemanager.app.databinding.ItemTransactionDashboardBinding
import com.financemanager.app.domain.model.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter for displaying recent transactions on the dashboard.
 * Uses ListAdapter with DiffUtil for efficient updates.
 */
class TransactionAdapter(
    private val onItemClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionDashboardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TransactionViewHolder(
        private val binding: ItemTransactionDashboardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(transaction: Transaction) {
            binding.apply {
                // Set description
                tvDescription.text = transaction.description.ifEmpty { "No description" }

                // Set category
                tvCategory.text = transaction.category.displayName
                
                // Set icon based on category (using emoji as fallback)
                // For now, using default receipt icon - can be customized per category later
                ivTransactionIcon.setImageResource(R.drawable.ic_receipt)

                // Set amount with color
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                val amountText = if (transaction.transactionType == com.financemanager.app.domain.model.TransactionType.DEBIT) {
                    "- ${currencyFormat.format(transaction.amount)}"
                } else {
                    "+ ${currencyFormat.format(transaction.amount)}"
                }
                tvAmount.text = amountText

                // Set amount color based on transaction type
                val amountColor = if (transaction.transactionType == com.financemanager.app.domain.model.TransactionType.DEBIT) {
                    ContextCompat.getColor(root.context, R.color.expense_red)
                } else {
                    ContextCompat.getColor(root.context, R.color.income_green)
                }
                tvAmount.setTextColor(amountColor)
            }
        }

        private fun formatDate(timestamp: Long): String {
            val calendar = Calendar.getInstance()
            val today = calendar.get(Calendar.DAY_OF_YEAR)
            
            calendar.timeInMillis = timestamp
            val transactionDay = calendar.get(Calendar.DAY_OF_YEAR)

            return when {
                transactionDay == today -> {
                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    "Today, ${timeFormat.format(Date(timestamp))}"
                }
                transactionDay == today - 1 -> {
                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    "Yesterday, ${timeFormat.format(Date(timestamp))}"
                }
                else -> {
                    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
                }
            }
        }
    }

    private class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.transactionId == newItem.transactionId
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}

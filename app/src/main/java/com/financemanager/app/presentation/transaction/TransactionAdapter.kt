package com.financemanager.app.presentation.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.databinding.ItemTransactionBinding
import com.financemanager.app.domain.model.Transaction
import com.financemanager.app.domain.model.TransactionType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adapter for displaying transactions in RecyclerView
 */
class TransactionAdapter(
    private val onItemClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.ViewHolder>(TransactionDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(
        private val binding: ItemTransactionBinding,
        private val onItemClick: (Transaction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(transaction: Transaction) {
            binding.apply {
                tvDescription.text = transaction.description
                tvCategory.text = transaction.category.displayName
                tvCategoryIcon.text = transaction.category.icon
                tvDate.text = formatDate(transaction.timestamp)
                
                val formattedAmount = formatCurrency(transaction.amount)
                tvAmount.text = when (transaction.transactionType) {
                    TransactionType.DEBIT -> "- $formattedAmount"
                    TransactionType.CREDIT -> "+ $formattedAmount"
                }
                
                val color = when (transaction.transactionType) {
                    TransactionType.DEBIT -> android.graphics.Color.parseColor("#D32F2F") // Red
                    TransactionType.CREDIT -> android.graphics.Color.parseColor("#388E3C") // Green
                }
                tvAmount.setTextColor(color)
                
                root.setOnClickListener { onItemClick(transaction) }
            }
        }
        
        private fun formatDate(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp
            
            return when {
                diff < 86400000 -> "Today, ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))}"
                diff < 172800000 -> "Yesterday, ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(timestamp))}"
                else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
            }
        }
        
        private fun formatCurrency(amount: Double): String {
            return "₹${NumberFormat.getInstance(Locale("en", "IN")).format(amount)}"
        }
    }
    
    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.transactionId == newItem.transactionId
        }
        
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
}

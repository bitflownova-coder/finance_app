package com.financemanager.app.presentation.splitbill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.R
import com.financemanager.app.databinding.ItemSplitBillBinding
import com.financemanager.app.domain.model.SplitBill
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SplitBillAdapter(
    private val onItemClick: (SplitBill) -> Unit,
    private val onSendReminder: (SplitBill) -> Unit
) : ListAdapter<SplitBill, SplitBillAdapter.ViewHolder>(SplitBillDiffCallback()) {
    
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSplitBillBinding.inflate(
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
        private val binding: ItemSplitBillBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(splitBill: SplitBill) {
            binding.apply {
                textDescription.text = splitBill.description
                textTotalAmount.text = currencyFormat.format(splitBill.totalAmount)
                textDate.text = dateFormat.format(Date(splitBill.createdAt))
                textParticipants.text = "${splitBill.participants.size} participants"
                
                val paidCount = splitBill.participants.count { it.isPaid }
                val unpaidCount = splitBill.participants.size - paidCount
                
                textStatus.text = if (splitBill.isSettled) {
                    "Settled"
                } else {
                    "$unpaidCount pending"
                }
                
                val statusColor = when {
                    splitBill.isSettled -> R.color.success_green
                    unpaidCount > 0 -> R.color.warning_orange
                    else -> R.color.text_secondary
                }
                textStatus.setTextColor(
                    ContextCompat.getColor(root.context, statusColor)
                )
                
                textSplitType.text = splitBill.splitType.name.lowercase()
                    .replaceFirstChar { it.uppercase() }
                
                root.setOnClickListener { onItemClick(splitBill) }
                
                buttonReminder.setOnClickListener {
                    onSendReminder(splitBill)
                }
                
                buttonReminder.isEnabled = !splitBill.isSettled
            }
        }
    }
}

class SplitBillDiffCallback : DiffUtil.ItemCallback<SplitBill>() {
    override fun areItemsTheSame(oldItem: SplitBill, newItem: SplitBill): Boolean {
        return oldItem.splitId == newItem.splitId
    }
    
    override fun areContentsTheSame(oldItem: SplitBill, newItem: SplitBill): Boolean {
        return oldItem == newItem
    }
}

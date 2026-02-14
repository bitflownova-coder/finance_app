package com.financemanager.app.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.databinding.ItemBankAccountBinding
import com.financemanager.app.domain.model.BankAccount
import java.text.NumberFormat
import java.util.Locale

/**
 * Adapter for displaying bank accounts in RecyclerView
 */
class BankAccountAdapter(
    private val onEditClick: (BankAccount) -> Unit,
    private val onDeleteClick: (BankAccount) -> Unit
) : ListAdapter<BankAccount, BankAccountAdapter.ViewHolder>(BankAccountDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBankAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onEditClick, onDeleteClick)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(
        private val binding: ItemBankAccountBinding,
        private val onEditClick: (BankAccount) -> Unit,
        private val onDeleteClick: (BankAccount) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(account: BankAccount) {
            binding.apply {
                tvAccountName.text = account.accountName
                tvBankName.text = account.bankName
                tvAccountNumber.text = maskAccountNumber(account.accountNumber)
                tvAccountBalance.text = formatCurrency(account.balance, account.currency)
                tvPrimaryBadge.isVisible = account.isPrimary
                
                btnEdit.setOnClickListener { onEditClick(account) }
                btnDelete.setOnClickListener { onDeleteClick(account) }
            }
        }
        
        private fun maskAccountNumber(accountNumber: String): String {
            return if (accountNumber.length > 4) {
                "XXXX XXXX ${accountNumber.takeLast(4)}"
            } else {
                accountNumber
            }
        }
        
        private fun formatCurrency(amount: Double, currency: String): String {
            val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
            format.maximumFractionDigits = 2
            return when (currency) {
                "INR" -> "₹ ${NumberFormat.getInstance(Locale("en", "IN")).format(amount)}"
                else -> format.format(amount)
            }
        }
    }
    
    class BankAccountDiffCallback : DiffUtil.ItemCallback<BankAccount>() {
        override fun areItemsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
            return oldItem.accountId == newItem.accountId
        }
        
        override fun areContentsTheSame(oldItem: BankAccount, newItem: BankAccount): Boolean {
            return oldItem == newItem
        }
    }
}

package com.financemanager.app.presentation.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.financemanager.app.R
import com.financemanager.app.databinding.DialogAddEditAccountBinding
import com.financemanager.app.domain.model.AccountType
import com.financemanager.app.domain.model.BankAccount
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Dialog for adding or editing a bank account
 */
class AddEditAccountDialog : DialogFragment() {
    
    private var _binding: DialogAddEditAccountBinding? = null
    private val binding get() = _binding!!
    
    private var account: BankAccount? = null
    var onAccountSaved: ((BankAccount) -> Unit)? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = arguments?.getParcelable(ARG_ACCOUNT)
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddEditAccountBinding.inflate(layoutInflater)
        
        setupAccountTypes()
        populateFields()
        setupClickListeners()
        
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (account == null) "Add Account" else "Edit Account")
            .setView(binding.root)
            .create()
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
    
    private fun setupAccountTypes() {
        val accountTypes = AccountType.values().map { it.name }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            accountTypes
        )
        binding.actAccountType.setAdapter(adapter)
        binding.actAccountType.setText(AccountType.SAVINGS.name, false)
    }
    
    private fun populateFields() {
        account?.let {
            binding.etBankName.setText(it.bankName)
            binding.actAccountType.setText(it.accountType.name, false)
            binding.etBalance.setText(it.balance.toString())
        }
    }
    
    private fun setupClickListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                saveAccount()
            }
        }
    }
    
    private fun validateFields(): Boolean {
        var isValid = true
        
        if (binding.etBankName.text.isNullOrBlank()) {
            binding.tilBankName.error = "Bank name is required"
            isValid = false
        } else {
            binding.tilBankName.error = null
        }
        
        if (binding.etBalance.text.isNullOrBlank()) {
            binding.tilBalance.error = "Amount is required"
            isValid = false
        } else {
            try {
                val amount = binding.etBalance.text.toString().toDouble()
                if (amount < 0) {
                    binding.tilBalance.error = "Amount cannot be negative"
                    isValid = false
                } else {
                    binding.tilBalance.error = null
                }
            } catch (e: Exception) {
                binding.tilBalance.error = "Invalid amount"
                isValid = false
            }
        }
        
        return isValid
    }
    
    private fun saveAccount() {
        val bankName = binding.etBankName.text.toString()
        val accountType = AccountType.fromString(binding.actAccountType.text.toString())
        val balance = binding.etBalance.text.toString().toDoubleOrNull() ?: 0.0
        
        // Auto-generate account name and number
        val accountName = "$bankName ${accountType.name}"
        val accountNumber = System.currentTimeMillis().toString().takeLast(10)
        
        val savedAccount = BankAccount(
            accountId = account?.accountId ?: 0,
            userId = account?.userId ?: 0, // Will be set in ViewModel
            accountName = accountName,
            accountNumber = accountNumber,
            bankName = bankName,
            ifscCode = null,
            accountType = accountType,
            balance = balance,
            isPrimary = false,
            createdAt = account?.createdAt ?: System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        onAccountSaved?.invoke(savedAccount)
        dismiss()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        private const val ARG_ACCOUNT = "account"
        
        fun newInstance(account: BankAccount?): AddEditAccountDialog {
            return AddEditAccountDialog().apply {
                arguments = bundleOf(ARG_ACCOUNT to account)
            }
        }
    }
}

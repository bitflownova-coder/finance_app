package com.financemanager.app.presentation.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.financemanager.app.databinding.DialogAddContributionBinding
import com.financemanager.app.domain.model.SavingsGoal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.util.Locale

class AddContributionDialog : BottomSheetDialogFragment() {
    
    private var _binding: DialogAddContributionBinding? = null
    private val binding get() = _binding!!
    
    private var goal: SavingsGoal? = null
    
    var onContributionSaved: ((Double, String) -> Unit)? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddContributionBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        arguments?.getParcelable<GoalParcelable>(ARG_GOAL)?.let {
            goal = it.toGoal()
            setupViews()
        }
        
        setupClickListeners()
    }
    
    private fun setupViews() {
        goal?.let { g ->
            binding.apply {
                tvGoalName.text = g.name
                tvCurrentAmount.text = formatCurrency(g.currentAmount)
                tvRemainingAmount.text = "₹${formatCurrency(g.remainingAmount)} remaining"
                
                // Quick amount buttons
                val remaining = g.remainingAmount
                val suggested = g.suggestedMonthlyContribution
                
                btnQuick1.text = formatCurrency(suggested)
                btnQuick2.text = formatCurrency(remaining / 4)
                btnQuick3.text = formatCurrency(remaining / 2)
                btnQuick4.text = formatCurrency(remaining)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.btnQuick1.setOnClickListener {
            binding.etAmount.setText(goal?.suggestedMonthlyContribution.toString())
        }
        
        binding.btnQuick2.setOnClickListener {
            goal?.let { binding.etAmount.setText((it.remainingAmount / 4).toString()) }
        }
        
        binding.btnQuick3.setOnClickListener {
            goal?.let { binding.etAmount.setText((it.remainingAmount / 2).toString()) }
        }
        
        binding.btnQuick4.setOnClickListener {
            goal?.let { binding.etAmount.setText(it.remainingAmount.toString()) }
        }
        
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        
        binding.btnSave.setOnClickListener {
            saveContribution()
        }
    }
    
    private fun saveContribution() {
        val amountStr = binding.etAmount.text.toString().trim()
        val note = binding.etNote.text.toString().trim()
        
        if (amountStr.isBlank()) {
            binding.tilAmount.error = "Amount is required"
            return
        }
        
        val amount = amountStr.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            binding.tilAmount.error = "Invalid amount"
            return
        }
        
        onContributionSaved?.invoke(amount, note)
        dismiss()
    }
    
    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getInstance(Locale("en", "IN"))
        formatter.minimumFractionDigits = 0
        formatter.maximumFractionDigits = 0
        return "₹${formatter.format(amount)}"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        private const val ARG_GOAL = "goal"
        
        fun newInstance(goal: SavingsGoal): AddContributionDialog {
            return AddContributionDialog().apply {
                arguments = bundleOf(ARG_GOAL to GoalParcelable.from(goal))
            }
        }
    }
}

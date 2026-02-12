package com.financemanager.app.presentation.budget

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.financemanager.app.R
import com.financemanager.app.databinding.DialogAddEditBudgetBinding
import com.financemanager.app.domain.model.Budget
import com.financemanager.app.domain.model.BudgetPeriodType
import com.financemanager.app.domain.model.TransactionCategory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * Dialog for adding or editing a budget
 */
@AndroidEntryPoint
class AddEditBudgetDialog : DialogFragment() {
    
    private var _binding: DialogAddEditBudgetBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: BudgetViewModel by activityViewModels()
    
    private var editBudget: Budget? = null
    
    companion object {
        private const val ARG_BUDGET = "budget"
        
        fun newInstance(budget: Budget? = null): AddEditBudgetDialog {
            return AddEditBudgetDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_BUDGET, budget)
                }
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBudget = arguments?.getParcelable(ARG_BUDGET)
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddEditBudgetBinding.inflate(layoutInflater)
        
        val title = if (editBudget != null) "Edit Budget" else "Add Budget"
        
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
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
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoryDropdown()
        setupPeriodDropdown()
        setupThresholdSlider()
        setupClickListeners()
        
        editBudget?.let { populateFields(it) }
    }
    
    private fun setupCategoryDropdown() {
        val categories = listOf("All Categories") + TransactionCategory.values().map { it.displayName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.etCategory.setAdapter(adapter)
        binding.etCategory.setText(categories[0], false)
    }
    
    private fun setupPeriodDropdown() {
        val periods = listOf("Monthly", "Yearly")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, periods)
        binding.etPeriod.setAdapter(adapter)
        binding.etPeriod.setText(periods[0], false)
    }
    
    private fun setupThresholdSlider() {
        binding.sliderThreshold.value = 80f
        binding.tvThresholdValue.text = "80%"
        
        binding.sliderThreshold.addOnChangeListener { _, value, _ ->
            binding.tvThresholdValue.text = "${value.toInt()}%"
        }
    }
    
    private fun populateFields(budget: Budget) {
        binding.apply {
            etBudgetName.setText(budget.budgetName)
            etTotalBudget.setText(budget.totalBudget.toString())
            
            val categoryText = budget.category?.displayName ?: "All Categories"
            etCategory.setText(categoryText, false)
            
            val periodText = when (budget.periodType) {
                BudgetPeriodType.MONTHLY -> "Monthly"
                BudgetPeriodType.YEARLY -> "Yearly"
            }
            etPeriod.setText(periodText, false)
            
            val thresholdPercent = (budget.alertThreshold * 100).toFloat()
            sliderThreshold.value = thresholdPercent
            tvThresholdValue.text = "${thresholdPercent.toInt()}%"
        }
    }
    
    private fun setupClickListeners() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        
        binding.btnSave.setOnClickListener {
            saveBudget()
        }
    }
    
    private fun saveBudget() {
        binding.apply {
            val name = etBudgetName.text.toString().trim()
            val amountStr = etTotalBudget.text.toString().trim()
            val categoryText = etCategory.text.toString()
            val periodText = etPeriod.text.toString()
            val threshold = sliderThreshold.value / 100
            
            // Validation
            if (name.isEmpty()) {
                tilBudgetName.error = "Budget name is required"
                return
            }
            
            if (amountStr.isEmpty()) {
                tilTotalBudget.error = "Budget amount is required"
                return
            }
            
            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                tilTotalBudget.error = "Enter a valid amount"
                return
            }
            
            // Clear errors
            tilBudgetName.error = null
            tilTotalBudget.error = null
            
            // Determine category
            val category = if (categoryText == "All Categories") {
                null
            } else {
                TransactionCategory.values().find { it.displayName == categoryText }
            }
            
            // Determine period
            val periodType = when (periodText) {
                "Monthly" -> BudgetPeriodType.MONTHLY
                "Yearly" -> BudgetPeriodType.YEARLY
                else -> BudgetPeriodType.MONTHLY
            }
            
            // Calculate period dates
            val calendar = Calendar.getInstance()
            val periodStart = calendar.timeInMillis
            
            calendar.add(
                if (periodType == BudgetPeriodType.MONTHLY) Calendar.MONTH else Calendar.YEAR,
                1
            )
            val periodEnd = calendar.timeInMillis
            
            // Create budget object
            val budget = if (editBudget != null) {
                editBudget!!.copy(
                    budgetName = name,
                    totalBudget = amount,
                    category = category,
                    periodType = periodType,
                    alertThreshold = threshold.toDouble()
                )
            } else {
                Budget(
                    userId = 0, // Will be set by ViewModel
                    budgetName = name,
                    totalBudget = amount,
                    category = category,
                    periodType = periodType,
                    periodStart = periodStart,
                    periodEnd = periodEnd,
                    alertThreshold = threshold.toDouble()
                )
            }
            
            // Save budget
            if (editBudget != null) {
                viewModel.onEvent(BudgetEvent.UpdateBudget(budget))
            } else {
                viewModel.onEvent(BudgetEvent.AddBudget(budget))
            }
            
            dismiss()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

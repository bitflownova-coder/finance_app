package com.financemanager.app.presentation.goals

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.financemanager.app.R
import com.financemanager.app.databinding.DialogAddEditGoalBinding
import com.financemanager.app.domain.model.GoalCategory
import com.financemanager.app.domain.model.GoalPriority
import com.financemanager.app.domain.model.SavingsGoal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AddEditGoalDialog : DialogFragment() {
    
    private var _binding: DialogAddEditGoalBinding? = null
    private val binding get() = _binding!!
    
    private var goal: SavingsGoal? = null
    private var selectedDate: LocalDate = LocalDate.now().plusMonths(6)
    
    var onGoalSaved: ((SavingsGoal) -> Unit)? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_FinanceManager)
        
        arguments?.getParcelable<GoalParcelable>(ARG_GOAL)?.let {
            goal = it.toGoal()
            selectedDate = it.toGoal().targetDate
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddEditGoalBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupCategoryDropdown()
        setupPriorityDropdown()
        setupClickListeners()
        populateFields()
    }
    
    private fun setupCategoryDropdown() {
        val categories = GoalCategory.values().map { it.displayName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.etCategory.setAdapter(adapter)
    }
    
    private fun setupPriorityDropdown() {
        val priorities = GoalPriority.values().map { it.displayName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, priorities)
        binding.etPriority.setAdapter(adapter)
    }
    
    private fun setupClickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_save -> {
                    saveGoal()
                    true
                }
                else -> false
            }
        }
        
        binding.etTargetDate.setOnClickListener {
            showDatePicker()
        }
    }
    
    private fun populateFields() {
        goal?.let { g ->
            binding.apply {
                toolbar.title = "Edit Goal"
                etName.setText(g.name)
                etDescription.setText(g.description)
                etTargetAmount.setText(g.targetAmount.toString())
                etCategory.setText(g.category.displayName, false)
                etPriority.setText(g.priority.displayName, false)
                updateDateDisplay(g.targetDate)
            }
        } ?: run {
            binding.toolbar.title = "New Goal"
            updateDateDisplay(selectedDate)
        }
    }
    
    private fun showDatePicker() {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth)
        
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                updateDateDisplay(selectedDate)
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }
    
    private fun updateDateDisplay(date: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        binding.etTargetDate.setText(date.format(formatter))
    }
    
    private fun saveGoal() {
        val name = binding.etName.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val targetAmountStr = binding.etTargetAmount.text.toString().trim()
        val categoryStr = binding.etCategory.text.toString()
        val priorityStr = binding.etPriority.text.toString()
        
        // Validation
        if (name.isBlank()) {
            binding.tilName.error = "Name is required"
            return
        }
        
        if (targetAmountStr.isBlank()) {
            binding.tilTargetAmount.error = "Target amount is required"
            return
        }
        
        val targetAmount = targetAmountStr.toDoubleOrNull()
        if (targetAmount == null || targetAmount <= 0) {
            binding.tilTargetAmount.error = "Invalid amount"
            return
        }
        
        if (categoryStr.isBlank()) {
            binding.tilCategory.error = "Category is required"
            return
        }
        
        if (priorityStr.isBlank()) {
            binding.tilPriority.error = "Priority is required"
            return
        }
        
        val category = GoalCategory.values().find { it.displayName == categoryStr }
            ?: GoalCategory.OTHER
        val priority = GoalPriority.values().find { it.displayName == priorityStr }
            ?: GoalPriority.MEDIUM
        
        val updatedGoal = goal?.copy(
            name = name,
            description = description,
            targetAmount = targetAmount,
            targetDate = selectedDate,
            category = category,
            priority = priority
        ) ?: SavingsGoal(
            userId = requireArguments().getLong(ARG_USER_ID),
            name = name,
            description = description,
            targetAmount = targetAmount,
            targetDate = selectedDate,
            category = category,
            priority = priority,
            status = com.financemanager.app.domain.model.GoalStatus.ACTIVE
        )
        
        onGoalSaved?.invoke(updatedGoal)
        dismiss()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        private const val ARG_GOAL = "goal"
        private const val ARG_USER_ID = "user_id"
        
        fun newInstance(goal: SavingsGoal): AddEditGoalDialog {
            return AddEditGoalDialog().apply {
                arguments = bundleOf(
                    ARG_GOAL to GoalParcelable.from(goal),
                    ARG_USER_ID to goal.userId
                )
            }
        }
    }
}

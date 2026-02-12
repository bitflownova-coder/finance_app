package com.financemanager.app.presentation.recurring

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.financemanager.app.R
import com.financemanager.app.databinding.DialogAddEditRecurringBinding
import com.financemanager.app.domain.model.RecurringFrequency
import com.financemanager.app.domain.model.RecurringTransaction
import com.financemanager.app.domain.model.TransactionCategory
import com.financemanager.app.domain.model.TransactionType
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dialog for adding or editing recurring transactions
 */
class AddEditRecurringDialog : DialogFragment() {

    private var _binding: DialogAddEditRecurringBinding? = null
    private val binding get() = _binding!!

    private var recurringTransaction: RecurringTransaction? = null
    private var onSaveListener: ((RecurringTransaction) -> Unit)? = null

    private var startDate: Long = System.currentTimeMillis()
    private var endDate: Long? = null
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        
        arguments?.let {
            recurringTransaction = it.getParcelable(ARG_RECURRING_TRANSACTION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddEditRecurringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupToolbar()
        setupSpinners()
        setupDatePickers()
        setupClickListeners()
        
        // Pre-fill if editing
        recurringTransaction?.let { fillData(it) }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = if (recurringTransaction == null) {
                getString(R.string.add_recurring_transaction)
            } else {
                getString(R.string.edit_recurring_transaction)
            }
            
            setNavigationOnClickListener {
                dismiss()
            }
            
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_save -> {
                        saveRecurringTransaction()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupSpinners() {
        // Type spinner
        val types = TransactionType.values().map { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
        val typeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = typeAdapter
        
        // Category spinner
        val categories = TransactionCategory.values().map { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter
        
        // Frequency spinner
        val frequencies = RecurringFrequency.values().map { it.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
        val frequencyAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, frequencies)
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFrequency.adapter = frequencyAdapter
    }

    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        
        // Start date picker
        binding.textStartDate.setOnClickListener {
            calendar.timeInMillis = startDate
            
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    startDate = calendar.timeInMillis
                    binding.textStartDate.text = dateFormat.format(Date(startDate))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        
        // End date picker
        binding.textEndDate.setOnClickListener {
            calendar.timeInMillis = endDate ?: System.currentTimeMillis()
            
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    endDate = calendar.timeInMillis
                    binding.textEndDate.text = dateFormat.format(Date(endDate!!))
                    binding.checkboxNoEndDate.isChecked = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        
        // Update initial date displays
        binding.textStartDate.text = dateFormat.format(Date(startDate))
        binding.textEndDate.text = getString(R.string.select_date)
    }

    private fun setupClickListeners() {
        // No end date checkbox
        binding.checkboxNoEndDate.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                endDate = null
                binding.textEndDate.text = getString(R.string.no_end_date)
                binding.textEndDate.isEnabled = false
            } else {
                binding.textEndDate.isEnabled = true
                binding.textEndDate.text = getString(R.string.select_date)
            }
        }
    }

    private fun fillData(recurring: RecurringTransaction) {
        binding.apply {
            editAmount.setText(recurring.amount.toString())
            editDescription.setText(recurring.description)
            
            spinnerType.setSelection(
                TransactionType.values().indexOf(recurring.type)
            )
            
            spinnerCategory.setSelection(
                TransactionCategory.values().indexOf(recurring.category)
            )
            
            spinnerFrequency.setSelection(
                RecurringFrequency.values().indexOf(recurring.frequency)
            )
            
            startDate = recurring.startDate
            textStartDate.text = dateFormat.format(Date(startDate))
            
            if (recurring.endDate != null) {
                endDate = recurring.endDate
                textEndDate.text = dateFormat.format(Date(endDate!!))
                checkboxNoEndDate.isChecked = false
            } else {
                endDate = null
                checkboxNoEndDate.isChecked = true
            }
            
            switchActive.isChecked = recurring.isActive
        }
    }

    private fun saveRecurringTransaction() {
        // Validate inputs
        val amount = binding.editAmount.text.toString().toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Snackbar.make(binding.root, R.string.error_invalid_amount, Snackbar.LENGTH_SHORT).show()
            return
        }
        
        val description = binding.editDescription.text.toString().trim()
        if (description.isBlank()) {
            Snackbar.make(binding.root, R.string.error_empty_description, Snackbar.LENGTH_SHORT).show()
            return
        }
        
        if (endDate != null && endDate!! <= startDate) {
            Snackbar.make(binding.root, R.string.error_end_date_before_start, Snackbar.LENGTH_SHORT).show()
            return
        }
        
        // Get selected values
        val type = TransactionType.values()[binding.spinnerType.selectedItemPosition]
        val category = TransactionCategory.values()[binding.spinnerCategory.selectedItemPosition]
        val frequency = RecurringFrequency.values()[binding.spinnerFrequency.selectedItemPosition]
        val isActive = binding.switchActive.isChecked
        
        // Calculate next occurrence if new transaction
        val nextOccurrence = recurringTransaction?.nextOccurrence ?: startDate
        
        // Create recurring transaction object
        val recurring = RecurringTransaction(
            recurringId = recurringTransaction?.recurringId ?: 0L,
            userId = recurringTransaction?.userId ?: 0L, // Will be set by ViewModel
            accountId = recurringTransaction?.accountId ?: 0L, // Will be set by ViewModel
            amount = amount,
            type = type,
            category = category,
            description = description,
            frequency = frequency,
            startDate = startDate,
            endDate = endDate,
            nextOccurrence = nextOccurrence,
            isActive = isActive,
            lastGenerated = recurringTransaction?.lastGenerated,
            createdAt = recurringTransaction?.createdAt ?: System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        onSaveListener?.invoke(recurring)
        dismiss()
    }

    fun setOnSaveListener(listener: (RecurringTransaction) -> Unit) {
        onSaveListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_RECURRING_TRANSACTION = "recurring_transaction"
        
        fun newInstance(recurringTransaction: RecurringTransaction? = null): AddEditRecurringDialog {
            return AddEditRecurringDialog().apply {
                arguments = bundleOf(ARG_RECURRING_TRANSACTION to recurringTransaction)
            }
        }
    }
}

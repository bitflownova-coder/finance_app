package com.financemanager.app.presentation.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.financemanager.app.R
import com.financemanager.app.databinding.DialogTransactionFilterBinding
import com.financemanager.app.domain.model.TransactionCategory
import com.financemanager.app.domain.model.TransactionFilter
import com.financemanager.app.domain.model.TransactionType
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.*

/**
 * Dialog for filtering transactions
 * Allows users to set multiple filter criteria
 */
class FilterDialogFragment : DialogFragment() {
    
    private var _binding: DialogTransactionFilterBinding? = null
    private val binding get() = _binding!!
    
    private var currentFilter: TransactionFilter = TransactionFilter.EMPTY
    private var startDate: Long? = null
    private var endDate: Long? = null
    
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    
    var onFilterApplied: ((TransactionFilter) -> Unit)? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_FinanceManager)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTransactionFilterBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupCategoryChips()
        setupDatePickers()
        setupButtons()
        populateCurrentFilter()
    }
    
    private fun setupCategoryChips() {
        // Add category chips dynamically
        TransactionCategory.values().forEach { category ->
            val chip = Chip(requireContext()).apply {
                text = category.name.replace("_", " ")
                isCheckable = true
            }
            binding.chipGroupCategory.addView(chip)
        }
    }
    
    private fun setupDatePickers() {
        binding.btnStartDate.setOnClickListener {
            showDatePicker { date ->
                startDate = date
                binding.btnStartDate.text = dateFormat.format(Date(date))
            }
        }
        
        binding.btnEndDate.setOnClickListener {
            showDatePicker { date ->
                endDate = date
                binding.btnEndDate.text = dateFormat.format(Date(date))
            }
        }
    }
    
    private fun showDatePicker(onDateSelected: (Long) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth, 0, 0, 0)
                onDateSelected(calendar.timeInMillis)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
    
    private fun setupButtons() {
        binding.btnApply.setOnClickListener {
            applyFilter()
        }
        
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
        
        binding.btnClearFilters.setOnClickListener {
            clearAllFilters()
        }
    }
    
    private fun populateCurrentFilter() {
        // Set search query
        binding.editSearchQuery.setText(currentFilter.searchQuery ?: "")
        
        // Set dates
        currentFilter.startDate?.let {
            startDate = it
            binding.btnStartDate.text = dateFormat.format(Date(it))
        }
        currentFilter.endDate?.let {
            endDate = it
            binding.btnEndDate.text = dateFormat.format(Date(it))
        }
        
        // Set amounts
        currentFilter.minAmount?.let {
            binding.editMinAmount.setText(it.toString())
        }
        currentFilter.maxAmount?.let {
            binding.editMaxAmount.setText(it.toString())
        }
        
        // Set transaction types
        currentFilter.transactionTypes?.let { types ->
            binding.chipIncome.isChecked = TransactionType.CREDIT in types
            binding.chipExpense.isChecked = TransactionType.DEBIT in types
        }
        
        // Set sort
        when (currentFilter.sortBy) {
            TransactionFilter.SortBy.DATE_ASC -> binding.chipSortDateAsc.isChecked = true
            TransactionFilter.SortBy.DATE_DESC -> binding.chipSortDateDesc.isChecked = true
            TransactionFilter.SortBy.AMOUNT_ASC -> binding.chipSortAmountAsc.isChecked = true
            TransactionFilter.SortBy.AMOUNT_DESC -> binding.chipSortAmountDesc.isChecked = true
            TransactionFilter.SortBy.CATEGORY -> binding.chipSortCategory.isChecked = true
        }
    }
    
    private fun applyFilter() {
        val searchQuery = binding.editSearchQuery.text?.toString()?.takeIf { it.isNotBlank() }
        
        val selectedCategories = mutableListOf<TransactionCategory>()
        for (i in 0 until binding.chipGroupCategory.childCount) {
            val chip = binding.chipGroupCategory.getChildAt(i) as? Chip
            if (chip?.isChecked == true) {
                val categoryName = chip.text.toString().replace(" ", "_")
                selectedCategories.add(TransactionCategory.valueOf(categoryName))
            }
        }
        
        val selectedTypes = mutableListOf<TransactionType>()
        if (binding.chipIncome.isChecked) selectedTypes.add(TransactionType.CREDIT)
        if (binding.chipExpense.isChecked) selectedTypes.add(TransactionType.DEBIT)
        
        val minAmount = binding.editMinAmount.text?.toString()?.toDoubleOrNull()
        val maxAmount = binding.editMaxAmount.text?.toString()?.toDoubleOrNull()
        
        val sortBy = when {
            binding.chipSortDateAsc.isChecked -> TransactionFilter.SortBy.DATE_ASC
            binding.chipSortDateDesc.isChecked -> TransactionFilter.SortBy.DATE_DESC
            binding.chipSortAmountAsc.isChecked -> TransactionFilter.SortBy.AMOUNT_ASC
            binding.chipSortAmountDesc.isChecked -> TransactionFilter.SortBy.AMOUNT_DESC
            binding.chipSortCategory.isChecked -> TransactionFilter.SortBy.CATEGORY
            else -> TransactionFilter.SortBy.DATE_DESC
        }
        
        val filter = TransactionFilter(
            searchQuery = searchQuery,
            categories = selectedCategories.takeIf { it.isNotEmpty() },
            transactionTypes = selectedTypes.takeIf { it.isNotEmpty() },
            minAmount = minAmount,
            maxAmount = maxAmount,
            startDate = startDate,
            endDate = endDate,
            sortBy = sortBy
        )
        
        onFilterApplied?.invoke(filter)
        dismiss()
    }
    
    private fun clearAllFilters() {
        binding.editSearchQuery.setText("")
        binding.editMinAmount.setText("")
        binding.editMaxAmount.setText("")
        startDate = null
        endDate = null
        binding.btnStartDate.text = "Start Date"
        binding.btnEndDate.text = "End Date"
        binding.chipIncome.isChecked = false
        binding.chipExpense.isChecked = false
        
        // Clear category chips
        for (i in 0 until binding.chipGroupCategory.childCount) {
            (binding.chipGroupCategory.getChildAt(i) as? Chip)?.isChecked = false
        }
        
        // Reset sort to default
        binding.chipSortDateDesc.isChecked = true
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        fun newInstance(currentFilter: TransactionFilter = TransactionFilter.EMPTY): FilterDialogFragment {
            return FilterDialogFragment().apply {
                this.currentFilter = currentFilter
            }
        }
    }
}

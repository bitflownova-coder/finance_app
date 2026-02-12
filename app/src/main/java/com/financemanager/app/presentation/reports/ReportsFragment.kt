package com.financemanager.app.presentation.reports

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentReportsBinding
import com.financemanager.app.domain.model.DateRange
import com.financemanager.app.domain.model.FileFormat
import com.financemanager.app.domain.model.ReportType
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat

/**
 * Fragment for generating and exporting financial reports
 */
@AndroidEntryPoint
class ReportsFragment : Fragment() {
    
    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: ReportsViewModel by viewModels()
    private val currencyFormat = NumberFormat.getCurrencyInstance()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupReportTypeDropdown()
        setupDateRangeDropdown()
        setupFileFormatRadioGroup()
        setupClickListeners()
        observeUiState()
    }
    
    private fun setupReportTypeDropdown() {
        val reportTypes = ReportType.values().map { it.displayName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, reportTypes)
        binding.etReportType.setAdapter(adapter)
        binding.etReportType.setText(ReportType.TRANSACTION_SUMMARY.displayName, false)
        
        binding.etReportType.setOnItemClickListener { _, _, position, _ ->
            val selectedType = ReportType.values()[position]
            viewModel.onEvent(ReportEvent.SelectReportType(selectedType))
        }
    }
    
    private fun setupDateRangeDropdown() {
        val dateRanges = listOf(
            "This Month" to DateRange.thisMonth(),
            "Last Month" to DateRange.lastMonth(),
            "This Year" to DateRange.thisYear()
        )
        
        val labels = dateRanges.map { it.first }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, labels)
        binding.etDateRange.setAdapter(adapter)
        binding.etDateRange.setText(labels[0], false)
        
        binding.etDateRange.setOnItemClickListener { _, _, position, _ ->
            val selectedRange = dateRanges[position].second
            viewModel.onEvent(ReportEvent.SelectDateRange(selectedRange))
        }
    }
    
    private fun setupFileFormatRadioGroup() {
        binding.rgFileFormat.setOnCheckedChangeListener { _, checkedId ->
            val fileFormat = when (checkedId) {
                R.id.rb_pdf -> FileFormat.PDF
                R.id.rb_csv -> FileFormat.CSV
                else -> FileFormat.PDF
            }
            viewModel.onEvent(ReportEvent.SelectFileFormat(fileFormat))
        }
    }
    
    private fun setupClickListeners() {
        binding.btnGenerateReport.setOnClickListener {
            viewModel.onEvent(ReportEvent.GenerateReport)
        }
        
        binding.btnShareReport.setOnClickListener {
            shareReport()
        }
    }
    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUi(state)
                }
            }
        }
    }
    
    private fun updateUi(state: ReportsUiState) {
        binding.apply {
            // Loading state
            progressBar.isVisible = state.isLoading
            btnGenerateReport.isEnabled = !state.isLoading
            
            // Summary data
            state.summary?.let { summary ->
                tvTotalIncome.text = currencyFormat.format(summary.totalIncome)
                tvTotalExpenses.text = currencyFormat.format(summary.totalExpenses)
                tvNetAmount.text = currencyFormat.format(summary.netAmount)
                
                // Set color based on positive/negative
                tvNetAmount.setTextColor(
                    if (summary.netAmount >= 0) {
                        resources.getColor(R.color.income_green, null)
                    } else {
                        resources.getColor(R.color.expense_red, null)
                    }
                )
                
                cardSummary.isVisible = true
            } ?: run {
                cardSummary.isVisible = false
            }
            
            // Generated file
            btnShareReport.isEnabled = state.generatedFile != null
            
            if (state.generatedFile != null) {
                Snackbar.make(
                    binding.root,
                    "Report generated successfully!",
                    Snackbar.LENGTH_LONG
                ).setAction("Share") {
                    shareReport()
                }.show()
            }
            
            // Error handling
            state.error?.let { error ->
                Snackbar.make(root, error, Snackbar.LENGTH_LONG).show()
            }
        }
    }
    
    private fun shareReport() {
        val state = viewModel.uiState.value
        val file = state.generatedFile ?: return
        
        try {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                file
            )
            
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = state.selectedFileFormat.mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Financial Report - ${state.selectedReportType.displayName}")
                putExtra(Intent.EXTRA_TEXT, "Please find attached financial report.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            startActivity(Intent.createChooser(shareIntent, "Share Report"))
        } catch (e: Exception) {
            Snackbar.make(
                binding.root,
                "Failed to share report: ${e.message}",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

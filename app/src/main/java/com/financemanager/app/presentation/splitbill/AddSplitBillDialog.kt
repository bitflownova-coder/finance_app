package com.financemanager.app.presentation.splitbill

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.financemanager.app.R
import com.financemanager.app.databinding.DialogAddSplitBillBinding
import com.financemanager.app.domain.model.Participant
import com.financemanager.app.domain.model.SplitType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dialog for adding a new split bill with participants
 */
@AndroidEntryPoint
class AddSplitBillDialog : DialogFragment() {
    
    private var _binding: DialogAddSplitBillBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: SplitBillViewModel by activityViewModels()
    
    private val participants = mutableListOf<Participant>()
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddSplitBillBinding.inflate(layoutInflater)
        
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Split Bill")
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
        setupSplitTypeDropdown()
        setupClickListeners()
    }
    
    private fun setupSplitTypeDropdown() {
        val splitTypes = listOf("Equal", "Custom", "Percentage")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, splitTypes)
        binding.etSplitType.setAdapter(adapter)
        binding.etSplitType.setText(splitTypes[0], false)
    }
    
    private fun setupClickListeners() {
        binding.apply {
            btnCancel.setOnClickListener {
                dismiss()
            }
            
            btnAddParticipant.setOnClickListener {
                addParticipant()
            }
            
            btnSave.setOnClickListener {
                saveSplitBill()
            }
        }
    }
    
    private fun addParticipant() {
        binding.apply {
            val name = etParticipantName.text.toString().trim()
            val amountStr = etParticipantAmount.text.toString().trim()
            
            if (name.isEmpty() || amountStr.isEmpty()) {
                return
            }
            
            val amount = amountStr.toDoubleOrNull() ?: return
            val participant = Participant(
                splitId = 0,
                name = name,
                shareAmount = amount,
                phoneNumber = etParticipantPhone.text.toString().trim().ifEmpty { null }
            )
            
            participants.add(participant)
            
            // Clear fields
            etParticipantName.text?.clear()
            etParticipantAmount.text?.clear()
            etParticipantPhone.text?.clear()
            
            updateParticipantsList()
        }
    }
    
    private fun updateParticipantsList() {
        binding.tvParticipantsList.text = participants.joinToString("\n") { p ->
            "${p.name}: ₹${String.format("%.2f", p.shareAmount)}"
        }
    }
    
    private fun saveSplitBill() {
        binding.apply {
            val description = etBillDescription.text.toString().trim()
            val totalStr = etTotalAmount.text.toString().trim()
            val splitTypeText = etSplitType.text.toString()
            
            if (description.isEmpty() || totalStr.isEmpty() || participants.isEmpty()) {
                return
            }
            
            val totalAmount = totalStr.toDoubleOrNull() ?: return
            val splitType = when (splitTypeText) {
                "Custom" -> SplitType.CUSTOM
                "Percentage" -> SplitType.PERCENTAGE
                else -> SplitType.EQUAL
            }
            
            viewModel.onEvent(
                SplitBillEvent.AddSplitBill(
                    transactionId = null, // Not linked to any transaction
                    userId = 0L, // Will be replaced with actual userId in ViewModel
                    totalAmount = totalAmount,
                    description = description,
                    splitType = splitType,
                    participants = participants
                )
            )
            
            dismiss()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

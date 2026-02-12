package com.financemanager.app.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentBankSetupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Step 2 of setup wizard — Bank Name + Initial Amount
 */
@AndroidEntryPoint
class BankSetupFragment : Fragment() {
    
    private var _binding: FragmentBankSetupBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: BankSetupViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBankSetupBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeUiState()
    }
    
    private fun setupClickListeners() {
        binding.btnContinue.setOnClickListener {
            val bankName = binding.etBankName.text.toString()
            val amount = binding.etAmount.text.toString()
            viewModel.setupBank(bankName, amount)
        }
    }
    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is BankSetupUiState.Initial -> showLoading(false)
                        is BankSetupUiState.Loading -> showLoading(true)
                        is BankSetupUiState.Success -> {
                            showLoading(false)
                            findNavController().navigate(R.id.action_bankSetup_to_pinSetup)
                        }
                        is BankSetupUiState.Error -> {
                            showLoading(false)
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                            viewModel.resetState()
                        }
                    }
                }
            }
        }
    }
    
    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.isVisible = isLoading
            btnContinue.isEnabled = !isLoading
            etBankName.isEnabled = !isLoading
            etAmount.isEnabled = !isLoading
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

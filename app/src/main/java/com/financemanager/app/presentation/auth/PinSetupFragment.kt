package com.financemanager.app.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentPinSetupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * Step 3 of setup wizard — Set 4-digit PIN (enter + confirm)
 */
@AndroidEntryPoint
class PinSetupFragment : Fragment() {
    
    private var _binding: FragmentPinSetupBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: PinSetupViewModel by viewModels()
    
    private var firstPin: String? = null
    private var isConfirmPhase = false
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinSetupBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPinInput()
        setupClickListeners()
        observeUiState()
        focusPinInput()
    }
    
    private fun setupPinInput() {
        binding.etPin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0
                updateDots(length)
                binding.tvError.isVisible = false
            }
        })
    }
    
    private fun updateDots(filledCount: Int) {
        val dots = listOf(binding.dot1, binding.dot2, binding.dot3, binding.dot4)
        dots.forEachIndexed { index, dot ->
            val bgRes = if (index < filledCount) {
                R.drawable.bg_vault_pin_dot_filled
            } else {
                R.drawable.bg_vault_pin_dot_empty
            }
            dot.setBackgroundResource(bgRes)
        }
    }
    
    private fun setupClickListeners() {
        binding.btnSetPin.setOnClickListener {
            val pin = binding.etPin.text.toString()
            
            if (pin.length != 4) {
                showError("Enter 4-digit PIN")
                return@setOnClickListener
            }
            
            if (!isConfirmPhase) {
                // Phase 1: Record PIN, switch to confirm
                firstPin = pin
                isConfirmPhase = true
                binding.tvPinLabel.text = "CONFIRM PIN"
                binding.btnSetPin.text = getString(R.string.confirm)
                clearPin()
            } else {
                // Phase 2: Confirm and submit
                viewModel.setupPin(firstPin ?: "", pin)
            }
        }
    }
    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is PinSetupUiState.Initial -> showLoading(false)
                        is PinSetupUiState.Loading -> showLoading(true)
                        is PinSetupUiState.Success -> {
                            showLoading(false)
                            findNavController().navigate(R.id.action_pinSetup_to_dashboard)
                        }
                        is PinSetupUiState.Error -> {
                            showLoading(false)
                            showError(state.message)
                            // Reset to enter phase on mismatch
                            firstPin = null
                            isConfirmPhase = false
                            binding.tvPinLabel.text = "ENTER PIN"
                            binding.btnSetPin.text = getString(R.string.set_pin)
                            clearPin()
                            viewModel.resetState()
                        }
                    }
                }
            }
        }
    }
    
    private fun showError(message: String) {
        binding.tvError.text = message
        binding.tvError.isVisible = true
    }
    
    private fun clearPin() {
        binding.etPin.text?.clear()
        updateDots(0)
        focusPinInput()
    }
    
    private fun focusPinInput() {
        binding.etPin.requestFocus()
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.showSoftInput(binding.etPin, InputMethodManager.SHOW_IMPLICIT)
    }
    
    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.isVisible = isLoading
            btnSetPin.isEnabled = !isLoading
            etPin.isEnabled = !isLoading
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

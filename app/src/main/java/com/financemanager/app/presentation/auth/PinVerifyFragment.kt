package com.financemanager.app.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentPinVerifyBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * PIN verification screen for returning users
 */
@AndroidEntryPoint
class PinVerifyFragment : Fragment() {
    
    private var _binding: FragmentPinVerifyBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: PinVerifyViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPinVerifyBinding.inflate(inflater, container, false)
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
                
                // Auto-submit when 4 digits entered
                if (length == 4) {
                    viewModel.verifyPin(s.toString())
                }
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
        binding.tvForgotPin.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Reset App?")
                .setMessage("This will erase all data and start fresh. Are you sure?")
                .setPositiveButton("Reset") { _, _ ->
                    viewModel.resetApp()
                    findNavController().navigate(R.id.action_pinVerify_to_register)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
    
    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is PinVerifyUiState.Initial -> showLoading(false)
                        is PinVerifyUiState.Loading -> showLoading(true)
                        is PinVerifyUiState.Success -> {
                            showLoading(false)
                            findNavController().navigate(R.id.action_pinVerify_to_dashboard)
                        }
                        is PinVerifyUiState.Error -> {
                            showLoading(false)
                            binding.tvError.text = state.message
                            binding.tvError.isVisible = true
                            clearPin()
                            viewModel.resetState()
                        }
                    }
                }
            }
        }
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
            etPin.isEnabled = !isLoading
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

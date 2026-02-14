package com.financemanager.app.presentation.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentSplashBinding
import com.financemanager.app.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Splash screen displaying full branded image
 */
@AndroidEntryPoint
class SplashFragment : Fragment() {
    
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    
    @Inject
    lateinit var sessionManager: SessionManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Start fade-in animation
        binding.splashImage.alpha = 0f
        binding.splashImage.animate()
            .alpha(1f)
            .setDuration(800)
            .start()
        
        // Check authentication after 3.5 seconds
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3500)
            checkNavigationDestination()
        }
    }
    

    
    private fun checkNavigationDestination() {
        val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val onboardingCompleted = prefs.getBoolean("onboarding_completed", false)
        
        when {
            // First launch - show onboarding
            !onboardingCompleted -> {
                findNavController().navigate(R.id.action_splash_to_onboarding)
            }
            // Setup complete - verify PIN to unlock
            sessionManager.isSetupComplete() -> {
                findNavController().navigate(R.id.action_splash_to_pinVerify)
            }
            // Onboarding done but setup not complete - go to register
            else -> {
                findNavController().navigate(R.id.action_splash_to_register)
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

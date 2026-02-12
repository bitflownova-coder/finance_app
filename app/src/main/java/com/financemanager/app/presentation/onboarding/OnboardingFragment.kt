package com.financemanager.app.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentOnboardingBinding
import com.financemanager.app.domain.model.OnboardingPage
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * Onboarding Fragment - Shows app intro and features
 */
@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupClickListeners()
    }

    private fun setupViewPager() {
        onboardingAdapter = OnboardingAdapter(getOnboardingPages())
        binding.viewPager.adapter = onboardingAdapter
        
        // Link TabLayout with ViewPager
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()
        
        // Update buttons on page change
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtons(position)
            }
        })
        
        updateButtons(0)
    }

    private fun setupClickListeners() {
        binding.btnSkip.setOnClickListener {
            completeOnboarding()
        }
        
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < onboardingAdapter.itemCount - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                completeOnboarding()
            }
        }
    }

    private fun updateButtons(position: Int) {
        val isLastPage = position == onboardingAdapter.itemCount - 1
        
        binding.btnSkip.visibility = if (isLastPage) View.GONE else View.VISIBLE
        binding.btnNext.text = if (isLastPage) "Get Started" else "Next"
    }

    private fun completeOnboarding() {
        // Mark onboarding as completed
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("onboarding_completed", true)
            .apply()
        
        // Navigate to register
        findNavController().navigate(R.id.action_onboarding_to_register)
    }

    private fun getOnboardingPages(): List<OnboardingPage> {
        return listOf(
            OnboardingPage(
                title = "Welcome to Finance Manager",
                description = "Keep track of your money easily. See where it goes, set spending limits, and save more.",
                imageRes = android.R.drawable.ic_dialog_info,
                backgroundColor = ContextCompat.getColor(requireContext(), R.color.background)
            ),
            OnboardingPage(
                title = "See Your Spending",
                description = "Keep all your money activity in one place. Sort by type and know exactly where your money goes.",
                imageRes = android.R.drawable.ic_menu_agenda,
                backgroundColor = ContextCompat.getColor(requireContext(), R.color.surface)
            ),
            OnboardingPage(
                title = "Set Spending Limits",
                description = "Decide how much to spend on different things each month. Get a heads-up when you're spending too much.",
                imageRes = android.R.drawable.ic_menu_save,
                backgroundColor = ContextCompat.getColor(requireContext(), R.color.surface_variant)
            ),
            OnboardingPage(
                title = "Understand Your Money",
                description = "See charts and tips that help you understand your spending and make better choices.",
                imageRes = android.R.drawable.ic_menu_sort_by_size,
                backgroundColor = ContextCompat.getColor(requireContext(), R.color.background)
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

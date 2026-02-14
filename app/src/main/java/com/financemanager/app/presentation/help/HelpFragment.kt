package com.financemanager.app.presentation.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.databinding.FragmentHelpBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Help Fragment - Shows FAQs and user guide
 */
@AndroidEntryPoint
class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var helpAdapter: HelpAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        helpAdapter = HelpAdapter(getHelpItems())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = helpAdapter
            setHasFixedSize(true)
        }
    }

    private fun getHelpItems(): List<HelpItem> {
        return listOf(
            HelpItem(
                "Getting Started",
                listOf(
                    FaqItem(
                        "How do I create an account?",
                        "Tap 'Register' on the login screen, enter your email and password, and follow the prompts."
                    ),
                    FaqItem(
                        "How do I add a bank account?",
                        "Go to Profile tab → tap '+' button → enter account name, type, and initial balance."
                    ),
                    FaqItem(
                        "How do I record a transaction?",
                        "Tap the '+' FAB button on Home screen → select category, amount, and account → Save."
                    )
                )
            ),
            HelpItem(
                "Budgets",
                listOf(
                    FaqItem(
                        "How do I create a budget?",
                        "Go to Budget tab → tap '+' button → set name, amount, category, and period → Save."
                    ),
                    FaqItem(
                        "What happens when I exceed my budget?",
                        "The budget card turns red and shows how much you've overspent. You'll also receive a notification."
                    ),
                    FaqItem(
                        "Can I edit or delete a budget?",
                        "Yes, tap on any budget card to edit details or use the delete option."
                    )
                )
            ),
            HelpItem(
                "Transactions",
                listOf(
                    FaqItem(
                        "How do I edit a transaction?",
                        "Tap on any transaction in the list → modify details → Save changes."
                    ),
                    FaqItem(
                        "How do I search for transactions?",
                        "Use the search bar in Transactions tab or apply filters for date, amount, or category."
                    ),
                    FaqItem(
                        "What are transaction categories?",
                        "Categories help organize spending: Food, Transport, Shopping, Bills, Entertainment, Health, Education, Travel, Salary, Investment, Gifts, and Others."
                    )
                )
            ),
            HelpItem(
                "Analytics",
                listOf(
                    FaqItem(
                        "What charts are available?",
                        "Pie chart for expense breakdown by category, Line chart for 6-month income vs expense trend."
                    ),
                    FaqItem(
                        "What is savings rate?",
                        "Savings rate = (Total Income - Total Expenses) / Total Income × 100%. It shows how much of your income you're saving."
                    ),
                    FaqItem(
                        "How do I view different periods?",
                        "Use the month/year selector at the top of Analytics tab to view data for different time periods."
                    )
                )
            ),
            HelpItem(
                "Settings",
                listOf(
                    FaqItem(
                        "How do I change the theme?",
                        "Go to Profile → Settings → Theme → select Light, Dark, or System default."
                    ),
                    FaqItem(
                        "How do I change currency?",
                        "Go to Profile → Settings → Currency → select from available options (INR, USD, EUR, etc.)."
                    ),
                    FaqItem(
                        "Is my data secure?",
                        "Yes! All data is encrypted with SQLCipher (AES-256). Passwords are hashed with BCrypt. Session auto-expires after 15 minutes of inactivity."
                    )
                )
            ),
            HelpItem(
                "Troubleshooting",
                listOf(
                    FaqItem(
                        "App is running slow",
                        "Try clearing old transaction data, reducing chart periods, or restarting the app."
                    ),
                    FaqItem(
                        "I forgot my password",
                        "Currently, password reset is not available. Please create a new account or contact support."
                    ),
                    FaqItem(
                        "Transactions not showing",
                        "Check if you have the correct account selected and date filters applied. Pull down to refresh."
                    )
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/**
 * Help section with FAQ items
 */
data class HelpItem(
    val category: String,
    val faqs: List<FaqItem>
)

/**
 * Single FAQ item
 */
data class FaqItem(
    val question: String,
    val answer: String,
    var isExpanded: Boolean = false
)

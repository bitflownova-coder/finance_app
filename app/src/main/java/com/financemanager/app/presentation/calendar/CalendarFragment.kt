package com.financemanager.app.presentation.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.financemanager.app.databinding.FragmentCalendarBinding
import com.financemanager.app.presentation.dashboard.TransactionAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by viewModels()
    
    private lateinit var calendarAdapter: CalendarDayAdapter
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListeners()
        observeUiState()
    }

    private fun setupRecyclerView() {
        // Calendar grid - 7 columns for days of week with fixed rows
        calendarAdapter = CalendarDayAdapter { day ->
            viewModel.onEvent(CalendarEvent.SelectDate(day))
        }
        
        binding.rvCalendar.apply {
            val gridLayout = GridLayoutManager(requireContext(), 7)
            // Set span size to create 6 rows of equal height
            gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int = 1
            }
            layoutManager = gridLayout
            adapter = calendarAdapter
            setHasFixedSize(true)
            itemAnimator = null // Disable animations for better performance
        }

        // Transactions list
        transactionAdapter = TransactionAdapter { transaction ->
            // Handle transaction click if needed
        }
        
        binding.rvTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            btnPreviousMonth.setOnClickListener {
                viewModel.onEvent(CalendarEvent.PreviousMonth)
            }

            btnNextMonth.setOnClickListener {
                viewModel.onEvent(CalendarEvent.NextMonth)
            }

            btnToday.setOnClickListener {
                viewModel.onEvent(CalendarEvent.Today)
            }

            btnCloseDetails.setOnClickListener {
                cardSelectedDate.visibility = View.GONE
                calendarAdapter.setSelectedDate(null)
            }
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

    private fun updateUi(state: CalendarUiState) {
        binding.apply {
            // Loading state
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

            // Month/Year header
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            tvMonthYear.text = state.yearMonth.format(formatter)

            // Calendar days - force update
            if (state.calendarDays.isNotEmpty()) {
                calendarAdapter.submitList(null)
                calendarAdapter.submitList(state.calendarDays)
            }

            // Selected date details
            if (state.selectedDate != null) {
                cardSelectedDate.visibility = View.VISIBLE
                calendarAdapter.setSelectedDate(state.selectedDate)
                
                // Format selected date
                val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                tvSelectedDate.text = state.selectedDate.date.format(dateFormatter)
                
                // Show income/expense
                tvSelectedIncome.text = "₹%.2f".format(state.selectedDate.totalIncome)
                tvSelectedExpense.text = "₹%.2f".format(state.selectedDate.totalExpense)
                
                // Show transactions
                if (state.transactionsForSelectedDate.isEmpty()) {
                    rvTransactions.visibility = View.GONE
                    tvNoTransactions.visibility = View.VISIBLE
                } else {
                    rvTransactions.visibility = View.VISIBLE
                    tvNoTransactions.visibility = View.GONE
                    transactionAdapter.submitList(state.transactionsForSelectedDate)
                }
            } else {
                cardSelectedDate.visibility = View.GONE
            }

            // Error handling
            state.error?.let { error ->
                Snackbar.make(root, error, Snackbar.LENGTH_LONG)
                    .setAction("Dismiss") {
                        viewModel.onEvent(CalendarEvent.DismissError)
                    }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

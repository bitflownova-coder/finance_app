package com.financemanager.app.presentation.analytics

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.financemanager.app.R
import com.financemanager.app.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

/**
 * Analytics fragment showing real charts and spending statistics
 */
@AndroidEntryPoint
class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnalyticsViewModel by viewModels()

    private val chartColors by lazy {
        listOf(
            ContextCompat.getColor(requireContext(), R.color.chart_color_1),
            ContextCompat.getColor(requireContext(), R.color.chart_color_2),
            ContextCompat.getColor(requireContext(), R.color.chart_color_3),
            ContextCompat.getColor(requireContext(), R.color.chart_color_4),
            ContextCompat.getColor(requireContext(), R.color.chart_color_5),
            ContextCompat.getColor(requireContext(), R.color.chart_color_6)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharts()
        setupClickListeners()
        observeUiState()
    }

    private fun setupCharts() {
        setupBarChart(binding.chartMonthlyTrend)
        setupPieChart(binding.chartCategory)
    }

    private fun setupBarChart(chart: BarChart) {
        val textColor = ContextCompat.getColor(requireContext(), R.color.on_surface_variant)

        chart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            legend.textColor = textColor
            legend.textSize = 10f
            legend.formSize = 10f
            setTouchEnabled(true)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setDrawValueAboveBar(false)
            setPinchZoom(false)
            setScaleEnabled(false)
            isDoubleTapToZoomEnabled = false
            setFitBars(true)
            animateY(800)
            setNoDataText("No data yet")
            setNoDataTextColor(textColor)
            setExtraOffsets(0f, 0f, 0f, 10f)

            axisLeft.apply {
                this.textColor = textColor
                textSize = 10f
                setDrawGridLines(true)
                gridColor = ContextCompat.getColor(requireContext(), R.color.vault_border)
                gridLineWidth = 0.5f
                axisMinimum = 0f
                setDrawAxisLine(false)
            }
            axisRight.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                this.textColor = textColor
                textSize = 10f
                setDrawGridLines(false)
                setDrawAxisLine(false)
                granularity = 1f
            }
        }
    }

    private fun setupPieChart(chart: PieChart) {
        val textColor = ContextCompat.getColor(requireContext(), R.color.on_surface_variant)
        val brassColor = ContextCompat.getColor(requireContext(), R.color.vault_brass)

        chart.apply {
            description.isEnabled = false
            setUsePercentValues(true)
            setDrawEntryLabels(false)
            isDrawHoleEnabled = true
            holeRadius = 55f
            transparentCircleRadius = 60f
            setHoleColor(Color.TRANSPARENT)
            setTransparentCircleColor(Color.TRANSPARENT)
            setTransparentCircleAlpha(50)
            setCenterTextSize(14f)
            setCenterTextColor(brassColor)
            setCenterTextTypeface(Typeface.create("monospace", Typeface.BOLD))
            animateY(800)
            setNoDataText("No spending data")
            setNoDataTextColor(textColor)

            legend.apply {
                isEnabled = true
                this.textColor = ContextCompat.getColor(requireContext(), R.color.vault_parchment)
                textSize = 11f
                formSize = 10f
                isWordWrapEnabled = true
                horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnReport.setOnClickListener {
            // TODO: Show report generation dialog
        }

        binding.tabWeek.setOnClickListener { selectPeriodTab(PeriodType.WEEK) }
        binding.tabMonth.setOnClickListener { selectPeriodTab(PeriodType.MONTH) }
        binding.tabYear.setOnClickListener { selectPeriodTab(PeriodType.YEAR) }

        binding.tvSeeAllCategories.setOnClickListener {
            // TODO: Navigate to all categories screen
        }
    }

    private fun selectPeriodTab(period: PeriodType) {
        binding.apply {
            tabWeek.setBackgroundResource(
                if (period == PeriodType.WEEK) R.drawable.bg_vault_tab_selected
                else R.drawable.bg_vault_tab_unselected
            )
            tabMonth.setBackgroundResource(
                if (period == PeriodType.MONTH) R.drawable.bg_vault_tab_selected
                else R.drawable.bg_vault_tab_unselected
            )
            tabYear.setBackgroundResource(
                if (period == PeriodType.YEAR) R.drawable.bg_vault_tab_selected
                else R.drawable.bg_vault_tab_unselected
            )

            val brassColor = ContextCompat.getColor(requireContext(), R.color.vault_brass)
            val dimColor = ContextCompat.getColor(requireContext(), R.color.on_surface_variant)

            tabWeek.setTextColor(if (period == PeriodType.WEEK) brassColor else dimColor)
            tabMonth.setTextColor(if (period == PeriodType.MONTH) brassColor else dimColor)
            tabYear.setTextColor(if (period == PeriodType.YEAR) brassColor else dimColor)
        }

        viewModel.onEvent(AnalyticsEvent.LoadAnalytics)
    }

    private enum class PeriodType { WEEK, MONTH, YEAR }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    updateUi(state)
                }
            }
        }
    }

    private fun updateUi(state: AnalyticsUiState) {
        binding.apply {
            progressBar.isVisible = state.isLoading

            // Summary cards
            state.financialSummary?.let { summary ->
                tvTotalIncome.text = formatCurrency(summary.totalIncome)
                tvTotalSpending.text = formatCurrency(summary.totalExpense)
                tvNetSavings.text = formatCurrency(summary.netSavings)

                // Extra stats
                cardExtraStats.isVisible = true
                tvDailyAverage.text = formatCurrency(summary.averageDailyExpense)
                tvTransactionCount.text = summary.transactionCount.toString()

                // Spending change text
                if (summary.totalExpense > 0) {
                    val savingsPercent = String.format("%.1f%%", summary.savingsRate)
                    tvSpendingChange.text = "Savings rate: $savingsPercent"
                } else {
                    tvSpendingChange.text = "Money In vs Money Out"
                }
            } ?: run {
                tvTotalIncome.text = formatCurrency(0.0)
                tvTotalSpending.text = formatCurrency(0.0)
                tvNetSavings.text = formatCurrency(0.0)
                cardExtraStats.isVisible = false
            }

            // Monthly trend bar chart
            renderBarChart(state)

            // Category pie chart
            renderPieChart(state)

            // Top spending list
            renderTopSpending(state)
        }
    }

    private fun renderBarChart(state: AnalyticsUiState) {
        val trendData = state.monthlyTrend
        val chart = binding.chartMonthlyTrend

        if (trendData.isEmpty()) {
            chart.isVisible = false
            binding.tvNoTrendData.isVisible = true
            return
        }

        chart.isVisible = true
        binding.tvNoTrendData.isVisible = false

        val incomeEntries = mutableListOf<BarEntry>()
        val expenseEntries = mutableListOf<BarEntry>()
        val labels = mutableListOf<String>()

        trendData.forEachIndexed { index, data ->
            incomeEntries.add(BarEntry(index.toFloat(), data.income.toFloat()))
            expenseEntries.add(BarEntry(index.toFloat(), data.expense.toFloat()))
            labels.add(data.monthName.uppercase().take(3))
        }

        val incomeColor = ContextCompat.getColor(requireContext(), R.color.income_green)
        val expenseColor = ContextCompat.getColor(requireContext(), R.color.expense_red)
        val textColor = ContextCompat.getColor(requireContext(), R.color.on_surface_variant)

        val incomeDataSet = BarDataSet(incomeEntries, "Money In").apply {
            color = incomeColor
            setDrawValues(false)
        }

        val expenseDataSet = BarDataSet(expenseEntries, "Money Out").apply {
            color = expenseColor
            setDrawValues(false)
        }

        val barData = BarData(incomeDataSet, expenseDataSet).apply {
            barWidth = 0.35f
        }

        chart.apply {
            data = barData
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.labelCount = labels.size

            // Grouped bar settings
            val groupSpace = 0.1f
            val barSpace = 0.05f
            xAxis.axisMinimum = 0f
            xAxis.axisMaximum = barData.getGroupWidth(groupSpace, barSpace) * labels.size
            xAxis.setCenterAxisLabels(true)
            groupBars(0f, groupSpace, barSpace)

            legend.textColor = textColor
            invalidate()
        }
    }

    private fun renderPieChart(state: AnalyticsUiState) {
        val categoryData = state.expenseByCategory
        val chart = binding.chartCategory

        if (categoryData.isEmpty()) {
            chart.isVisible = false
            binding.tvNoCategoryData.isVisible = true
            return
        }

        chart.isVisible = true
        binding.tvNoCategoryData.isVisible = false

        val totalExpense = categoryData.values.sum()
        val entries = mutableListOf<PieEntry>()

        categoryData.entries
            .sortedByDescending { it.value }
            .forEach { (category, amount) ->
                val percentage = ((amount / totalExpense) * 100).toFloat()
                if (percentage >= 1f) {
                    entries.add(PieEntry(percentage, category.displayName))
                }
            }

        if (entries.isEmpty()) {
            chart.isVisible = false
            binding.tvNoCategoryData.isVisible = true
            return
        }

        val dataSet = PieDataSet(entries, "").apply {
            colors = chartColors
            sliceSpace = 2f
            selectionShift = 5f
            setDrawValues(true)
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.vault_parchment)
            valueTextSize = 10f
            valueFormatter = PercentFormatter(chart)
            valueTypeface = Typeface.create("monospace", Typeface.BOLD)
        }

        chart.apply {
            data = PieData(dataSet)
            centerText = formatCurrency(totalExpense)
            invalidate()
        }
    }

    private fun renderTopSpending(state: AnalyticsUiState) {
        val container = binding.layoutTopSpending
        container.removeAllViews()

        val topList = state.topSpending

        if (topList.isEmpty()) {
            binding.tvNoSpendingData.isVisible = true
            return
        }

        binding.tvNoSpendingData.isVisible = false

        val parchmentColor = ContextCompat.getColor(requireContext(), R.color.vault_parchment)
        val dimColor = ContextCompat.getColor(requireContext(), R.color.on_surface_variant)
        val brassDimColor = ContextCompat.getColor(requireContext(), R.color.vault_brass_dim)
        val brassColor = ContextCompat.getColor(requireContext(), R.color.vault_brass)

        topList.forEachIndexed { index, item ->
            val card = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = dpToPx(8)
                }
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_vault_card)
                setPadding(dpToPx(14), dpToPx(14), dpToPx(14), dpToPx(14))
            }

            // Rank circle
            val rankFrame = FrameLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(dpToPx(36), dpToPx(36))
                background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_vault_icon_container)
            }

            val rankText = TextView(requireContext()).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
                text = "${index + 1}"
                setTextColor(brassColor)
                textSize = 14f
                typeface = Typeface.create("monospace", Typeface.BOLD)
            }
            rankFrame.addView(rankText)
            card.addView(rankFrame)

            // Info column
            val infoLayout = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    marginStart = dpToPx(12)
                }
                orientation = LinearLayout.VERTICAL
            }

            val nameText = TextView(requireContext()).apply {
                text = item.categoryName
                setTextColor(parchmentColor)
                textSize = 13f
                typeface = Typeface.create("sans-serif-condensed", Typeface.BOLD)
            }
            infoLayout.addView(nameText)

            val countText = TextView(requireContext()).apply {
                text = "${item.transactionCount} items"
                setTextColor(dimColor)
                textSize = 10f
                typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL)
            }
            infoLayout.addView(countText)
            card.addView(infoLayout)

            // Amount column
            val amountLayout = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
                gravity = android.view.Gravity.END
            }

            val amountText = TextView(requireContext()).apply {
                text = formatCurrency(item.amount)
                setTextColor(parchmentColor)
                textSize = 14f
                typeface = Typeface.create("monospace", Typeface.BOLD)
            }
            amountLayout.addView(amountText)

            val percentText = TextView(requireContext()).apply {
                text = String.format("%.0f%%", item.percentage)
                setTextColor(brassDimColor)
                textSize = 10f
                typeface = Typeface.create("monospace", Typeface.NORMAL)
            }
            amountLayout.addView(percentText)
            card.addView(amountLayout)

            container.addView(card)
        }
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun formatCurrency(amount: Double): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
        return currencyFormat.format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

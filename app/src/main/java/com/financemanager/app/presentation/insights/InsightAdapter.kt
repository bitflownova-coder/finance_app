package com.financemanager.app.presentation.insights

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.R
import com.financemanager.app.databinding.ItemInsightBinding
import com.financemanager.app.domain.model.Insight
import com.financemanager.app.domain.model.InsightPriority
import com.financemanager.app.domain.model.InsightType

/**
 * Adapter for displaying insights in a RecyclerView
 */
class InsightAdapter(
    private val onInsightClick: (Insight) -> Unit
) : ListAdapter<Insight, InsightAdapter.InsightViewHolder>(InsightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsightViewHolder {
        val binding = ItemInsightBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InsightViewHolder(binding, onInsightClick)
    }

    override fun onBindViewHolder(holder: InsightViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class InsightViewHolder(
        private val binding: ItemInsightBinding,
        private val onInsightClick: (Insight) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(insight: Insight) {
            binding.apply {
                textTitle.text = insight.title
                textDescription.text = insight.description

                // Set icon based on type
                imageIcon.setImageResource(getIconForType(insight.type))

                // Set priority indicator color
                val priorityColor = getPriorityColor(insight.priority)
                viewPriorityIndicator.setBackgroundColor(priorityColor)
                
                // Set card background tint based on priority
                cardInsight.setBackgroundColor(
                    getPriorityBackgroundColor(insight.priority)
                )

                // Display amount if present
                if (insight.amount != null) {
                    textAmount.text = root.context.getString(
                        R.string.currency_format,
                        insight.amount
                    )
                    textAmount.visibility = android.view.View.VISIBLE
                } else {
                    textAmount.visibility = android.view.View.GONE
                }

                // Display percentage if present
                if (insight.percentage != null) {
                    textPercentage.text = root.context.getString(
                        R.string.percentage_format,
                        insight.percentage
                    )
                    textPercentage.visibility = android.view.View.VISIBLE
                } else {
                    textPercentage.visibility = android.view.View.GONE
                }

                // Show action button if actionable
                if (insight.actionable && insight.actionText != null) {
                    buttonAction.text = insight.actionText
                    buttonAction.visibility = android.view.View.VISIBLE
                    buttonAction.setOnClickListener { onInsightClick(insight) }
                } else {
                    buttonAction.visibility = android.view.View.GONE
                }

                // Set priority badge
                textPriority.text = when (insight.priority) {
                    InsightPriority.CRITICAL -> "CRITICAL"
                    InsightPriority.HIGH -> "HIGH"
                    InsightPriority.MEDIUM -> "MEDIUM"
                    InsightPriority.LOW -> "LOW"
                }

                root.setOnClickListener { onInsightClick(insight) }
            }
        }

        private fun getIconForType(type: InsightType): Int {
            return when (type) {
                InsightType.SPENDING_TREND -> R.drawable.ic_trending_up
                InsightType.BUDGET_WARNING -> R.drawable.ic_alert
                InsightType.SAVINGS_OPPORTUNITY -> R.drawable.ic_lightbulb
                InsightType.UNUSUAL_ACTIVITY -> R.drawable.ic_alert
                InsightType.CATEGORY_ANALYSIS -> R.drawable.ic_pie_chart
                InsightType.INCOME_TREND -> R.drawable.ic_trending_up
                InsightType.RECURRING_EXPENSE -> R.drawable.ic_repeat
                InsightType.MONTHLY_SUMMARY -> R.drawable.ic_calendar
                InsightType.PREDICTION -> R.drawable.ic_crystal_ball
            }
        }

        private fun getPriorityColor(priority: InsightPriority): Int {
            val context = binding.root.context
            return when (priority) {
                InsightPriority.CRITICAL -> ContextCompat.getColor(context, R.color.error)
                InsightPriority.HIGH -> ContextCompat.getColor(context, R.color.warning)
                InsightPriority.MEDIUM -> ContextCompat.getColor(context, R.color.info)
                InsightPriority.LOW -> ContextCompat.getColor(context, R.color.success)
            }
        }

        private fun getPriorityBackgroundColor(priority: InsightPriority): Int {
            val context = binding.root.context
            return when (priority) {
                InsightPriority.CRITICAL -> ContextCompat.getColor(context, R.color.error_light)
                InsightPriority.HIGH -> ContextCompat.getColor(context, R.color.warning_light)
                InsightPriority.MEDIUM -> ContextCompat.getColor(context, R.color.info_light)
                InsightPriority.LOW -> ContextCompat.getColor(context, R.color.success_light)
            }
        }
    }

    class InsightDiffCallback : DiffUtil.ItemCallback<Insight>() {
        override fun areItemsTheSame(oldItem: Insight, newItem: Insight): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Insight, newItem: Insight): Boolean {
            return oldItem == newItem
        }
    }
}

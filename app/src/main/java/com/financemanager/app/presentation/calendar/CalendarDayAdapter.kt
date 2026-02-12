package com.financemanager.app.presentation.calendar

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.R
import com.financemanager.app.databinding.ItemCalendarDayBinding
import com.financemanager.app.domain.model.CalendarDay

class CalendarDayAdapter(
    private val onDayClick: (CalendarDay) -> Unit
) : ListAdapter<CalendarDay, CalendarDayAdapter.CalendarDayViewHolder>(CalendarDayDiffCallback()) {

    private var selectedDate: CalendarDay? = null

    fun setSelectedDate(day: CalendarDay?) {
        val oldSelected = selectedDate
        selectedDate = day
        
        // Update old and new selected items
        currentList.indexOf(oldSelected).takeIf { it >= 0 }?.let { notifyItemChanged(it) }
        currentList.indexOf(day).takeIf { it >= 0 }?.let { notifyItemChanged(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayViewHolder {
        val binding = ItemCalendarDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CalendarDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarDayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CalendarDayViewHolder(
        private val binding: ItemCalendarDayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(day: CalendarDay) {
            binding.apply {
                // Set day number
                tvDayNumber.text = day.date.dayOfMonth.toString()

                // Set colors and style based on state
                when {
                    day.isToday -> {
                        // Today - brass highlight
                        root.setCardBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.vault_brass_dim)
                        )
                        tvDayNumber.setTextColor(
                            ContextCompat.getColor(root.context, R.color.on_primary)
                        )
                        tvDayNumber.setTypeface(null, Typeface.BOLD)
                    }
                    day == selectedDate -> {
                        // Selected date - warm surface
                        root.setCardBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.surface_variant)
                        )
                        tvDayNumber.setTextColor(
                            ContextCompat.getColor(root.context, R.color.vault_brass)
                        )
                        tvDayNumber.setTypeface(null, Typeface.BOLD)
                    }
                    !day.isCurrentMonth -> {
                        // Other month days - faded
                        root.setCardBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.background)
                        )
                        tvDayNumber.setTextColor(
                            ContextCompat.getColor(root.context, R.color.on_surface_variant)
                        )
                        tvDayNumber.alpha = 0.4f
                    }
                    else -> {
                        // Regular day
                        root.setCardBackgroundColor(
                            ContextCompat.getColor(root.context, R.color.surface)
                        )
                        tvDayNumber.setTextColor(
                            ContextCompat.getColor(root.context, R.color.vault_parchment)
                        )
                        tvDayNumber.alpha = 1f
                        tvDayNumber.setTypeface(null, Typeface.NORMAL)
                    }
                }

                // Show transaction indicators
                if (day.hasTransactions) {
                    indicatorIncome.visibility = if (day.totalIncome > 0) 
                        android.view.View.VISIBLE else android.view.View.GONE
                    indicatorExpense.visibility = if (day.totalExpense > 0) 
                        android.view.View.VISIBLE else android.view.View.GONE
                    
                    tvTransactionCount.visibility = android.view.View.VISIBLE
                    tvTransactionCount.text = day.transactionCount.toString()
                } else {
                    indicatorIncome.visibility = android.view.View.GONE
                    indicatorExpense.visibility = android.view.View.GONE
                    tvTransactionCount.visibility = android.view.View.GONE
                }

                // Click listener
                root.setOnClickListener {
                    if (day.isCurrentMonth) {
                        onDayClick(day)
                    }
                }
            }
        }
    }
}

class CalendarDayDiffCallback : DiffUtil.ItemCallback<CalendarDay>() {
    override fun areItemsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: CalendarDay, newItem: CalendarDay): Boolean {
        return oldItem == newItem
    }
}

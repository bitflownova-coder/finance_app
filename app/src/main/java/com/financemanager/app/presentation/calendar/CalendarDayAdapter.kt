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
        // Ensure proper layout params
        binding.root.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            parent.resources.getDimensionPixelSize(android.R.dimen.app_icon_size) // ~48-56dp
        ).apply {
            height = (parent.height / 6) // Divide by 6 rows
        }
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
                // Force visibility
                root.visibility = android.view.View.VISIBLE
                tvDayNumber.visibility = android.view.View.VISIBLE
                
                // ALWAYS set day number first - this is critical
                val dayNumber = day.date.dayOfMonth.toString()
                tvDayNumber.text = dayNumber
                tvDayNumber.visibility = android.view.View.VISIBLE
                
                // Show/hide today indicator circle
                todayIndicator.visibility = if (day.isToday) android.view.View.VISIBLE else android.view.View.GONE
                
                // Set text colors based on state
                when {
                    day.isToday -> {
                        // Today - white text on blue background
                        tvDayNumber.setTextColor(
                            ContextCompat.getColor(root.context, android.R.color.white)
                        )
                        tvDayNumber.setTypeface(null, Typeface.BOLD)
                    }
                    day == selectedDate -> {
                        // Selected date - highlighted
                        tvDayNumber.setTextColor(
                            ContextCompat.getColor(root.context, R.color.vault_brass)
                        )
                        tvDayNumber.setTypeface(null, Typeface.BOLD)
                        root.alpha = 1f
                    }
                    !day.isCurrentMonth -> {
                        // Other month days - dimmed gray
                        tvDayNumber.setTextColor(
                            ContextCompat.getColor(root.context, R.color.gray)
                        )
                        tvDayNumber.alpha = 0.3f
                        tvDayNumber.setTypeface(null, Typeface.NORMAL)
                    }
                    else -> {
                        // Regular day - white
                        tvDayNumber.setTextColor(
                            ContextCompat.getColor(root.context, android.R.color.white)
                        )
                        tvDayNumber.alpha = 1f
                        tvDayNumber.setTypeface(null, Typeface.NORMAL)
                    }
                }

                // Show transaction indicators
                if (day.hasTransactions && day.isCurrentMonth) {
                    transactionIndicator.visibility = android.view.View.VISIBLE
                    
                    // Show transaction count badge
                    if (day.transactionCount > 0) {
                        tvTransactionCount.visibility = android.view.View.VISIBLE
                        tvTransactionCount.text = day.transactionCount.toString()
                    } else {
                        tvTransactionCount.visibility = android.view.View.GONE
                    }
                } else {
                    transactionIndicator.visibility = android.view.View.GONE
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

package com.financemanager.app.presentation.help

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.financemanager.app.databinding.ItemHelpCategoryBinding

/**
 * Adapter for Help categories with nested FAQs
 */
class HelpAdapter(
    private val helpItems: List<HelpItem>
) : RecyclerView.Adapter<HelpAdapter.HelpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpViewHolder {
        val binding = ItemHelpCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HelpViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        holder.bind(helpItems[position])
    }

    override fun getItemCount(): Int = helpItems.size

    class HelpViewHolder(
        private val binding: ItemHelpCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(helpItem: HelpItem) {
            binding.tvCategory.text = helpItem.category
            
            // Setup nested RecyclerView for FAQs
            val faqAdapter = FaqAdapter(helpItem.faqs)
            binding.recyclerViewFaqs.apply {
                layoutManager = LinearLayoutManager(binding.root.context)
                adapter = faqAdapter
                setHasFixedSize(false)
            }
        }
    }
}

/**
 * Adapter for FAQ items (nested)
 */
class FaqAdapter(
    private val faqs: List<FaqItem>
) : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val binding = com.financemanager.app.databinding.ItemFaqBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.bind(faqs[position])
    }

    override fun getItemCount(): Int = faqs.size

    class FaqViewHolder(
        private val binding: com.financemanager.app.databinding.ItemFaqBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(faqItem: FaqItem) {
            binding.apply {
                tvQuestion.text = faqItem.question
                tvAnswer.text = faqItem.answer
                
                // Toggle expand/collapse
                tvAnswer.visibility = if (faqItem.isExpanded) View.VISIBLE else View.GONE
                
                root.setOnClickListener {
                    faqItem.isExpanded = !faqItem.isExpanded
                    tvAnswer.visibility = if (faqItem.isExpanded) View.VISIBLE else View.GONE
                }
            }
        }
    }
}

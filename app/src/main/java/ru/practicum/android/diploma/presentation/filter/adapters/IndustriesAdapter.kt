package ru.practicum.android.diploma.presentation.filter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryItemBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustriesAdapter(
    private val onIndustryClickListener: (industry: Industry, position: Int) -> Unit

) : ListAdapter<Industry, IndustriesAdapter.IndustryViewHolder>(object : DiffUtil.ItemCallback<Industry>() {

    override fun areItemsTheSame(oldItem: Industry, newItem: Industry): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Industry, newItem: Industry): Boolean {
        return oldItem == newItem
    }
}) {

    inner class IndustryViewHolder(val binding: IndustryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(industry: Industry, onIndustryClickListener: (industry: Industry, position: Int) -> Unit) {

            with(binding) {
                radioButton.text = industry.name
                radioButton.isChecked = industry.isChecked

                radioButton.setOnClickListener {
                    onIndustryClickListener(industry, bindingAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IndustryItemBinding.inflate(inflater, parent, false)
        return IndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onIndustryClickListener)
    }
}
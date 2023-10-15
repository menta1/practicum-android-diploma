package ru.practicum.android.diploma.presentation.filter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.CountryItemBinding
import ru.practicum.android.diploma.domain.models.Region

class RegionsAdapter(
    private val onCountryClickListener: (region: Region) -> Unit

) : ListAdapter<Region, RegionsAdapter.RegionViewHolder>(object : DiffUtil.ItemCallback<Region>() {

    override fun areItemsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Region, newItem: Region): Boolean {
        return oldItem == newItem
    }
}) {

    inner class RegionViewHolder(val binding: CountryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(region: Region, onCountryClickListener: (region: Region) -> Unit) {

            with(binding) {
                filterCountryName.text = region.name
                filterCountryName.setOnClickListener { onCountryClickListener(region) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CountryItemBinding.inflate(inflater, parent, false)
        return RegionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onCountryClickListener)
    }
}
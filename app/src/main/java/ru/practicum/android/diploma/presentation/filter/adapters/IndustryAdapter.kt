package ru.practicum.android.diploma.presentation.filter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryItemBinding
import ru.practicum.android.diploma.domain.models.Industry

class IndustryAdapter(
    private val onIndustryClickListener: (industry: Industry, position: Int) -> Unit
): RecyclerView.Adapter<IndustryAdapter.ItemIndustryViewHolder>() {

    var dataSet: List<Industry> = emptyList()
        set(newList) {
            field = newList
            notifyDataSetChanged()
        }

    private var previousCheckedPosition = -1

    inner class ItemIndustryViewHolder(val binding: IndustryItemBinding) :RecyclerView.ViewHolder(binding.root){

       /* init {
            binding.radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    handleRadioButtonsChecks(bindingAdapterPosition)
                }
            }
        }*/

        fun bind(industry: Industry, onIndustryClickListener: (industry: Industry, position: Int) -> Unit) {

            with(binding) {
                radioButton.text = industry.name
                radioButton.isChecked = industry.isChecked

                 radioButton.setOnClickListener {
                     onIndustryClickListener(industry, bindingAdapterPosition)
                 }
                /*radioButton.setOnCheckedChangeListener { _, isChecked ->
                    if(isChecked) onIndustryClickListener(industry)
                }*/
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemIndustryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = IndustryItemBinding.inflate(inflater, parent, false)
        return ItemIndustryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemIndustryViewHolder, position: Int) {
        val item = dataSet[position]
        holder.bind(item, onIndustryClickListener)
    }

    override fun getItemCount(): Int  = dataSet.size

}
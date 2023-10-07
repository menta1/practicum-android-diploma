package ru.practicum.android.diploma.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemProgressBinding

class VacanciesLoaderStateAdapter : LoadStateAdapter<VacanciesLoaderStateAdapter.ItemViewHolder>() {

    override fun onBindViewHolder(holder: ItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProgressBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    class ItemViewHolder(private val view: ItemProgressBinding) : RecyclerView.ViewHolder(view.root) {

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Loading){
                view.progressBar.visibility = View.VISIBLE
            }else{
                view.progressBar.visibility = View.GONE
            }
        }
    }
}
package ru.practicum.android.diploma.presentation.favourite.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouriteAdapter(
    private val context: Context,
    private val clickListener: ClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: List<Vacancy> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return FavouriteViewHolder(view, context, clickListener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FavouriteViewHolder).binding(data[position])
    }
}

interface ClickListener {
    fun clickOnVacancy(vacancyId: String)
}
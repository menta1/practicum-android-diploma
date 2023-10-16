package ru.practicum.android.diploma.presentation.similar.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

class SimilarAdapter(
    private val context: Context,
    private val clickListener: SimilarClickListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: MutableList<Vacancy> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return SimilarViewHolder(view, context, clickListener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SimilarViewHolder).binding(data[position])
    }
}

interface SimilarClickListener {
    fun clickOnVacancy(vacancyIdFind: String)
}
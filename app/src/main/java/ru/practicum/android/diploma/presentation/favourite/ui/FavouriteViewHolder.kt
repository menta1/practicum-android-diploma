package ru.practicum.android.diploma.presentation.favourite.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

class FavouriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView
    private val vacancyTitle: TextView
    private val vacancyEmployer: TextView
    private val vacancySalary: TextView

    init {
        imageView = itemView.findViewById(R.id.vacancy_image)
        vacancyTitle = itemView.findViewById(R.id.vacancy_item_title)
        vacancyEmployer = itemView.findViewById(R.id.vacancy_item_employer)
        vacancySalary = itemView.findViewById(R.id.vacancy_item_salary)
    }
    fun binding(data: Vacancy) {
        vacancyTitle.text = data.name
        vacancyEmployer.text = data.employer
        vacancySalary.text = data.salaryTo.toString()
    }
}
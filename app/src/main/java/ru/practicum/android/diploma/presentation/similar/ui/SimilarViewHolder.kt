package ru.practicum.android.diploma.presentation.similar.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.utils.getSalaryText

class SimilarViewHolder(
    itemView: View,
    private val context: Context,
    private val clickListener: SimilarClickListener
): RecyclerView.ViewHolder(itemView) {
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
        vacancySalary.text = getSalaryText(data.salaryFrom, data.salaryTo, data.currency, context)

        Glide.with(itemView)
            .load(data.employerLogoUrls)
            .placeholder(R.drawable.logo_not_load)
            .transform(
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(R.dimen.round_radius_search)
                )
            )
            .into(imageView)
        itemView.setOnClickListener {
            clickListener.clickOnVacancy(data.id)
        }
    }
}
package ru.practicum.android.diploma.presentation.similar.ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.Vacancy

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
        vacancySalary.text =
            salaryText(data.salaryFrom, data.salaryTo, data.currency)

        Glide.with(itemView)
            .load(data.employerLogoUrls)
            .placeholder(R.drawable.logo_not_load)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(R.dimen.round_radius_search)
                )
            )
            .into(imageView)
        itemView.setOnClickListener {
            clickListener.clickOnVacancy(data.id)
        }
    }

    private fun salaryText(salaryFrom: Int?, salaryTo: Int?, currency: String?) =
        if (salaryFrom != null && salaryTo != null) {
            context.getString(R.string.salary_from) + " " +
                    salaryFrom + " " + context.getString(R.string.salary_to) + " " + salaryTo + " " + currency
        } else if (salaryFrom != null) {
            context.getString(R.string.salary_from) + " " + salaryFrom + " " + currency
        } else if (salaryTo != null) {
            context.getString(R.string.salary_to) + " " + salaryTo + " " + currency
        } else {
            context.getString(R.string.without_salary)
        }
}
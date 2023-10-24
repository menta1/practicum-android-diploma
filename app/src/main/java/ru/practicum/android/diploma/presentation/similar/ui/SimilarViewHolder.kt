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
import java.text.NumberFormat

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
                RoundedCorners(
                    itemView.resources.getDimensionPixelSize(R.dimen.round_radius_search)
                )
            )
            .into(imageView)
        itemView.setOnClickListener {
            clickListener.clickOnVacancy(data.id)
        }
    }

    private fun salaryText(salaryFrom: Int?, salaryTo: Int?, currencyRaw: String?): String {
        val currency = when (currencyRaw) {
            "AZN" -> "₼"
            "BYR" -> "Br"
            "EUR" -> "€"
            "GEL" -> "₾"
            "KGS" -> "с"
            "KZT" -> "₸"
            "RUR" -> "₽"
            "UAH" -> "₴"
            "USD" -> "$"
            "UZS" -> "Soʻm"
            else -> {
                currencyRaw
            }
        }
        return when {
            salaryFrom == 0 && salaryTo != null && salaryTo != 0 -> {
                context.getString(R.string.salary_to, salaryText(salaryTo), currency)
            }
            salaryFrom != null && salaryTo == null || salaryTo == 0 -> {
                context.getString(
                    R.string.salary_from,
                    salaryText(salaryFrom!!),
                    currency
                )
            }
            salaryFrom != null && salaryTo != null -> {
                context.getString(
                    R.string.salary_from_to,
                    salaryText(salaryFrom),
                    salaryText(salaryTo),
                    currency
                )
            }
            else -> {
                context.getString(R.string.without_salary)
            }
        }
    }

    private fun salaryText(number: Int): String {
        return NumberFormat.getInstance().format(number)
    }
}
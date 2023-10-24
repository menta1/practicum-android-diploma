package ru.practicum.android.diploma.presentation.utils

import android.content.Context
import ru.practicum.android.diploma.R
import java.text.NumberFormat
import java.util.Locale

fun getCurrencySign(currencyCode: String?): String {
    return when (currencyCode) {
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
            currencyCode.toString()
        }
    }
}

fun getSalaryText(salaryFrom: Int?, salaryTo: Int?, currency: String, context: Context): String {
    return when {
        salaryFrom == 0 && salaryTo != null && salaryTo != 0 -> {
            context.getString(R.string.salary_to, salaryFormat(salaryTo), currency)
        }
        salaryFrom != null && salaryTo == null || salaryTo == 0 -> {
            context.getString(
                R.string.salary_from,
                salaryFormat(salaryFrom!!),
                currency
            )
        }
        salaryFrom != null -> {
            context.getString(
                R.string.salary_from_to,
                salaryFormat(salaryFrom),
                salaryFormat(salaryTo),
                currency
            )
        }
        else -> {
            context.getString(R.string.without_salary)
        }
    }
}

fun salaryFormat(number: Int?): String {
    return number?.let {
        if (it > 0) NumberFormat.getNumberInstance(Locale.getDefault()).format(it) else "0"
    } ?: ""
}
package ru.practicum.android.diploma.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemProgressBinding
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import java.text.NumberFormat
import java.util.Locale

class VacancyAdapter(private val listener: Listener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_VACANCY = 1
        const val VIEW_TYPE_LOADING = 0
    }

    var loading = View.GONE
    private var itemList = emptyList<Vacancy>()
    private lateinit var progressBarVisibility: ItemProgressBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_VACANCY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.vacancy_item, parent, false)
                VacancyViewHolder(view)
            }

            VIEW_TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_progress, parent, false)
                LoadingViewHolder(view)
            }

            else -> throw IllegalArgumentException("Не правильный тип")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < itemList.size) VIEW_TYPE_VACANCY else VIEW_TYPE_LOADING
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VacancyViewHolder) {
            holder.bind(itemList[position], listener)
        } else if (holder is LoadingViewHolder) {
            progressBarVisibility = holder.bind()
            progressBarVisibility.root.visibility = loading
        }
    }

    override fun getItemCount(): Int = itemList.size + 1

    fun setData(newList: List<Vacancy>) {
        val diffResult = DiffUtil.calculateDiff(ItemDiffCallback(itemList, newList))
        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun visibilityProgress(visibility: Int) {
        loading = visibility
        if (::progressBarVisibility.isInitialized) {
            progressBarVisibility.root.visibility = visibility
        }
    }

    inner class VacancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = VacancyItemBinding.bind(itemView)
        fun bind(item: Vacancy, listener: Listener) = with(binding) {
            vacancyItemTitle.text = item.name
            vacancyItemEmployer.text = item.employer
            vacancyItemSalary.text = formatSalaryString(
                salaryFrom = item.salaryFrom, salaryTo = item.salaryTo, currency = item.currency
            )
            itemView.setOnClickListener {
                listener.onClick(item)
            }
            Glide.with(vacancyImage).load(item.employerLogoUrls)
                .placeholder(R.drawable.logo_not_load)
                .transform(RoundedCorners(vacancyImage.resources.getDimensionPixelSize(R.dimen.round_radius_search)))
                .into(vacancyImage)
        }

        private fun formatSalaryString(
            salaryFrom: Int?, salaryTo: Int?, currency: String?
        ): String {
            val formattedFrom = formatNumber(salaryFrom)
            val formattedTo = formatNumber(salaryTo)
            val currencySign: String = when (currency) {
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
                    ""
                }
            }

            return when {
                salaryFrom == 0 && salaryTo != null -> {
                    "$formattedTo $currencySign"
                }

                salaryFrom != null && salaryTo == 0 -> {
                    "от $formattedFrom $currencySign"
                }

                salaryFrom != null && salaryTo != null -> {
                    "от $formattedFrom до $formattedTo $currencySign"
                }

                salaryFrom != null && salaryTo == null -> {
                    "от $formattedFrom $currencySign"
                }

                salaryFrom == null && salaryTo != null -> {
                    "$formattedTo $currencySign"
                }

                else -> {
                    "Зарплата не указана"
                }
            }
        }

        private fun formatNumber(number: Int?): String {
            return number?.let {
                if (it > 0) NumberFormat.getNumberInstance(Locale.getDefault()).format(it) else "0"
            } ?: ""
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() = ItemProgressBinding.bind(itemView)
    }

    class ItemDiffCallback(private val oldList: List<Vacancy>, private val newList: List<Vacancy>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    interface Listener {
        fun onClick(item: Vacancy)
    }
}
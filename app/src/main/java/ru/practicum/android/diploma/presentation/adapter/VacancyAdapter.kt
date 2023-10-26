package ru.practicum.android.diploma.presentation.adapter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.utils.getSalaryText

class VacancyAdapter(
    private val context: Context,
    private val listener: Listener
) : RecyclerView.Adapter<VacancyAdapter.Holder>() {

    private var itemList = emptyList<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return Holder(view, context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position], listener)
    }


    override fun getItemCount(): Int = itemList.size

    fun setData(newList: List<Vacancy>) {
        val diffResult = DiffUtil.calculateDiff(ItemDiffCallback(itemList, newList))
        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class Holder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        val binding = VacancyItemBinding.bind(itemView)
        fun bind(item: Vacancy, listener: Listener) = with(binding) {
            vacancyItemTitle.text = item.name
            vacancyItemEmployer.text = item.employer
            vacancyItemSalary.text = getSalaryText(
                salaryFrom = item.salaryFrom,
                salaryTo = item.salaryTo,
                currency = item.currency,
                context = context
            )
            itemView.setOnClickListener {
                listener.onClick(item)
            }
            Glide.with(vacancyImage)
                .load(item.employerLogoUrls)
                .placeholder(R.drawable.logo_not_load)
                .transform(RoundedCorners(vacancyImage.resources.getDimensionPixelSize(R.dimen.round_radius_search)))
                .into(vacancyImage)
        }
    }

    class MarginItemDecorator(private val marginTop: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = marginTop
            } else {
                outRect.top = 0
            }
        }
    }

    class ItemDiffCallback(private val oldList: List<Vacancy>, private val newList: List<Vacancy>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }
        override fun getNewListSize(): Int {
            return newList.size
        }
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
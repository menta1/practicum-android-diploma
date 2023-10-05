package ru.practicum.android.diploma.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyItemBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class VacancyAdapter :
    PagingDataAdapter<Vacancy, VacancyAdapter.VacancyHolder>(VacanciesDiffCallback()) {
    override fun onBindViewHolder(holder: VacancyHolder, position: Int) {
        val vacancy = getItem(position) ?: return
        with(holder.binding) {
            vacancyItemTitle.text = vacancy.name
            vacancyItemEmployer.text = vacancy.employer
            vacancyItemSalary.text = vacancy.salaryFrom.toString()
            Glide.with(vacancyImage)
                .load(vacancy.employerLogoUrls)
                .placeholder(R.drawable.mock_logo)
                .transform(RoundedCorners(vacancyImage.resources.getDimensionPixelSize(R.dimen.round_radius_search)))
                .into(vacancyImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyHolder {
        val binding = VacancyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VacancyHolder(binding)
    }

    class VacancyHolder(val binding: VacancyItemBinding) : RecyclerView.ViewHolder(binding.root)
}

class VacanciesDiffCallback : DiffUtil.ItemCallback<Vacancy>() {
    override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean {
        return oldItem == newItem
    }
}
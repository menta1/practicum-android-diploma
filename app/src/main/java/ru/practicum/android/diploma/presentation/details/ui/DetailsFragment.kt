package ru.practicum.android.diploma.presentation.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentDetailsBinding
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.details.view_model.DetailsViewModel
import javax.inject.Inject


class DetailsFragment : Fragment() {

    @Inject
    lateinit var viewModel: DetailsViewModel

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var vacancyDetail: VacancyDetail


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentDetailsBinding.inflate(layoutInflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vacancyId = arguments?.getSerializable(VACANCY) as String
        setVacancy(vacancyId)
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setVacancy(vacancyId: String) {
        viewModel.getVacancyDetails(vacancyId)

        viewModel.isFavouriteVacancy.observe(viewLifecycleOwner) { isFavourite ->
            if (isFavourite) {
                binding.favouriteButtonOff.visibility = View.GONE
                binding.favouriteButtonOn.visibility = View.VISIBLE
            }
            else {
                binding.favouriteButtonOn.visibility = View.GONE
                binding.favouriteButtonOff.visibility = View.VISIBLE
            }
        }

        viewModel.vacancyDetail.observe(viewLifecycleOwner) { vacancy ->
            vacancyDetail = vacancy
            binding.textNameVacancy.text = vacancy.name
            binding.textCurrency.text = "${vacancy.salaryFrom.toString()} ${vacancy.currency}"

            Glide.with(binding.employerLogo)
                .load(vacancy.employerLogoUrls)
                .placeholder(R.drawable.placeholder)
                .transform(RoundedCorners(R.dimen.round_radius_search))
                .into(binding.employerLogo)
            binding.employerName.text = vacancy.employer
            binding.employerCity.text = vacancy.city

            binding.textExperience.text = vacancy.experience
            binding.textEmploymentSchedule.text = vacancy.schedule
            binding.textDescription.text = vacancy.description
            binding.textKeySkills.text = vacancy.keySkills.toString()

            binding.textContactPersonName.text = vacancy.contactPerson
            binding.textEmail.text = vacancy.email
            binding.textPhoneFirst.text = vacancy.phone.toString()
        }
    }

    fun setListeners() {
        binding.favouriteButtonOff.setOnClickListener {
            viewModel.saveVacancy(vacancyDetail)
            binding.favouriteButtonOff.visibility = View.GONE
            binding.favouriteButtonOn.visibility = View.VISIBLE
        }

        binding.favouriteButtonOn.setOnClickListener {
            viewModel.deleteVacancy(vacancyDetail)
            binding.favouriteButtonOn.visibility = View.GONE
            binding.favouriteButtonOff.visibility = View.VISIBLE
        }
    }

    companion object {
        const val VACANCY = "vacancy"
    }
}
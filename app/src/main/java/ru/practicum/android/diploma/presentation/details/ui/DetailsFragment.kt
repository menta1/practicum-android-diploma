package ru.practicum.android.diploma.presentation.details.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentDetailsBinding
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.details.models.DetailsState
import ru.practicum.android.diploma.presentation.details.view_model.DetailsViewModel
import ru.practicum.android.diploma.presentation.utils.getSalaryText
import javax.inject.Inject

class DetailsFragment : Fragment() {

    @Inject
    lateinit var viewModel: DetailsViewModel

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DetailsFragmentArgs>()
    private val vacancyId by lazy { args.vacancyId }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()

        viewModel.initData(vacancyId)
        viewModel.getDetailsStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                DetailsState.Loading -> {
                    changeLoadingVisibility(true)
                }

                DetailsState.Error -> {
                    changeContentVisibility(false)
                }

                is DetailsState.Content -> {
                    changeContentVisibility(true)
                    updateData(state.data)
                }
            }
        }

        viewModel.getFavouriteStateLiveData().observe(viewLifecycleOwner) { isFavourite ->
            binding.favouriteButton.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    getFavouriteIcon(isFavourite),
                    null
                )
            )
        }

    }

    private fun getFavouriteIcon(isFavourite: Boolean) =
        if (isFavourite) R.drawable.ic_favourite_on else R.drawable.ic_favourite_off

    private fun updateData(data: VacancyDetail) {
        binding.textNameVacancy.text = data.name
        binding.textCurrency.text =
            getSalaryText(data.salaryFrom, data.salaryTo, data.currency, requireContext())

        Glide.with(this)
            .load(data.employerLogoUrls)
            .placeholder(R.drawable.logo_not_load)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.round_radius_search)))
            .into(binding.employerLogo)

        binding.employerName.text = data.employer
        binding.employerCity.text = data.city
        binding.textExperience.text = data.experience
        binding.textEmploymentSchedule.text =
            getString(R.string.employment_schedule, data.employmentType, data.schedule)

        binding.textDescription.text = Html.fromHtml(data.description, Html.FROM_HTML_MODE_COMPACT)
        if (data.keySkills.isNotEmpty()) {
            binding.textKeySkills.visibility = View.VISIBLE
            binding.textKeySkillsTitle.visibility = View.VISIBLE
            binding.textKeySkills.text = skillText(data.keySkills)
        } else {
            binding.textKeySkills.visibility = View.GONE
            binding.textKeySkillsTitle.visibility = View.GONE
        }

        if (!data.contactPerson.isNullOrEmpty()) {
            binding.textContactPersonName.visibility = View.VISIBLE
            binding.textContactPersonNameTitle.visibility = View.VISIBLE
            binding.textContactPersonName.text = data.contactPerson
        } else {
            binding.textContactPersonName.visibility = View.GONE
            binding.textContactPersonNameTitle.visibility = View.GONE
        }

        if (!data.email.isNullOrEmpty()) {
            binding.textEmail.visibility = View.VISIBLE
            binding.textEmailTitle.visibility = View.VISIBLE
            binding.textEmail.text = data.email
        } else {
            binding.textEmail.visibility = View.GONE
            binding.textEmailTitle.visibility = View.GONE
        }

        if (!data.phone.isNullOrEmpty()) {
            binding.textPhone.visibility = View.VISIBLE
            binding.textPhoneTitle.visibility = View.VISIBLE
            binding.textPhone.text = data.phone.first().number

            if (!data.phone.first().comment.isNullOrEmpty()) {
                binding.textMessage.visibility = View.VISIBLE
                binding.textMessageTitle.visibility = View.VISIBLE
                binding.textMessage.text = data.phone.first().comment
            } else {
                binding.textMessage.visibility = View.GONE
                binding.textMessageTitle.visibility = View.GONE
            }

        } else {
            binding.textPhone.visibility = View.GONE
            binding.textPhoneTitle.visibility = View.GONE
            binding.textMessage.visibility = View.GONE
            binding.textMessageTitle.visibility = View.GONE
        }

        if (data.contactPerson.isNullOrEmpty() && data.email.isNullOrEmpty() && data.phone.isNullOrEmpty()) {
            binding.textContactTitle.visibility = View.GONE
        } else {
            binding.textContactTitle.visibility = View.VISIBLE
        }
    }

    private fun skillText(skills: List<String>): String {
        var refactorSkill = ""
        skills.map { refactorSkill += getString(R.string.skill, it) }
        return refactorSkill
    }

    private fun changeContentVisibility(isContent: Boolean) {
        changeLoadingVisibility(false)
        if (isContent) {
            binding.detailsBlock.visibility = View.VISIBLE
            binding.detailsErrorBlock.visibility = View.GONE
        } else {
            binding.detailsBlock.visibility = View.GONE
            binding.detailsErrorBlock.visibility = View.VISIBLE
        }
    }

    private fun changeLoadingVisibility(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarDetails.visibility = View.VISIBLE
            binding.detailsBlock.visibility = View.GONE
        } else {
            binding.progressBarDetails.visibility = View.GONE
            binding.detailsBlock.visibility = View.VISIBLE
        }
    }

    private fun setListeners() {
        binding.similarButton.setOnClickListener {
            findNavController().navigate(
                DetailsFragmentDirections.actionDetailsFragmentToSimilarFragment(vacancyId)
            )
        }

        binding.shareButton.setOnClickListener { viewModel.sharingVacancy() }
        binding.favouriteButton.setOnClickListener {
            viewModel.setFavourite()
        }
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.textEmail.setOnClickListener { viewModel.employerEmail() }
        binding.textPhone.setOnClickListener { viewModel.employerPhone() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package ru.practicum.android.diploma.presentation.filter.ui

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentFilterPlaceBinding
import ru.practicum.android.diploma.presentation.filter.models.FilterScreenState
import ru.practicum.android.diploma.presentation.filter.view_model.FilterViewModel
import javax.inject.Inject


class FilterPlaceFragment : Fragment() {

    @Inject
    lateinit var viewModel: FilterViewModel

    private var _binding: FragmentFilterPlaceBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentFilterPlaceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFilter()
        viewModel.filterScreenState.observe(viewLifecycleOwner){state->
            manageScreenContent(state)
        }


        binding.navigationBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.filterCountry.setOnClickListener {
            val action =
                FilterPlaceFragmentDirections.actionFilterPlaceFragmentToFilterCountryFragment()
            findNavController().navigate(action)
        }

        binding.filterRegion.setOnClickListener {
            val action =
                FilterPlaceFragmentDirections.actionFilterPlaceFragmentToFilterRegionFragment()
            findNavController().navigate(action)
        }

        binding.countryCloseButton.setOnClickListener {
            viewModel.clearCountry()
        }

        binding.cityCloseButton.setOnClickListener {
            viewModel.clearRegion()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun manageScreenContent(state: FilterScreenState) {
        with(binding) {
            when (state) {
                is FilterScreenState.Content -> {

                    state.countryName?.let {countryName->
                        filterCountry.visibility = View.GONE
                        filterCountySelected.visibility = View.VISIBLE
                        countryCloseButton.visibility = View.VISIBLE
                        filterCountryName.text = countryName
                    }

                    state.regionName?.let {regionName->
                        filterRegion.visibility = View.GONE
                        filterRegionSelected.visibility = View.VISIBLE
                        cityCloseButton.visibility = View.VISIBLE
                        filterRegionName.text = regionName
                    }
                }

                FilterScreenState.Default -> {
                    filterCountry.visibility = View.VISIBLE
                    filterRegion.visibility = View.VISIBLE

                    filterCountySelected.visibility = View.GONE
                    filterRegionSelected.visibility = View.GONE
                    countryCloseButton.visibility = View.GONE
                    cityCloseButton.visibility = View.GONE
                }
            }
        }
    }

    companion object{
        const val SHARED_PREFS_EDITING_DELAY = 500L
    }
}
package ru.practicum.android.diploma.presentation.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentFilterPlaceBinding
import ru.practicum.android.diploma.presentation.filter.models.FilterPlaceScreenState
import ru.practicum.android.diploma.presentation.filter.view_model.FilterViewModel
import javax.inject.Inject


class FilterPlaceFragment : Fragment() {

    @Inject
    lateinit var viewModel: FilterViewModel

    private var _binding: FragmentFilterPlaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as App).appComponent.activityComponent().create().inject(this)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.checkCountryInFilterPlaceAndNavigate()
        }
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
        viewModel.filterPlaceScreenState.observe(viewLifecycleOwner) { state ->
            manageScreenContent(state)
        }


        binding.navigationBackButton.setOnClickListener {
            viewModel.checkCountryInFilterPlaceAndNavigate()
        }

        binding.filterCountry.setOnClickListener {
            val action =
                FilterPlaceFragmentDirections.actionFilterPlaceFragmentToFilterCountryFragment()
            findNavController().navigate(action)
        }

        binding.filterCountySelected.setOnClickListener {
            val action =
                FilterPlaceFragmentDirections.actionFilterPlaceFragmentToFilterCountryFragment()
            findNavController().navigate(action)
        }

        binding.filterRegion.setOnClickListener {
            val action =
                FilterPlaceFragmentDirections.actionFilterPlaceFragmentToFilterRegionFragment()
            findNavController().navigate(action)
        }

        binding.filterRegionSelected.setOnClickListener {
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


        binding.selectionButton.setOnClickListener {
            viewModel.checkCountryInFilterPlaceAndNavigate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun manageScreenContent(state: FilterPlaceScreenState) {
        with(binding) {
            when (state) {
                is FilterPlaceScreenState.Content -> {

                    if (state.countryName != null) {
                        filterCountry.visibility = View.GONE
                        filterCountySelected.visibility = View.VISIBLE
                        countryCloseButton.visibility = View.VISIBLE
                        filterCountryName.text = state.countryName
                    } else {
                        filterCountry.visibility = View.VISIBLE
                        filterCountySelected.visibility = View.GONE
                        countryCloseButton.visibility = View.GONE
                    }


                    if (state.regionName != null) {
                        filterRegion.visibility = View.GONE
                        filterRegionSelected.visibility = View.VISIBLE
                        cityCloseButton.visibility = View.VISIBLE
                        filterRegionName.text = state.regionName
                    } else {
                        filterRegion.visibility = View.VISIBLE
                        filterRegionSelected.visibility = View.GONE
                        cityCloseButton.visibility = View.GONE
                    }

                    if (state.regionName == null && state.countryName == null) {
                        selectionButton.visibility = View.GONE
                    } else selectionButton.visibility = View.VISIBLE
                }

                FilterPlaceScreenState.Default -> {
                    filterCountry.visibility = View.VISIBLE
                    filterRegion.visibility = View.VISIBLE

                    filterCountySelected.visibility = View.GONE
                    filterRegionSelected.visibility = View.GONE
                    countryCloseButton.visibility = View.GONE
                    cityCloseButton.visibility = View.GONE

                    selectionButton.visibility = View.GONE
                }

                FilterPlaceScreenState.EscapeScreen -> {
                    val action =
                        FilterPlaceFragmentDirections.actionFilterPlaceFragmentToFilterSettingsFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }
}
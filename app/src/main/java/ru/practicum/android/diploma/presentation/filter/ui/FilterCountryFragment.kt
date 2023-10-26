package ru.practicum.android.diploma.presentation.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentFilterCountryBinding
import ru.practicum.android.diploma.presentation.filter.adapters.RegionsAdapter
import ru.practicum.android.diploma.presentation.filter.models.FilterCountryScreenState
import ru.practicum.android.diploma.presentation.filter.view_model.FilterViewModel
import javax.inject.Inject


class FilterCountryFragment : Fragment() {

    @Inject
    lateinit var viewModel: FilterViewModel

    private var _binding: FragmentFilterCountryBinding? = null
    private val binding get() = _binding!!

    private lateinit var regionsAdapter: RegionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterCountryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()

        viewModel.getAllCountries()

        viewModel.filterCountryScreenState.observe(viewLifecycleOwner){state->
            manageScreenContent(state)
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView() {
        regionsAdapter = RegionsAdapter { region ->
            viewModel.editCountry(region)

            val action =
                FilterCountryFragmentDirections.actionFilterCountryFragmentToFilterPlaceFragment()
            findNavController().navigate(action)
        }

        binding.recyclerViewCountry.adapter = regionsAdapter
        binding.recyclerViewCountry.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCountry.setHasFixedSize(true)
    }

    private fun manageScreenContent(state: FilterCountryScreenState) {
        with(binding) {
            when (state) {
                is FilterCountryScreenState.Content -> {
                    regionsAdapter.submitList(state.countries)
                    recyclerViewCountry.visibility = View.VISIBLE
                    progressBarCountry.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    layoutUnableToGetResult.unableToGetResultLayout.visibility = View.GONE
                }

                FilterCountryScreenState.Loading -> {
                    recyclerViewCountry.visibility = View.GONE
                    progressBarCountry.visibility = View.VISIBLE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    layoutUnableToGetResult.unableToGetResultLayout.visibility = View.GONE
                }

                FilterCountryScreenState.NoInternet -> {
                    recyclerViewCountry.visibility = View.GONE
                    progressBarCountry.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.VISIBLE
                    layoutUnableToGetResult.unableToGetResultLayout.visibility = View.GONE
                }

                FilterCountryScreenState.UnableToGetResult -> {
                    recyclerViewCountry.visibility = View.GONE
                    progressBarCountry.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    layoutUnableToGetResult.unableToGetResultLayout.visibility = View.VISIBLE
                }
            }
        }
    }
}
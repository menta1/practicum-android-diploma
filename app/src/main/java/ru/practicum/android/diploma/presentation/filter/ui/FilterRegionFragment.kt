package ru.practicum.android.diploma.presentation.filter.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.presentation.filter.adapters.RegionsAdapter
import ru.practicum.android.diploma.presentation.filter.models.FilterRegionScreenState
import ru.practicum.android.diploma.presentation.filter.view_model.FilterViewModel
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject


class FilterRegionFragment : Fragment() {

    @Inject
    lateinit var viewModel: FilterViewModel

    private var _binding: FragmentFilterRegionBinding? = null
    private val binding get() = _binding!!
    private lateinit var regionsAdapter: RegionsAdapter
    private lateinit var filterDebounce: (CharSequence?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterRegionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterDebounce = debounce(BEFORE_FILTERING_DELAY, lifecycleScope, false) { input ->
            viewModel.filterList(input.toString())
        }

        setRecyclerView()

        viewModel.getFilter()
        viewModel.getAllRegions()
        viewModel.filterRegionScreenState.observe(viewLifecycleOwner) { state ->
            manageScreenContent(state)
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterDebounce(s)
                manageMagnifierImageVisibility(s)
                manageCloseImageVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.editSearch.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            binding.editSearchLayout.isHintEnabled = !hasFocus
            if (hasFocus) showKeyboard()
        }

        binding.closeImage.setOnClickListener {
            binding.editSearch.setText("")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun manageMagnifierImageVisibility(s: CharSequence?) {
        if (s.isNullOrBlank()) {
            binding.magnifierImage.visibility = View.VISIBLE
        } else binding.magnifierImage.visibility = View.GONE
    }

    private fun manageCloseImageVisibility(s: CharSequence?) {
        if (s.isNullOrBlank()) {
            binding.closeImage.visibility = View.GONE
        } else binding.closeImage.visibility = View.VISIBLE
    }

    private fun setRecyclerView() {
        regionsAdapter = RegionsAdapter { region ->
            viewModel.editRegion(region)
        }

        binding.recyclerRegion.adapter = regionsAdapter
        binding.recyclerRegion.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRegion.setHasFixedSize(true)
    }

    private fun manageScreenContent(state: FilterRegionScreenState) {
        with(binding) {
            when (state) {
                is FilterRegionScreenState.Content -> {

                    regionsAdapter.submitList(state.regions)

                    if (state.regions.isEmpty()) {
                        layoutErrorNotFound.visibility = View.VISIBLE
                        recyclerRegion.visibility = View.GONE
                        hideKeyboard()
                    } else {
                        layoutErrorNotFound.visibility = View.GONE
                        recyclerRegion.visibility = View.VISIBLE
                    }

                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    layoutUnableToGetResult.unableToGetResultLayout.visibility = View.GONE
                    progressBarCountry.visibility = View.GONE
                }

                FilterRegionScreenState.Loading -> {
                    layoutErrorNotFound.visibility = View.GONE
                    recyclerRegion.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    layoutUnableToGetResult.unableToGetResultLayout.visibility = View.GONE
                    progressBarCountry.visibility = View.VISIBLE
                }

                FilterRegionScreenState.NoInternet -> {
                    layoutErrorNotFound.visibility = View.GONE
                    recyclerRegion.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.VISIBLE
                    layoutUnableToGetResult.unableToGetResultLayout.visibility = View.GONE
                    progressBarCountry.visibility = View.GONE
                    hideKeyboard()
                }

                FilterRegionScreenState.UnableToGetResult -> {
                    layoutErrorNotFound.visibility = View.GONE
                    recyclerRegion.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    layoutUnableToGetResult.unableToGetResultLayout.visibility = View.VISIBLE
                    progressBarCountry.visibility = View.GONE
                    hideKeyboard()
                }

                FilterRegionScreenState.EscapeScreen -> {
                        val action =
                            FilterRegionFragmentDirections.actionFilterRegionFragmentToFilterPlaceFragment()
                        findNavController().navigate(action)
                }
            }
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.containerView.windowToken, 0)
    }

    private fun showKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.editSearch, 0)
    }

    companion object {
        const val BEFORE_FILTERING_DELAY = 1000L
    }
}
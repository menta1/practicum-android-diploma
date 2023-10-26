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
import ru.practicum.android.diploma.databinding.FragmentFilterSectorBinding
import ru.practicum.android.diploma.presentation.filter.adapters.IndustriesAdapter
import ru.practicum.android.diploma.presentation.filter.models.FilterIndustryScreenState
import ru.practicum.android.diploma.presentation.filter.view_model.FilterViewModel
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject


class FilterSectorFragment : Fragment() {

    @Inject
    lateinit var viewModel: FilterViewModel

    private var _binding: FragmentFilterSectorBinding? = null
    private val binding get() = _binding!!
    private lateinit var industriesAdapter: IndustriesAdapter
    private lateinit var filterDebounce: (CharSequence?) -> Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFilterSectorBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterDebounce =
            debounce(FilterRegionFragment.BEFORE_FILTERING_DELAY, lifecycleScope, false) { input ->
                viewModel.filterIndustryList(input.toString())
            }

        setRecyclerView()

        with(viewModel){
            getFilter()
            getAllIndustries()

            filterIndustryScreenState.observe(viewLifecycleOwner) { state ->
                manageScreenContents(state)
            }
        }

        with(binding){
            selectionButton.setOnClickListener {
                val action =
                    FilterSectorFragmentDirections.actionFilterSectorFragmentToFilterSettingsFragment()
                findNavController().navigate(action)
            }

            backButton.setOnClickListener {
                findNavController().navigateUp()
            }

            editSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    filterDebounce(s)
                    manageMagnifierImageVisibility(s)
                    manageCloseImageVisibility(s)
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            editSearch.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                editSearchLayout.isHintEnabled = !hasFocus
                if (hasFocus) showKeyboard()
            }

            closeImage.setOnClickListener {
                editSearch.setText("")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView() {
        industriesAdapter = IndustriesAdapter { industry, position ->

            viewModel.handleRadioButtonsChecks(position)
            industriesAdapter.notifyDataSetChanged()
            viewModel.editIndustry(industry)

        }
        binding.recyclerIndustry.adapter = industriesAdapter
        binding.recyclerIndustry.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun manageScreenContents(state: FilterIndustryScreenState) {

        with(binding) {
            when (state) {

                is FilterIndustryScreenState.Content -> {

                    industriesAdapter.submitList(state.industries)

                    if (state.industries.isEmpty()){
                        notFoundError.visibility = View.VISIBLE
                        recyclerIndustry.visibility = View.GONE
                        binding.selectionButton.visibility = View.GONE
                        hideKeyboard()
                    }
                    else {
                        notFoundError.visibility = View.GONE
                        recyclerIndustry.visibility = View.VISIBLE
                    }

                    if (state.isSelected){
                        binding.selectionButton.visibility = View.VISIBLE
                        hideKeyboard()
                    }
                    else{
                        binding.selectionButton.visibility = View.GONE
                    }

                    progressBar.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    failedGetListError.visibility = View.GONE

                }
                FilterIndustryScreenState.Loading -> {
                    notFoundError.visibility = View.GONE
                    recyclerIndustry.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    failedGetListError.visibility = View.GONE
                    binding.selectionButton.visibility = View.GONE
                }

                FilterIndustryScreenState.NoInternet -> {
                    notFoundError.visibility = View.GONE
                    recyclerIndustry.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.VISIBLE
                    failedGetListError.visibility = View.GONE
                    binding.selectionButton.visibility = View.GONE
                    hideKeyboard()
                }

                FilterIndustryScreenState.UnableToGetResult -> {
                    notFoundError.visibility = View.GONE
                    recyclerIndustry.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    failedGetListError.visibility = View.VISIBLE
                    binding.selectionButton.visibility = View.GONE
                    hideKeyboard()

                }
            }
        }
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

}
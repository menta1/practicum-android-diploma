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
import ru.practicum.android.diploma.presentation.filter.models.FilterRegionScreenState
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
        arguments?.let {

        }
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

            industries.observe(viewLifecycleOwner) { industries ->
                industriesAdapter.submitList(industries)
            }

            filterRegionScreenState.observe(viewLifecycleOwner) { state ->
                manageScreenContents(state)
            }

            isSelectionButtonVisible.observe(viewLifecycleOwner) { isItVisible ->
                binding.selectionButton.visibility = if (isItVisible) View.VISIBLE else View.GONE
            }

            isTimeToHideKeyboard.observe(viewLifecycleOwner){isTimeToHideKeyBoard->
                if (isTimeToHideKeyBoard) hideKeyboard()
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
        binding.recyclerIndustry.setHasFixedSize(true)
    }

    private fun manageScreenContents(state: FilterRegionScreenState) {

        with(binding) {
            when (state) {

                is FilterRegionScreenState.Content -> {
                    if (state.isListEmpty) {
                        notFoundError.visibility = View.VISIBLE
                        recyclerIndustry.visibility = View.GONE
                        hideKeyboard()
                        binding.selectionButton.visibility = View.GONE
                    } else {
                        notFoundError.visibility = View.GONE
                        recyclerIndustry.visibility = View.VISIBLE
                    }

                    progressBar.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    failedGetListError.visibility = View.GONE
                }

                FilterRegionScreenState.Loading -> {
                    notFoundError.visibility = View.GONE
                    recyclerIndustry.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    failedGetListError.visibility = View.GONE
                    binding.selectionButton.visibility = View.GONE
                }

                FilterRegionScreenState.NoInternet -> {
                    notFoundError.visibility = View.GONE
                    recyclerIndustry.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.VISIBLE
                    failedGetListError.visibility = View.GONE
                    binding.selectionButton.visibility = android.view.View.GONE
                }

                FilterRegionScreenState.UnableToGetResult -> {
                    notFoundError.visibility = View.GONE
                    recyclerIndustry.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    layoutNoInternet.noInternetLayout.visibility = View.GONE
                    failedGetListError.visibility = View.VISIBLE
                    binding.selectionButton.visibility = View.GONE
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
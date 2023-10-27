package ru.practicum.android.diploma.presentation.filter.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentFilterSettingsBinding
import ru.practicum.android.diploma.presentation.filter.models.FilterScreenState
import ru.practicum.android.diploma.presentation.filter.view_model.FilterViewModel
import ru.practicum.android.diploma.util.debounce
import javax.inject.Inject


class FilterSettingsFragment : Fragment() {

    @Inject
    lateinit var viewModel: FilterViewModel

    private var _binding: FragmentFilterSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var editDebounce: (CharSequence?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editDebounce = debounce(SHARED_PREFS_EDITING_DELAY, lifecycleScope, true) { input ->
            viewModel.editExpectedSalary(input)
        }

        with(viewModel){
            getFilter()
            filterScreenState.observe(viewLifecycleOwner) { state ->
                manageScreenContent(state)
            }
        }

        with(binding){

            filterPlaceWorkCloseButton.setOnClickListener { viewModel.clearWorkPlace() }

            filterIndustryCloseButton.setOnClickListener { viewModel.clearIndustry() }

            salaryFilterClearButton.setOnClickListener {
                filterSalaryInputEditText.setText("")
                viewModel.clearExpectedSalary()
            }

            filterSalaryInputEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    manageClearButtonVisibility(s)
                    editDebounce(s)
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

            filterSalary.setOnCheckedChangeListener { _, isChecked ->
                viewModel.editIsOnlyWithSalary(isChecked)
            }

            filterSettingClearButton.setOnClickListener { viewModel.clearFiler() }

            filterSettingSelectButton.setOnClickListener {
                viewModel.putSearchingMode(true)
                findNavController().navigateUp()
            }

            filterPlaceWork.setOnClickListener {
                val action =
                    FilterSettingsFragmentDirections.actionFilterSettingsFragmentToFilterPlaceFragment()
                findNavController().navigate(action)
            }

            filterPlaceWorkCloseButton.setOnClickListener {
                viewModel.clearPlace()
            }

            filterPlaceWorkSelected.setOnClickListener {
                val action =
                    FilterSettingsFragmentDirections.actionFilterSettingsFragmentToFilterPlaceFragment()
                findNavController().navigate(action)
            }

            filterIndustry.setOnClickListener {
                val action =
                    FilterSettingsFragmentDirections.actionFilterSettingsFragmentToFilterSectorFragment()
                findNavController().navigate(action)
            }

            filterIndustrySelected.setOnClickListener {
                val action =
                    FilterSettingsFragmentDirections.actionFilterSettingsFragmentToFilterSectorFragment()
                findNavController().navigate(action)
            }
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
                    filterSettingClearButton.visibility = View.VISIBLE
                    filterSettingSelectButton.visibility = View.VISIBLE

                    if (state.countryName == null && state.regionName == null) {
                        filterPlaceWork.visibility = View.VISIBLE
                        filterPlaceWorkSelected.visibility = View.GONE
                        filterPlaceWorkCloseButton.visibility = View.GONE
                        filterPlaceWorkName.text = ""
                    } else if (state.countryName != null && state.regionName != null) {
                        filterPlaceWork.visibility = View.GONE
                        filterPlaceWorkSelected.visibility = View.VISIBLE
                        filterPlaceWorkCloseButton.visibility = View.VISIBLE
                        val workPlace = "${state.countryName}, ${state.regionName}"
                        filterPlaceWorkName.text = workPlace
                    } else {
                        filterPlaceWork.visibility = View.GONE
                        filterPlaceWorkSelected.visibility = View.VISIBLE
                        filterPlaceWorkCloseButton.visibility = View.VISIBLE
                        filterPlaceWorkName.text = state.countryName
                    }

                    if (state.industryName != null) {
                        filterIndustry.visibility = View.GONE
                        filterIndustrySelected.visibility = View.VISIBLE
                        filterIndustryCloseButton.visibility = View.VISIBLE
                        filterIndustryName.text = state.industryName
                    } else {
                        filterIndustry.visibility = View.VISIBLE
                        filterIndustrySelected.visibility = View.GONE
                        filterIndustryCloseButton.visibility = View.GONE
                        filterIndustryName.text = ""
                    }

                    state.expectedSalary?.let {
                        if (filterSalaryInputEditText.text.isNullOrBlank()) {
                            filterSalaryInputEditText.setText(it.toString())
                        }
                    }

                    filterSalary.isChecked = state.isOnlyWithSalary

                    setupBackNavigation(state.isClearButtonPressed)
                }

                is FilterScreenState.Default -> {
                    filterPlaceWork.visibility = View.VISIBLE
                    filterIndustry.visibility = View.VISIBLE
                    filterIndustrySelected.visibility = View.GONE
                    filterPlaceWorkSelected.visibility = View.GONE
                    filterSalary.isChecked = false
                    filterSalaryInputEditText.setText("")

                    filterSettingClearButton.visibility = View.GONE
                    filterSettingSelectButton.visibility = View.GONE

                    filterPlaceWorkCloseButton.visibility = View.GONE
                    filterIndustryCloseButton.visibility = View.GONE

                    setupBackNavigation(state.isClearButtonPressed)
                }

                is FilterScreenState.SalaryInput -> {
                    binding.filterSettingClearButton.visibility = if (state.isInputNotEmpty )View.VISIBLE else View.GONE
                    binding.filterSettingSelectButton.visibility = if (state.isInputNotEmpty  )View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun manageClearButtonVisibility(s: CharSequence?) {
        binding.salaryFilterClearButton.visibility =
            if (s.isNullOrBlank()) View.GONE else View.VISIBLE
    }

    private fun setupBackNavigation(isClearPressed: Boolean){
        binding.buttonBack.setOnClickListener {
            viewModel.putSearchingMode(isClearPressed)
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.putSearchingMode(isClearPressed)
            findNavController().navigateUp()
        }
    }

    companion object {
        const val SHARED_PREFS_EDITING_DELAY = 500L
    }

}
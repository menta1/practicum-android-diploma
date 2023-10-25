package ru.practicum.android.diploma.presentation.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.adapter.VacancyAdapter
import ru.practicum.android.diploma.presentation.search.SearchModelState
import ru.practicum.android.diploma.presentation.search.view_model.SearchViewModel
import javax.inject.Inject


class SearchFragment : Fragment(), VacancyAdapter.Listener {

    companion object {
        const val START_SEARCH = "startSearch"
    }

    @Inject
    lateinit var viewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var startSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            startSearch = it.getBoolean(START_SEARCH)
        }
        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            viewModel.getStartingInfo(it.getBoolean(START_SEARCH))
        }
        viewModel.getFilter()
        viewModel.startSearchIfNewFiltersSelected()
        viewModel.savedInput.observe(viewLifecycleOwner) { savedInput ->
            binding.editSearch.setText(savedInput)
        }

        val adapter = VacancyAdapter(this)
        val itemDecorator =
            VacancyAdapter.MarginItemDecorator(resources.getDimensionPixelSize(R.dimen.item_margin_top))
        binding.recyclerVacancy.adapter = adapter
        binding.recyclerVacancy.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerVacancy.addItemDecoration(itemDecorator)
        setVacancies(adapter)
        setupSearchInput()
        clearTextSearch()
        scrolling(adapter)
        stateView()
        openFilters()
        manageFilterButtonsVisibility(viewModel.isFilterEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFilter()
    }

    override fun onClick(item: Vacancy) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToDetailsFragment(item.id)
        )
    }

    private fun stateView() {
        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchModelState.NoSearch -> stateNoSearch()

                SearchModelState.Loading -> stateLoading()

                SearchModelState.Search -> stateSearch()

                SearchModelState.Loaded -> stateLoaded()

                SearchModelState.NoInternet -> stateNoInternet()

                SearchModelState.FailedToGetList -> stateFailedToGetList()

                SearchModelState.ServerError -> stateServerError()
            }
        }
    }

    private fun stateServerError() {
        with(binding) {
            progressBar.visibility = View.GONE
            imageSearchNotStarted.visibility = View.GONE
            searchMessage.visibility = View.GONE
            recyclerVacancy.visibility = View.GONE
            recyclerVacancyLayout.visibility = View.GONE
            errorServer.serverErrorLayout.visibility = View.VISIBLE
            errorNoInternet.noInternetLayout.visibility = View.GONE
            errorFailedGetCat.errorFailedGetCat.visibility = View.GONE
        }
    }

    private fun stateNoSearch() {
        with(binding) {
            imageSearchNotStarted.visibility = View.VISIBLE
            recyclerVacancy.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchMessage.visibility = View.GONE
            recyclerVacancyLayout.visibility = View.GONE
            errorNoInternet.noInternetLayout.visibility = View.GONE
            errorServer.serverErrorLayout.visibility = View.GONE
            errorFailedGetCat.errorFailedGetCat.visibility = View.GONE
        }
    }

    private fun stateLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            imageSearchNotStarted.visibility = View.GONE
            searchMessage.visibility = View.GONE
            recyclerVacancy.visibility = View.GONE
            recyclerVacancyLayout.visibility = View.GONE
            errorNoInternet.noInternetLayout.visibility = View.GONE
            errorFailedGetCat.errorFailedGetCat.visibility = View.GONE
            errorServer.serverErrorLayout.visibility = View.GONE
        }
    }

    private fun stateSearch() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            imageSearchNotStarted.visibility = View.GONE
            recyclerVacancy.visibility = View.VISIBLE
            recyclerVacancyLayout.visibility = View.VISIBLE
            searchMessage.visibility = View.VISIBLE
            errorNoInternet.noInternetLayout.visibility = View.GONE
            errorFailedGetCat.errorFailedGetCat.visibility = View.GONE
            errorServer.serverErrorLayout.visibility = View.GONE
        }
    }

    private fun stateLoaded() {
        with(binding) {
            progressBar.visibility = View.GONE
            imageSearchNotStarted.visibility = View.GONE
            recyclerVacancy.visibility = View.VISIBLE
            recyclerVacancyLayout.visibility = View.VISIBLE
            searchMessage.visibility = View.VISIBLE
            errorNoInternet.noInternetLayout.visibility = View.GONE
            errorFailedGetCat.errorFailedGetCat.visibility = View.GONE
            errorServer.serverErrorLayout.visibility = View.GONE
        }
    }

    private fun stateNoInternet() {
        with(binding) {
            progressBar.visibility = View.GONE
            imageSearchNotStarted.visibility = View.GONE
            recyclerVacancy.visibility = View.GONE
            recyclerVacancyLayout.visibility = View.GONE
            searchMessage.visibility = View.GONE
            errorNoInternet.noInternetLayout.visibility = View.VISIBLE
            errorFailedGetCat.errorFailedGetCat.visibility = View.GONE
            errorServer.serverErrorLayout.visibility = View.GONE
        }
    }

    private fun stateFailedToGetList() {
        with(binding) {
            progressBar.visibility = View.GONE
            imageSearchNotStarted.visibility = View.GONE
            recyclerVacancy.visibility = View.GONE
            searchMessage.visibility = View.GONE
            recyclerVacancyLayout.visibility = View.VISIBLE
            errorNoInternet.noInternetLayout.visibility = View.GONE
            errorFailedGetCat.errorFailedGetCat.visibility = View.VISIBLE
            errorServer.serverErrorLayout.visibility = View.GONE
        }
    }

    private fun scrolling(adapter: VacancyAdapter) {
        binding.recyclerVacancy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos =
                        (binding.recyclerVacancy.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })
    }


    private fun setVacancies(adapter: VacancyAdapter) {
        viewModel.usersLiveData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        viewModel.usersFoundLiveData.observe(viewLifecycleOwner) {
            if (it.isNotBlank()) {
                binding.searchMessage.text = formatVacanciesString(it.toInt())
            }
        }
    }

    private fun formatVacanciesString(count: Int): String {
        val vacanciesWordForms = listOf("вакансия", "вакансии", "вакансий")
        val lastTwoDigits = count % 100
        val lastDigit = lastTwoDigits % 10

        return when {
            lastTwoDigits in 11..19 -> "Найдено $count ${vacanciesWordForms[2]}"
            lastDigit == 1 -> "Найдена $count ${vacanciesWordForms[0]}"
            count == 0 -> "Таких вакансий нет"
            lastDigit in 2..4 -> "Найдено $count ${vacanciesWordForms[1]}"
            else -> "Найдено $count ${vacanciesWordForms[2]}"
        }
    }

    private fun clearTextSearch() {
        binding.clearButton.setOnClickListener {
            binding.editSearch.setText("")
            stateNoSearch()
            it.hideKeyboard()
        }
    }

    private fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setupSearchInput() {
        binding.editSearch.addTextChangedListener {
            viewModel.onTextChangedInput(it)
            if (it?.isNotEmpty() == true) {
                binding.searchButton.visibility = View.GONE
                binding.clearButton.visibility = View.VISIBLE
            } else {
                binding.searchButton.visibility = View.VISIBLE
                binding.clearButton.visibility = View.GONE
            }
            binding.editSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.search()
                }
                false
            }
        }
    }

    private fun manageFilterButtonsVisibility(isFilterEmpty: Boolean) {
        binding.buttonFiltersEmpty.visibility = if (isFilterEmpty) View.VISIBLE else View.GONE
        binding.buttonFiltersNotEmpty.visibility = if (isFilterEmpty) View.GONE else View.VISIBLE
    }

    private fun openFilters() {
        binding.buttonFiltersEmpty.setOnClickListener {
            viewModel.editSavedInput(binding.editSearch.text.toString())
            findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
        }
        binding.buttonFiltersNotEmpty.setOnClickListener {
            viewModel.editSavedInput(binding.editSearch.text.toString())
            findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
        }
    }
}
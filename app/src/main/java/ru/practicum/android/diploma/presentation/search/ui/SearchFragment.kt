package ru.practicum.android.diploma.presentation.search.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
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
import ru.practicum.android.diploma.presentation.search.LoadingPageErrorStates
import ru.practicum.android.diploma.presentation.search.SearchModelState
import ru.practicum.android.diploma.presentation.search.view_model.SearchViewModel
import javax.inject.Inject


class SearchFragment : Fragment(), VacancyAdapter.Listener {


    @Inject
    lateinit var viewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

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

        viewModel.getFilter()
        viewModel.startSearchIfNewFiltersSelected()
        viewModel.savedInput.observe(viewLifecycleOwner){savedInput->
            binding.editSearch.setText(savedInput)
        }
        viewModel.isNextPageLoading.observe(viewLifecycleOwner){isNextPageLoading->
            managePagingProgressVisibility(isNextPageLoading)
        }

        viewModel.loadingPageErrorState.observe(viewLifecycleOwner){state->
            manageLoadingPageErrors(state)
        }

        val adapter = VacancyAdapter(requireContext(), this)
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
            }
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
            binding.pagingProgressBar.visibility= View.GONE
            hideKeyboard()
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
            binding.pagingProgressBar.visibility= View.GONE
        }
    }

    private fun stateFailedToGetList() {
        with(binding) {
            progressBar.visibility = View.GONE
            imageSearchNotStarted.visibility = View.GONE
            recyclerVacancy.visibility = View.GONE
            searchMessage.visibility = View.VISIBLE
            recyclerVacancyLayout.visibility = View.VISIBLE
            errorNoInternet.noInternetLayout.visibility = View.GONE
            errorFailedGetCat.errorFailedGetCat.visibility = View.VISIBLE
            binding.pagingProgressBar.visibility= View.GONE
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

    private fun clearTextSearch(){
        binding.clearButton.setOnClickListener{
            binding.editSearch.setText("")
            showKeyboard()
        }
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
        }
    }

    private fun manageFilterButtonsVisibility(isFilterEmpty: Boolean) {
        binding.buttonFiltersEmpty.visibility = if (isFilterEmpty) View.VISIBLE else View.GONE
        binding.buttonFiltersNotEmpty.visibility = if (isFilterEmpty) View.GONE else View.VISIBLE
    }

    private fun openFilters() {
        binding.buttonFiltersEmpty.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
        }
        binding.buttonFiltersNotEmpty.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
        }
    }

    private fun managePagingProgressVisibility(isNewPageLoading: Boolean){
        binding.pagingProgressBar.visibility = if(isNewPageLoading) View.VISIBLE else View.GONE
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

    private fun manageLoadingPageErrors(state: LoadingPageErrorStates){

        when(state){
            LoadingPageErrorStates.Default -> {

            }
            LoadingPageErrorStates.NoInternet -> {
                Toast.makeText(requireContext(),getString(R.string.no_internet_while_loading_page), Toast.LENGTH_LONG).show()
            }
            LoadingPageErrorStates.ServerError -> {
                Toast.makeText(requireContext(),getString(R.string.server_error_while_loading_page), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
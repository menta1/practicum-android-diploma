package ru.practicum.android.diploma.presentation.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.adapter.VacancyAdapter
import ru.practicum.android.diploma.presentation.details.ui.DetailsFragment
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = VacancyAdapter(this)
        binding.recyclerVacancy.adapter = adapter
        binding.recyclerVacancy.layoutManager = LinearLayoutManager(requireContext())
        setVacancies(adapter)
        setupSearchInput()
        scrolling(adapter)
        stateView()
        openFilters()
    }

    private fun stateView() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { state ->
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.usersFlow.collect { list ->
                adapter.setData(list)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.usersFound.collect {
                if (it.isNotBlank())
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
            lastDigit == 0 -> "Таких вакансий нет"
            lastDigit in 2..4 -> "Найдено $count ${vacanciesWordForms[1]}"
            else -> "Найдено $count ${vacanciesWordForms[2]}"
        }
    }

    private fun setupSearchInput() {
        binding.editSearch.addTextChangedListener {
            viewModel.onTextChangedInput(it)
            binding.editSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.search()
                }
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: Vacancy) {
        val bundle = bundleOf(DetailsFragment.VACANCY to item.id)
        findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
    }

    private fun openFilters() {
        binding.buttonFilters.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterSettingsFragment)
        }
    }
}
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
import ru.practicum.android.diploma.presentation.search.SearchModelState
import ru.practicum.android.diploma.presentation.search.view_model.SearchViewModel
import javax.inject.Inject


class SearchFragment : Fragment(), VacancyAdapter.Listener {

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFilter()
        val adapter = VacancyAdapter(this)
        binding.recyclerVacancy.adapter = adapter
        binding.recyclerVacancy.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonToFilter.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToFilterSettingsFragment()
            findNavController().navigate(action)
        }

        setVacancies(adapter)
        setupSearchInput()
        scrolling(adapter)
        stateView()
    }

    private fun stateView() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect { state ->
                when (state) {
                    SearchModelState.NoSearch -> {
                        binding.imageSearchNotStarted.visibility = View.VISIBLE
                        binding.recyclerVacancy.visibility = View.GONE
                        binding.searchMessage.visibility = View.GONE
                    }

                    SearchModelState.Loading -> {
//                        binding.imageSearchNotStarted.visibility = View.GONE
//                        binding.searchMessage.visibility = View.GONE
//                        binding.recyclerVacancy.visibility = View.GONE
//                        binding.progressBar.visibility = View.VISIBLE
                    }

                    SearchModelState.Search -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                        binding.imageSearchNotStarted.visibility = View.GONE
//                        binding.recyclerVacancy.visibility = View.VISIBLE
//                        binding.searchMessage.visibility = View.VISIBLE
                    }

                    SearchModelState.Loaded -> {
                        binding.progressBar.visibility = View.GONE
                        binding.imageSearchNotStarted.visibility = View.GONE
                        binding.recyclerVacancy.visibility = View.VISIBLE
                        binding.searchMessage.visibility = View.VISIBLE
                    }

                    SearchModelState.NoInternet -> {

                    }

                    SearchModelState.FailedToGetList -> {
                        binding.imageSearchNotStarted.visibility = View.GONE
                        binding.searchMessage.visibility = View.VISIBLE
                        binding.recyclerVacancy.visibility = View.GONE
                        //Добавить лайоут что нет таких вакансий
                    }

                    else -> {}
                }
            }
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
        //viewModel.onClick(item)
        val bundle = bundleOf(VACANCY to item.id)
        findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
    }

    companion object{
        const val START_SEARCH = "startSearch"
        const val VACANCY = "vacancy"
    }
}
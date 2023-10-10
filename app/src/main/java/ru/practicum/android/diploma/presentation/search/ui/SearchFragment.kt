package ru.practicum.android.diploma.presentation.search.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.App
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
    }

    private fun stateView(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.collect{
                when(it){
                    SearchModelState.Loading -> {

                    }
                    SearchModelState.Loaded -> {
                        binding.imageSearchNotStarted.visibility = View.GONE
                        binding.recyclerVacancy.visibility = View.VISIBLE
                    }
                    SearchModelState.NoInternet -> {

                    }
                    SearchModelState.FailedToGetList -> {

                    }
                    else -> {}
                }
            }
        }
    }

    private fun scrolling(adapter: VacancyAdapter){
        binding.recyclerVacancy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos = (binding.recyclerVacancy.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount-1) {
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
    }

    private fun setupSearchInput() {
        binding.editSearch.addTextChangedListener {
            viewModel.search(it.toString(), false)
            binding.editSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.search(it.toString(), true)
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
        viewModel.onClick(item)
    }
}
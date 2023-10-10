package ru.practicum.android.diploma.presentation.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.presentation.adapter.VacanciesLoaderStateAdapter
import ru.practicum.android.diploma.presentation.adapter.VacancyAdapter
import ru.practicum.android.diploma.presentation.search.view_model.SearchViewModel
import javax.inject.Inject


class SearchFragment : Fragment() {

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
        val adapter = VacancyAdapter()
        val loaderAdapter = VacanciesLoaderStateAdapter()
        val adapterWithLoadState =
            adapter.withLoadStateFooter(loaderAdapter)
        binding.recyclerVacancy.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerVacancy.adapter = adapterWithLoadState

        setVacancies(adapter)
        setupSearchInput()

        adapter.addLoadStateListener { state ->
            with(binding) {
                if (state.refresh != LoadState.Loading) {
                    progressBar.visibility = View.GONE
                    recyclerVacancy.visibility = View.VISIBLE
                }
                if (state.refresh == LoadState.Loading) {
                    recyclerVacancy.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }

            }
        }

        binding.buttonToFilter.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToFilterSettingsFragment()
            findNavController().navigate(action)
        }
    }

    private fun setVacancies(adapter: VacancyAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.usersFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    private fun setupSearchInput() {
        binding.editSearch.addTextChangedListener {
            if (it.isNullOrBlank()){
                binding.imageSearchNotStarted.visibility = View.VISIBLE
                binding.recyclerVacancy.visibility = View.GONE
            }else{
                binding.imageSearchNotStarted.visibility = View.GONE
                binding.recyclerVacancy.visibility = View.VISIBLE
            }
            viewModel.search(it.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
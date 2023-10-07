package ru.practicum.android.diploma.presentation.favourite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentFavouriteBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.favourite.models.FavouriteState
import ru.practicum.android.diploma.presentation.favourite.view_model.FavouriteViewModel
import javax.inject.Inject

class FavouriteFragment : Fragment() {

    @Inject
    lateinit var viewModel: FavouriteViewModel

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val adapter = FavouriteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(layoutInflater, container, false)
        binding.recyclerFavourite.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavourite.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initData()
        viewModel.getFavouriteStateLiveData().observe(viewLifecycleOwner) { state ->
            when(state) {
                is FavouriteState.Loading -> {
                    changeContentVisibility(loading = true)
                }
                is FavouriteState.Empty -> {}
                is FavouriteState.Error -> {}
                is FavouriteState.Content -> {
                    changeContentVisibility(loading = false)
                    updateScreen(state.data)
                }
            }
        }
    }

    private fun changeContentVisibility(loading: Boolean) {
        binding.recyclerFavourite.isVisible = !loading
    }

    private fun updateScreen(data: List<Vacancy>) {
        adapter.data = data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
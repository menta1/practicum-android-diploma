package ru.practicum.android.diploma.presentation.favourite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFavouriteBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.favourite.models.FavouriteState
import ru.practicum.android.diploma.presentation.favourite.view_model.FavouriteViewModel
import javax.inject.Inject

class FavouriteFragment : Fragment(), ClickListener {

    @Inject
    lateinit var viewModel: FavouriteViewModel

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        FavouriteAdapter(requireContext(), this)
    }

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
            when (state) {
                is FavouriteState.Loading -> {
                    changeContentVisibility(loading = true)
                }

                is FavouriteState.Empty -> {
                    problemWithContentVisibility(isEmpty = true)
                }

                is FavouriteState.Error -> {
                    problemWithContentVisibility(isEmpty = false)
                }

                is FavouriteState.Content -> {
                    changeContentVisibility(loading = false)
                    updateScreen(state.data)
                }
            }
        }
    }

    private fun changeContentVisibility(loading: Boolean) {
        binding.layoutNotFound.visibility = View.GONE
        binding.recyclerFavourite.isVisible = !loading
        binding.progressBarFavourite.isVisible = loading
    }

    private fun problemWithContentVisibility(isEmpty: Boolean) {
        binding.layoutNotFound.visibility = View.VISIBLE
        binding.recyclerFavourite.visibility = View.GONE
        binding.progressBarFavourite.visibility = View.GONE

        binding.imageListEmpty.setImageDrawable(
            if (isEmpty) ResourcesCompat.getDrawable(
                resources,
                R.drawable.favourite_list_empty,
                null
            )
            else ResourcesCompat.getDrawable(resources, R.drawable.not_found, null)
        )
        binding.favouriteText.text = if (isEmpty) getString(R.string.list_empty)
        else getString(R.string.failed_get_list)
    }

    private fun updateScreen(data: List<Vacancy>) {
        adapter.data = data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun clickOnVacancy(vacancyId: String) {
        val bundle = Bundle()
        bundle.putString(VACANCY, vacancyId)
        findNavController().navigate(
            R.id.action_favouriteFragment_to_detailsFragment,
            bundle
        )
    }

    companion object {
        const val VACANCY = "vacancy"
    }
}
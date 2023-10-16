package ru.practicum.android.diploma.presentation.similar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSimilarBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.similar.models.SimilarState
import ru.practicum.android.diploma.presentation.similar.view_model.SimilarViewModel
import javax.inject.Inject

class SimilarFragment : Fragment(), SimilarClickListener {

    @Inject
    lateinit var viewModel: SimilarViewModel

    private var _binding: FragmentSimilarBinding? = null
    private val binding get() = _binding!!
    private var idVacancy: String? = null

    private val adapter by lazy {
        SimilarAdapter(requireContext(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idVacancy = it.getSerializable(VACANCY) as String
        }
        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarBinding.inflate(layoutInflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.init(idVacancy)
        scrolling(adapter)

        viewModel.getSimilarStateLiveData().observe(viewLifecycleOwner) { state ->
            when (state) {
                SimilarState.Loading -> {
                    changeContentVisibility(true)
                }

                SimilarState.Error -> {
                    problemWithContentVisibility(isNotConnect = false)
                }

                SimilarState.NoInternet -> {
                    problemWithContentVisibility(isNotConnect = true)
                }

                is SimilarState.Content -> {
                    changeContentVisibility(false)
                    updateData(state.data)
                }
            }
        }
    }

    private fun changeContentVisibility(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (loading) View.GONE else View.VISIBLE
        binding.serverErrorLayoutHost.serverErrorLayout.visibility = View.GONE
        binding.noInternetLayoutHost.noInternetLayout.visibility = View.GONE
    }

    private fun problemWithContentVisibility(isNotConnect: Boolean) {
        binding.noInternetLayoutHost.noInternetLayout.visibility =
            if (isNotConnect) View.VISIBLE else View.GONE
        binding.serverErrorLayoutHost.serverErrorLayout.visibility =
            if (isNotConnect) View.GONE else View.VISIBLE

        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
    }

    private fun updateData(data: List<Vacancy>) {
        adapter.data.addAll(data)
        adapter.notifyDataSetChanged()
    }

    private fun scrolling(adapter: SimilarAdapter) {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val pos =
                        (binding.recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.onLastItemReached()
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun clickOnVacancy(vacancyId: String) {
        val bundle = Bundle()
        bundle.putString(VACANCY, vacancyId)
        findNavController().navigate(
            R.id.action_similarFragment_to_detailsFragment,
            bundle
        )
    }

    companion object {
        const val VACANCY = "vacancy"
    }
}
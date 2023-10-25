package ru.practicum.android.diploma.presentation.similar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import ru.practicum.android.diploma.App
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

    private val adapter by lazy {
        SimilarAdapter(requireContext(), this)
    }

    private val args by navArgs<SimilarFragmentArgs>()
    private val vacancyId by lazy { args.vacancyId }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        viewModel.init(vacancyId)
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
                SimilarState.Empty -> {
                    binding.progressBarRecycler.visibility = View.GONE
                }
            }
        }

        binding.navigationBackButton.setOnClickListener {
            findNavController().popBackStack()
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
        binding.progressBarRecycler.visibility = View.GONE
    }

    private fun scrolling(adapter: SimilarAdapter) {
        binding.nestedScroll.setOnScrollChangeListener(object: NestedScrollView.OnScrollChangeListener {
            override fun onScrollChange(
                v: NestedScrollView,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    val pos =
                        (binding.recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        binding.progressBarRecycler.visibility = View.VISIBLE
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

    override fun clickOnVacancy(vacancyIdFind: String) {
        findNavController().navigate(
            SimilarFragmentDirections.actionSimilarFragmentToDetailsFragment(vacancyIdFind)
        )
    }
}
package ru.practicum.android.diploma.presentation.filter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentFilterCountryBinding
import ru.practicum.android.diploma.presentation.filter.adapters.RegionsAdapter
import ru.practicum.android.diploma.presentation.filter.view_model.FilterViewModel
import javax.inject.Inject


class FilterCountryFragment : Fragment() {

    @Inject
    lateinit var viewModel: FilterViewModel

    private var _binding: FragmentFilterCountryBinding? = null
    private val binding get() = _binding!!

    private lateinit var regionsAdapter: RegionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        (activity?.application as App).appComponent.activityComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterCountryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllCountries()
        viewModel.countries.observe(viewLifecycleOwner){countries->

        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerView(){

        regionsAdapter = RegionsAdapter { region ->
            val action = FilterCountryFragmentDirections.actionFilterCountryFragmentToFilterRegionFragment(region.id.toInt())
        }
    }
}
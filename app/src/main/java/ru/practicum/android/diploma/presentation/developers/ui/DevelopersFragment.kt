package ru.practicum.android.diploma.presentation.developers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.App
import ru.practicum.android.diploma.databinding.FragmentDevelopersBinding
import ru.practicum.android.diploma.presentation.developers.view_model.DevelopersViewModel
import javax.inject.Inject


class DevelopersFragment : Fragment() {

    @Inject
    lateinit var viewModel: DevelopersViewModel

    private var _binding: FragmentDevelopersBinding? = null
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
        _binding = FragmentDevelopersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            firstDeveloper.setOnClickListener {
                viewModel.openLinkGitHub("https://github.com/menta1")
            }
            secondDeveloper.setOnClickListener {
                viewModel.openLinkGitHub("https://github.com/vlodo-o")
            }
            thirdDeveloper.setOnClickListener {
                viewModel.openLinkGitHub("https://github.com/alexxk2")
            }
            fourthDeveloper.setOnClickListener {
                viewModel.openLinkGitHub("https://github.com/ImNia")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
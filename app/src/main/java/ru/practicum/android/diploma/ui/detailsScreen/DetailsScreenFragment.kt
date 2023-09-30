package ru.practicum.android.diploma.ui.detailsScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.detailsScreen.DetailsScreenViewModel

class DetailsScreenFragment : Fragment() {

    private lateinit var viewModel: DetailsScreenViewModel
    //private lateinit var binding:

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details_screen, container, false)
    }

}
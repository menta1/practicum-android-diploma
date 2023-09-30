package ru.practicum.android.diploma.ui.developersScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.developersScreen.DevelopersScreenViewModel

class DevelopersScreenFragment : Fragment() {

    private lateinit var viewModel: DevelopersScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_developers_screen, container, false)
    }

}
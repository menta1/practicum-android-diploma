package ru.practicum.android.diploma.ui.filterScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filterScreen.filterLocationScreen.countryScreen.CountryScreenViewModel

class CountryScreenFragment : Fragment() {

    private lateinit var viewModel: CountryScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_country_screen, container, false)
    }

}
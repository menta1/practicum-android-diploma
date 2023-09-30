package ru.practicum.android.diploma.ui.favouriteScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.favouriteScreen.FavouriteScreenViewModel

class FavouriteScreenFragment : Fragment() {

    private lateinit var viewModel: FavouriteScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourite_screen, container, false)
    }

}
package ru.practicum.android.diploma.presentation.root.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.BuildConfig
/*import ru.practicum.android.diploma.BuildConfig*/
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.similarFragment -> binding.bottomNavigation.visibility = View.GONE
                R.id.filterPlaceFragment -> binding.bottomNavigation.visibility = View.GONE
                R.id.filterSettingsFragment -> binding.bottomNavigation.visibility = View.GONE
                R.id.filterSectorFragment -> binding.bottomNavigation.visibility = View.GONE
                R.id.filterRegionFragment -> binding.bottomNavigation.visibility = View.GONE
                R.id.filterCountryFragment -> binding.bottomNavigation.visibility = View.GONE
                R.id.detailsFragment -> binding.bottomNavigation.visibility = View.GONE
                else -> binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        // Пример использования access token для HeadHunter API
        //networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()

    }

}
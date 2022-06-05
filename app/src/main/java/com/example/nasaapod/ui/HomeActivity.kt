package com.example.nasaapod.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nasaapod.R
import com.example.nasaapod.databinding.ActivityHomeBinding
import com.example.nasaapod.di.factory.ViewModelFactory
import com.example.nasaapod.ui.viewmodel.HomeViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {

    private lateinit var navController : NavController
    private var currentFragmentId: Int = 0
    private lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel : HomeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        setUpBottomNavigation()
        setNavDestinationChangeListener()
    }

    private fun setNavDestinationChangeListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailFragment) {
                binding.bottomNavigationBar.visibility = View.GONE
            } else {
                binding.bottomNavigationBar.visibility = View.VISIBLE
            }
            currentFragmentId = destination.id
        }
    }

    private fun setUpBottomNavigation() {
        binding.bottomNavigationBar.setupWithNavController(navController)
        binding.bottomNavigationBar.setOnNavigationItemReselectedListener {
            // Ignore the fragment reselection
        }
    }
}
package com.example.nasaapod.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nasaapod.R
import com.example.nasaapod.databinding.ActivityHomeBinding
import dagger.android.support.DaggerAppCompatActivity

class HomeActivity : DaggerAppCompatActivity() {

    private lateinit var navController : NavController
    private var currentFragmentId: Int = 0
    private lateinit var binding: ActivityHomeBinding

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
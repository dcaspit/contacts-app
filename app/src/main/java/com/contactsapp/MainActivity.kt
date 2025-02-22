package com.contactsapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.contactsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :  AppCompatActivity(){

    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private lateinit var binding: ActivityMainBinding

    private val fragmentIdsWithBottomNavigation = listOf(
        com.feature.home.R.id.homeFragment,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi() {
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in fragmentIdsWithBottomNavigation) {
//                binding.mainBottomNavigation.visibility = View.VISIBLE
                supportActionBar?.run {
                    setDisplayHomeAsUpEnabled(false)
                    setDisplayShowHomeEnabled(false)
                }
            } else {
//                binding.mainBottomNavigation.visibility = View.GONE
                supportActionBar?.run {
                    setDisplayHomeAsUpEnabled(true)
                    setDisplayShowHomeEnabled(true)
                }
            }
        }
        binding.mainBottomNavigation.setupWithNavController(navController)
    }
}
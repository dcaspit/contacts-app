package com.contactsapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.contactsapp.databinding.ActivityMainBinding

class MainActivity :  AppCompatActivity(){

    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private lateinit var binding: ActivityMainBinding

    private val homeFragment = com.feature.home.R.id.homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

}
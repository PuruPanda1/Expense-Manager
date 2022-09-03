package com.purabmodi.payment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.purabmodi.payment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomBar)
        bottomNavBar.setupWithNavController(navController)

        bottomNavBar.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            true
        }

        bottomNavBar.setOnItemReselectedListener { item ->
            val reselectedDestinationId = item.itemId
            val id = navController.currentDestination?.id
            when (id) {
                R.id.detailedTransactionAnalysis -> {
                    navController.navigate(R.id.action_detailedTransactionAnalysis_to_MainScreen)
                }
                R.id.detailedCategoryTransactionsFragment -> {
                    navController.navigate(R.id.action_detailedCategoryTransactionsFragment_to_MainScreen)
                }
                R.id.addTransaction -> {
                    navController.navigate(R.id.action_addTransaction_to_Stats)
                }
                R.id.incomeExpenseView -> {
                    navController.navigate(R.id.action_incomeExpenseView_to_MainScreen)
                }
            }
            navController.popBackStack(reselectedDestinationId, inclusive = false)
        }
    }
}
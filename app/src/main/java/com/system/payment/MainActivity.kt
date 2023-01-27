package com.system.payment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.system.payment.databinding.ActivityMainBinding
import com.system.payment.fragments.bottomSheet.AddNewBottomSheetDialog

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomBar)
        bottomNavBar.setupWithNavController(navController)

        bottomNavBar.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.addButton) {
                val addNewBottomSheetDialog = AddNewBottomSheetDialog(
                    openAddTransaction = { openAddTransaction() },
                    openBankTransfer = { openBankTransfer() }
                )
                addNewBottomSheetDialog.show(supportFragmentManager, "AddNewBottomSheetDialog")
                false
            } else {
                NavigationUI.onNavDestinationSelected(item, navController)
                navController.popBackStack(item.itemId, inclusive = false)
                true
            }
        }

        bottomNavBar.setOnItemReselectedListener { item ->
            val reselectedDestinationId = item.itemId
            navController.popBackStack(reselectedDestinationId, inclusive = false)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addTransaction -> {
                    bottomNavBar.visibility = View.GONE
                }
                R.id.accountTransfer -> {
                    bottomNavBar.visibility = View.GONE
                }
                R.id.detailedView -> {
                    bottomNavBar.visibility = View.GONE
                }
                else -> {
                    bottomNavBar.visibility = View.VISIBLE
                }
            }
        }

    }

    fun openAddTransaction() {
        navController.navigate(R.id.addTransaction)
    }

    fun openBankTransfer() {
        navController.navigate(R.id.accountTransfer)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}
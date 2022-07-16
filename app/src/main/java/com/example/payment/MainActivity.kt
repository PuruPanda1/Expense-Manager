package com.example.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.payment.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomBar)
        setupWithNavController(bottomNavBar,navController)



    }

    fun switchToGpay(){
        val i = packageManager.getLaunchIntentForPackage("com.google.android.apps.nbu.paisa.user")
        if(i!=null){
            startActivity(i)
            Toast.makeText(this, "Launching", Toast.LENGTH_SHORT).show()
        }
        else
        {
            Toast.makeText(this, "Failed to open Gpay", Toast.LENGTH_SHORT).show()
        }

    }
}
package com.purabmodi.payment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.purabmodi.payment.userDb.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var isPresent = false
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        userViewModel.userDetails.observe(this){
            if(it!=null){
                isPresent = true
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this,R.color.splash_screen)
        }

        lifecycleScope.launch(Dispatchers.Main){
            delay(2000)

            if(isPresent){
                startActivity(Intent(applicationContext,MainActivity::class.java))
            } else {
                startActivity(Intent(applicationContext,DropPage::class.java))
            }

            finish()
        }
    }
}
package com.system.payment

import android.app.Activity
import android.content.Intent
import android.icu.util.Currency
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mynameismidori.currencypicker.CurrencyPicker
import com.system.payment.accountsDb.Accounts
import com.system.payment.databinding.ActivityDropPageBinding
import com.system.payment.userDb.User
import com.system.payment.userDb.UserViewModel
import com.system.payment.viewModel.AccountViewModel

class DropPage : AppCompatActivity() {
    private lateinit var binding: ActivityDropPageBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var userPhoto: Uri
    private var currencyCode: String = "INR"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDropPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPhoto = Uri.parse("android.resource://com.purabmodi.payment/drawable/dev_photo")

        Glide.with(this).load(userPhoto).into(binding.userPhoto)

        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!

                    userPhoto = fileUri
                    Glide.with(this).load(userPhoto).into(binding.userPhoto)
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]


        binding.saveButton.setOnClickListener {
            setUserDetails()
        }

        binding.changeProfilePhoto.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .crop()
                .maxResultSize(
                    620,
                    620
                )
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.userCurrency.setOnClickListener {
            val picker = CurrencyPicker.newInstance("Select Currency")  // dialog title
            picker.setListener { _, code, _, _ ->
                currencyCode = code
                val currencyName = Currency.getInstance(currencyCode).displayName
                val currencySymbol = Currency.getInstance(currencyCode).symbol
                binding.userCurrency.text = "$currencyName ( $currencySymbol )"
                picker.dismiss()
            }
            picker.show(this.supportFragmentManager, "CURRENCY_PICKER")
        }

    }

    private fun setUserDetails() {
        val userName = binding.userName.text.toString()
        val budget = binding.userBudget.text.toString()
        var description: String = binding.userBio.text.toString()
        if (check(userName, budget)) {
            if (description.isEmpty()) {
                description = "Hey i am using BudgetWise"
            }
            userViewModel.insertUser(
                User(
                    1,
                    userName,
                    description,
                    budget.toInt(),
                    userPhoto.toString(),
                    currencyCode
                )
            )
            accountViewModel.insertAccount(Accounts(0, "CASH"))
            accountViewModel.insertAccount(Accounts(0, "BANK"))
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun check(userName: String, budget: String): Boolean {
        if (userName.isNullOrEmpty() || budget.isNullOrBlank()) {
            return false
        }
        return true
    }
}
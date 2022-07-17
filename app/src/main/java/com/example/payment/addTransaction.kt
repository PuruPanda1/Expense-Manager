package com.example.payment

import android.R.attr.autoText
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.payment.databinding.FragmentAddTransactionBinding
import com.example.payment.db.Transaction


class addTransaction : Fragment() {
    private val category = listOf<String>(
        "Groceries",
        "Stationary",
        "Fuel",
        "Transportation",
        "Suspense",
        "DineOut",
        "Entertainment",
        "Bills",
        "Shopping",
        "Credit Crad Due",
        "Investment"
    )
    private var date: String = ""
    private val months = listOf<String>(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "November",
        "December"
    )
    private var _binding: FragmentAddTransactionBinding? = null
    private lateinit var viewModel: TransactionViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

//        setting the viewmodel
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        binding.backButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_addTransaction_to_Stats)
        }

//      setting autoComplete textview for transaction type

        binding.transactionType.setThreshold(3)
        val adapter: ArrayAdapter<String> =ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, category)
        binding.transactionType.setAdapter(adapter)



//        datepicker
        binding.calenderPicker.setOnClickListener {
            datePicker()
        }


//        adding expenses button
        binding.addExpenseBtn.setOnClickListener {
            insertTransaction()
        }



        return binding.root
    }

    //    datePicker function
    private fun datePicker() {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)


        val datepickerdialog: DatePickerDialog = DatePickerDialog(
            activity!!,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                date = "$dayOfMonth ${months.get(monthOfYear)} $year"
                Log.d("checkingDate", "datePicker: $date")
            },
            y,
            m,
            d
        )
        datepickerdialog.show()
    }

//    insert function

    private fun insertTransaction() {
        val tDescription = binding.transactionDescription.text.toString()
        val tAmount = binding.transactionAmount.text.toString()
        val isExpense = true
        val transactionType = binding.transactionType.text.toString()
//        logic left to be written for remaining Amount
        val remainingAmount = 10000
        if (check(tDescription, tAmount, transactionType, date)) {
            viewModel.insertTransaction(
                Transaction(
                    0,
                    tDescription,
                    tAmount.toFloat(),
                    isExpense,
                    date,
                    transactionType,
                    remainingAmount.toFloat()
                )
            )
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).navigate(R.id.action_addTransaction_to_Stats)
        } else {
            Log.d("checkingDate", "insertTransaction: empty space")
            if (date.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Click on Calender to choose date",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(requireContext(), "Fields cant be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun check(
        tDescription: String,
        tAmount: String,
        transactionType: String,
        date: String
    ): Boolean {
        Log.d("checkingDate", "check: INside check function")
        if (tDescription.isBlank() || tAmount.isBlank() || transactionType.isBlank() || date.isBlank()) {
            return false
        }
        return true
    }
}
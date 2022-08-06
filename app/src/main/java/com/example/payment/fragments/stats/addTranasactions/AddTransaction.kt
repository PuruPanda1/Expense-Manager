package com.example.payment.fragments.stats.addTranasactions

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.payment.R
import com.example.payment.databinding.FragmentAddTransactionBinding
import com.example.payment.transactionDb.Transaction
import com.example.payment.viewModel.TransactionViewModel


class addTransaction : Fragment() {
    private val args by navArgs<addTransactionArgs>()
    private var day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private var week: Int = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 1
    private var month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1
    private var incomeAmount = 0f
    private var expenseAmount = 0f
    val cal = Calendar.getInstance()
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH)
    val d = cal.get(Calendar.DAY_OF_MONTH)
    private var date: Long = 0L
    private var isExpense = true
    private val expenseCategory = listOf<String>(
        "Credit Card Due",
        "Bills",
        "DineOut",
        "Entertainment",
        "Fuel",
        "Groceries",
        "Income",
        "Salary",
        "Shopping",
        "Stationary",
        "Suspense",
        "Transportation"
    )
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
    ): View {

//        setting the today's date
        cal.set(y, m, d)
        date = cal.timeInMillis
        Log.d("checkingDate", "onCreateView: " + date)

        // Inflate the layout for this fragment
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

//        setting the viewmodel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        binding.backButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_addTransaction_to_Stats)
        }

//        edit mode
        if (args.transaction.id != -1) {
            if (isExpense) {
                binding.transactionAmount.setText(args.transaction.expenseAmount.toString())
            } else {
                binding.transactionAmount.setText(args.transaction.incomeAmount.toString())
            }
            binding.transactionDescription.setText(args.transaction.tDescription)
            binding.transactionType.setText(args.transaction.transactionType)
            date = args.transaction.date
            binding.addExpenseBtn.setOnClickListener {
                updateTransaction()
            }
        } else {
            binding.addExpenseBtn.setOnClickListener {
                insertTransaction()
            }
        }

//      setting autoComplete textview for transaction type

        binding.transactionType.threshold = 3
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            expenseCategory
        )
        binding.transactionType.setAdapter(adapter)


//        datepicker
        binding.calenderPicker.setOnClickListener {
            datePicker()
        }

//        switch for isExpense
        binding.isExpenseSwitch.setOnCheckedChangeListener { _, isChecked ->
            isExpense = isChecked
        }

        binding.transactionType.setOnFocusChangeListener { view, b ->
            if (b) {
                hideSoftKeyboard(view)
            }
        }

        return binding.root
    }

    fun hideSoftKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun updateTransaction() {
        val tDescription = binding.transactionDescription.text.toString()
        val tAmount = binding.transactionAmount.text.toString()
        val transactionType = binding.transactionType.text.toString()
        if (check(tDescription, tAmount, transactionType, date)) {
            if (isExpense) {
                expenseAmount = tAmount.toFloat()
            } else {
                incomeAmount = tAmount.toFloat()
            }
            viewModel.updateTransaction(
                Transaction(
                    args.transaction.id,
                    tDescription,
                    incomeAmount,
                    isExpense,
                    date,
                    transactionType,
                    day,
                    week,
                    month,
                    expenseAmount
                )
            )
            Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).navigate(R.id.action_addTransaction_to_Stats)
        } else {
            if (date == 0L) {
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

    //    datePicker function
    private fun datePicker() {
        val datepickerdialog: DatePickerDialog = DatePickerDialog(
            requireActivity(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                cal.set(year, monthOfYear, dayOfMonth)
                date = cal.timeInMillis
                day = d
                week = cal.get(Calendar.WEEK_OF_YEAR) - 1
                month = cal.get(Calendar.MONTH) + 1
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
        val transactionType = binding.transactionType.text.toString()
//        logic left to be written for remaining Amount
        if (check(tDescription, tAmount, transactionType, date)) {
            if (isExpense) {
                expenseAmount = tAmount.toFloat()
            } else {
                incomeAmount = tAmount.toFloat()
            }
            viewModel.insertTransaction(
                Transaction(
                    0,
                    tDescription,
                    incomeAmount,
                    isExpense,
                    date,
                    transactionType,
                    day,
                    week,
                    month,
                    expenseAmount
                )
            )
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(binding.root).navigate(R.id.action_addTransaction_to_Stats)
        } else {
            if (date == 0L) {
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
        date: Long
    ): Boolean {
        if (tDescription.isBlank() || tAmount.isBlank() || transactionType.isBlank() || date == 0L) {
            return false
        }
        return true
    }
}
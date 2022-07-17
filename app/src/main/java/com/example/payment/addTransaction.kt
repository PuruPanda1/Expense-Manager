package com.example.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.payment.databinding.FragmentAddTransactionBinding
import com.example.payment.db.Transaction

class addTransaction : Fragment() {
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
                .navigate(R.id.action_addTransaction_to_MainScreen)
        }

//        adding expenses button
        binding.addExpenseBtn.setOnClickListener {
            insertTransaction()
        }

        return binding.root
    }

    private fun insertTransaction() {
        val tDescription = binding.transactionDescription.text.toString()
        val tAmount = binding.transactionAmount.text.toString().toFloat()
        val isExpense = true
        val date = ""
        val transactionType = binding.transactionType.text.toString()
//        logic left to be written for remaining Amount
        val remainingAmount = 10000 - tAmount
        viewModel.insertTransaction(
            Transaction(
                0,
                tDescription,
                tAmount,
                isExpense,
                date,
                transactionType,
                remainingAmount
            )
        )
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
    }
}
package com.example.payment.fragments.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.databinding.FragmentStatsBinding
import com.example.payment.rcAdapter.TransactionsAdapter
import com.example.payment.transactionDb.Transaction
import com.example.payment.userDb.UserViewModel
import com.example.payment.viewModel.TransactionViewModel
import com.google.android.material.datepicker.MaterialDatePicker

class Stats : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private lateinit var viewModel: TransactionViewModel
    private lateinit var userViewModel: UserViewModel
    private var currency = MutableLiveData("INR")
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStatsBinding.inflate(inflater, container, false)

//        setting the viewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]


        lateinit var adapter: TransactionsAdapter

//        live data for currency
        userViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                currency.value = it.userCurrency
            }
            adapter = TransactionsAdapter(this, currency.value!!)
            binding.transactionsRC.layoutManager = LinearLayoutManager(requireContext())
            binding.transactionsRC.adapter = adapter
//            adapter = TransactionsAdapter(this,currency.value!!)
        }

//          add new transaction onclick listener
        binding.addBtn.setOnClickListener {
            val action = StatsDirections.actionStatsToAddTransaction(
                Transaction(
                    -1,
                    "",
                    0f,
                    true,
                    0L,
                    "",
                    0,
                    0,
                    0,
                    0f,
                    ""
                )
            )
            Navigation.findNavController(binding.root).navigate(action)
        }

//        setting the custom date option
        binding.selectedDatePicker.setOnClickListener {
            openRangePicker(adapter)
        }

//        income button
        binding.incomeTranscations.setOnClickListener {
            viewModel.readAllTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllExpenseTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllIncomeTransaction.observe(viewLifecycleOwner) {
                adapter.submitList(it) {
                    binding.transactionsRC.post { binding.transactionsRC.smoothScrollToPosition(0) }
                }
            }
        }

//        expense button
        binding.expenseTransactions.setOnClickListener {
            viewModel.readAllTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllIncomeTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllExpenseTransaction.observe(viewLifecycleOwner) {
                adapter.submitList(it) {
                    binding.transactionsRC.post { binding.transactionsRC.smoothScrollToPosition(0) }
                }
            }
        }

//        all button
        binding.allTransactions.setOnClickListener {
            viewModel.readAllExpenseTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllIncomeTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllTransactionDate.removeObservers(viewLifecycleOwner)
            viewModel.readAllTransaction.observe(viewLifecycleOwner) {
                adapter.submitList(it) {
                    binding.transactionsRC.post { binding.transactionsRC.smoothScrollToPosition(0) }
                }
            }
        }

//        setting the observer
        viewModel.readAllTransaction.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                binding.transactionsRC.post { binding.transactionsRC.smoothScrollToPosition(0) }
            }
        }

        return binding.root
    }

    private fun openRangePicker(adapter: TransactionsAdapter) {
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Choose duration")
            .build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "datepicker")

        dateRangePicker.addOnPositiveButtonClickListener {
            viewModel.setCustomDurationData(listOf(it.first, it.second + 86400000))
        }
        viewModel.readAllTransaction.removeObservers(viewLifecycleOwner)
        //        calender icon
        viewModel.readAllTransactionDate.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }


    fun deleteTransaction(transaction: Transaction) {
        viewModel.deleteTransaction(transaction)
        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
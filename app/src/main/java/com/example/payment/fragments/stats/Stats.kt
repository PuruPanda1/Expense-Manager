package com.example.payment.fragments.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.viewModel.TransactionViewModel
import com.example.payment.rcAdapter.TransactionsAdapter
import com.example.payment.databinding.FragmentStatsBinding
import com.example.payment.transactionDb.Transaction

class Stats : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private lateinit var viewModel: TransactionViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatsBinding.inflate(inflater, container, false)

//        setting the viewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        val adapter = TransactionsAdapter(this)
        binding.transactionsRC.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsRC.adapter = adapter

//          add new transaction onclick listener
        binding.addBtn.setOnClickListener {
            val action = StatsDirections.actionStatsToAddTransaction(Transaction(-1, "", 0f, true,0L,"",0,0,0,0f))
            Navigation.findNavController(binding.root).navigate(action)
        }

//        income button
        binding.incomeTranscations.setOnClickListener {
            viewModel.readAllTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllExpenseTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllIncomeTransaction.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }

//        expense button
        binding.expenseTransactions.setOnClickListener{
            viewModel.readAllTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllIncomeTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllExpenseTransaction.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }

//        all button
        binding.allTransactions.setOnClickListener{
            viewModel.readAllExpenseTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllIncomeTransaction.removeObservers(viewLifecycleOwner)
            viewModel.readAllTransaction.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }

//        setting the observer
        viewModel.readAllTransaction.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        return binding.root
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
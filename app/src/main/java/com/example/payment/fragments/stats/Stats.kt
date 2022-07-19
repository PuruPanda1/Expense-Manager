package com.example.payment.fragments.stats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.R
import com.example.payment.TransactionViewModel
import com.example.payment.TransactionsAdapter
import com.example.payment.databinding.FragmentStatsBinding
import com.example.payment.db.Transaction

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

        val adapter = TransactionsAdapter(this)
        binding.transactionsRC.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsRC.adapter = adapter

//          add new transaction onclick listener
        binding.addBtn.setOnClickListener {
            val action = StatsDirections.actionStatsToAddTransaction(Transaction(-1, "", 0f, true,0L,"",0f))
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

//        setting the viewModel
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
//        setting the observer
        viewModel.readAllTransaction.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        return binding.root
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModel.deleteTransaction(transaction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
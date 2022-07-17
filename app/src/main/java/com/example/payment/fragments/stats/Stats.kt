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

class Stats : Fragment() {
    private var _binding:FragmentStatsBinding? = null
    private lateinit var viewModel:TransactionViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStatsBinding.inflate(inflater, container, false)

        val adapter = TransactionsAdapter()
        binding.transactionsRC.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsRC.adapter = adapter

//          add new transaction onclick listener
        binding.addBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_Stats_to_addTransaction)
        }

//        income button
        binding.incomeTranscations.setOnClickListener{
            viewModel.changeToIncome()
        }

//        setting the viewModel
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
//        setting the observer
        viewModel.readAllTransaction.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            Log.d("stats", "onCreateView: "+it[0].tAmount)
        })

        return binding.root
    }

}
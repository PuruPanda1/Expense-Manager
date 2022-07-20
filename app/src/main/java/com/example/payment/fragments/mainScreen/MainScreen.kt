package com.example.payment.fragments.mainScreen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.payment.R
import com.example.payment.TransactionViewModel
import com.example.payment.databinding.FragmentMainScreenBinding
import java.util.*
import kotlin.math.exp

class MainScreen : Fragment() {
    private var _binding: FragmentMainScreenBinding? = null
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
    private lateinit var viewModel: TransactionViewModel
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)

//        setting viewModel
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

//        set balance amount
        viewModel.readDifferenceSum.observe(viewLifecycleOwner, Observer {
            binding.balanceAmount.text = String.format(getString(R.string.amountInRupee,it.toString()))
        })

//        set monthly balance amount
        val month = months[Calendar.getInstance().get(Calendar.MONTH)]
        binding.monthlyDuration.text = String.format(getString(R.string.monthlyDuration,month))
        viewModel.readDifferenceSumMonthly.observe(viewLifecycleOwner, Observer {
            binding.monthlyExpense.text = String.format(getString(R.string.amountInRupee,it.toString()))
        })



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
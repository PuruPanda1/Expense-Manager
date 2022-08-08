package com.example.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.databinding.FragmentDetailedCategoryTransactionsBinding
import com.example.payment.rcAdapter.TransactionsCategoryWiseAdapter
import com.example.payment.transactionDb.Transaction
import com.example.payment.viewModel.TransactionTypeData
import com.example.payment.viewModel.TransactionViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class DetailedCategoryTransactionsFragment : Fragment() {
    private val months = listOf(
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
    private var _binding: FragmentDetailedCategoryTransactionsBinding? = null
    private val args by navArgs<DetailedCategoryTransactionsFragmentArgs>()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentDetailedCategoryTransactionsBinding.inflate(layoutInflater, container, false)

//        setting the back button
        binding.backToSummary.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_detailedCategoryTransactionsFragment_to_detailedTransactionAnalysis)
        }

//        onclick for the calender icon
        binding.categoryRangePicker.setOnClickListener {
            openRangePicker()
        }

//        setting the month
        if (args.startDate == 0L) {
            val month = months[Calendar.getInstance().get(Calendar.MONTH)]
            binding.categoryDuration.text =
                String.format(getString(R.string.monthlyDuration, month))
        } else {
            val simpleDateFormat = SimpleDateFormat("dd MMM YY")
            binding.categoryDuration.text = getString(
                R.string.monthlyDuration,
                "${simpleDateFormat.format(args.startDate)} - ${simpleDateFormat.format(args.endDate)}"
            )
        }


        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        binding.categoryName.text = args.categoryName

        val adapter = TransactionsCategoryWiseAdapter(this)
        binding.categoryTransactions.adapter = adapter
        binding.categoryTransactions.layoutManager = LinearLayoutManager(requireContext())

        viewModel.setTransactionTypeData(
            TransactionTypeData(
                args.categoryName,
                args.startDate,
                args.endDate
            )
        )

        viewModel.readSingleTransactionType.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.categoryTotalAmount.text =
                    getString(R.string.totalValue, it.amount.toString())
                if (it.count > 1) {
                    binding.categoryNumberOfExpenses.text =
                        getString(R.string.transactionTypeCount, it.count)
                } else {
                    binding.categoryNumberOfExpenses.text =
                        getString(R.string.transactionTypeCountSingular, it.count)
                }
            } else {
                binding.categoryTotalAmount.text = getString(R.string.totalValue, "0")
                binding.categoryNumberOfExpenses.text = getString(R.string.transactionTypeCount, 0)
            }
        }
        viewModel.transactionsCategoryWise.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }

        binding.categoryTransactions.adapter

        return binding.root
    }

    private fun openRangePicker() {
        val simpleDateFormat = SimpleDateFormat("dd MMM YY")
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Choose duration")
            .build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "datepicker")

        dateRangePicker.addOnPositiveButtonClickListener {
            Log.d("checkingDate", "openRangePicker: ${it.first} and ${it.second}")
            viewModel.setTransactionTypeData(
                TransactionTypeData(
                    args.categoryName,
                    it.first,
                    (it.second+86400000)
                )
            )
        }
        viewModel.sumAccordingToDate.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.categoryTotalAmount.text =
                    String.format(getString(R.string.amountInRupee, "0"))
            } else {
                binding.categoryTotalAmount.text =
                    String.format(getString(R.string.amountInRupee, it.toString()))
            }
        }
//        setting the duration in the card view
        viewModel.transactionTypeDetails.observe(viewLifecycleOwner) {
            binding.categoryDuration.text = getString(
                R.string.monthlyDuration,
                "${simpleDateFormat.format(it.startDate)} - ${simpleDateFormat.format(it.endDate-86400000)}"
            )
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
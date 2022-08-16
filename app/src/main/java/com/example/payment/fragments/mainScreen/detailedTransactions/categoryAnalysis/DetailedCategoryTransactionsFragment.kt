package com.example.payment.fragments.mainScreen.detailedTransactions.categoryAnalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.R
import com.example.payment.databinding.FragmentDetailedCategoryTransactionsBinding
import com.example.payment.rcAdapter.TransactionsCategoryWiseAdapter
import com.example.payment.transactionDb.Transaction
import com.example.payment.userDb.UserViewModel
import com.example.payment.viewModel.TransactionTypeData
import com.example.payment.viewModel.TransactionViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.NumberFormat
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
    private lateinit var userViewModel: UserViewModel
    private var _binding: FragmentDetailedCategoryTransactionsBinding? = null
    private val args by navArgs<DetailedCategoryTransactionsFragmentArgs>()
    private val binding get() = _binding!!
    private val currency = MutableLiveData("INR")
    private val currencyFormatter = NumberFormat.getCurrencyInstance()
    private var amount = 0f
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

//        setting the default currency formater
        currencyFormatter.maximumFractionDigits = 1
        currencyFormatter.currency = Currency.getInstance(currency.value)

//        observer for currency change
        lateinit var adapter : TransactionsCategoryWiseAdapter
        currency.observe(viewLifecycleOwner) {
            currencyFormatter.currency = Currency.getInstance(it!!)
            adapter = TransactionsCategoryWiseAdapter(this, it)
            binding.categoryTransactions.adapter = adapter
            binding.categoryTransactions.layoutManager = LinearLayoutManager(requireContext())
            setAmount()
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
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding.categoryName.text = args.categoryName

//        setting the adapter with the user viewModel for the currency
        userViewModel.userDetails.observe(viewLifecycleOwner){
            if(it!=null){
                currency.value = it.userCurrency
            }
        }



        viewModel.setTransactionTypeData(
            TransactionTypeData(
                args.categoryName,
                args.startDate,
                args.endDate
            )
        )

        viewModel.readSingleTransactionType.observe(viewLifecycleOwner) {
            if (it != null) {
                amount = it.amount
                if (it.count > 1) {
                    binding.categoryNumberOfExpenses.text =
                        getString(R.string.transactionTypeCount, it.count)
                } else {
                    binding.categoryNumberOfExpenses.text =
                        getString(R.string.transactionTypeCountSingular, it.count)
                }
            } else {
                amount = 0f
                binding.categoryNumberOfExpenses.text = getString(R.string.transactionTypeCount, 0)
            }
            setAmount()
        }
        viewModel.transactionsCategoryWise.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.categoryTransactions.adapter

        return binding.root
    }

    private fun setAmount() {
        binding.categoryTotalAmount.text = "Total: ${currencyFormatter.format(amount)}"
    }

    private fun openRangePicker() {
        val simpleDateFormat = SimpleDateFormat("dd MMM YY")
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Choose duration")
            .build()

        dateRangePicker.show(requireActivity().supportFragmentManager, "datepicker")

        dateRangePicker.addOnPositiveButtonClickListener {
            viewModel.setTransactionTypeData(
                TransactionTypeData(
                    args.categoryName,
                    it.first,
                    (it.second + 86400000)
                )
            )
        }
        viewModel.sumAccordingToDate.observe(viewLifecycleOwner) {
            amount = it ?: 0f
            setAmount()
        }
//        setting the duration in the card view
        viewModel.transactionTypeDetails.observe(viewLifecycleOwner) {
            binding.categoryDuration.text = getString(
                R.string.monthlyDuration,
                "${simpleDateFormat.format(it.startDate)} - ${simpleDateFormat.format(it.endDate - 86400000)}"
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
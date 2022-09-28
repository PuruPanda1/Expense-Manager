package com.purabmodi.payment.fragments.profile.detailedView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.purabmodi.payment.R
import com.purabmodi.payment.databinding.FragmentDetailedViewBinding
import com.purabmodi.payment.rcAdapter.IncomeExpenseTransactionsAdapter
import com.purabmodi.payment.viewModel.TransactionViewModel
import java.text.NumberFormat
import java.util.*


class DetailedView : Fragment() {
    private var _binding: FragmentDetailedViewBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailedViewArgs>()
    private lateinit var transactionViewModel: TransactionViewModel
    private val currencyFormatter = NumberFormat.getCurrencyInstance()
    private var months = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    private var currencyFormater = NumberFormat.getCurrencyInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedViewBinding.inflate(layoutInflater, container, false)

        transactionViewModel =
            ViewModelProvider(requireActivity())[(TransactionViewModel::class.java)]
        transactionViewModel.setDayOfYear(
            listOf(
                Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
                Calendar.getInstance().get(Calendar.YEAR)
            )
        )

        currencyFormatter.maximumFractionDigits = 1
        currencyFormatter.currency = Currency.getInstance(args.currency)

        currencyFormater.currency = Currency.getInstance(args.currency)
        currencyFormater.maximumFractionDigits = 1

        var dayOfYear = 0
        var year = 0
        transactionViewModel.dayOfYear.observe(viewLifecycleOwner) {
            dayOfYear = it[0]
            year = it[1]
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_YEAR, it[0])
            cal.set(Calendar.YEAR, it[1])
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val month = cal.get(Calendar.MONTH)
            val currentDay: String
            currentDay = when (day % 10) {
                1 -> {
                    "${day}st ${months[month]}"
                }
                2 -> {
                    "${day}nd ${months[month]}"
                }
                3 -> {
                    "${day}rd ${months[month]}"
                }
                else -> {
                    "${day}th ${months[month]}"
                }
            }
            binding.currentDay.text = currentDay
        }

        transactionViewModel.readIncomeByDay.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.incomeTotal.text = currencyFormatter.format(0)
            } else {
                binding.incomeTotal.text = currencyFormatter.format(it)
            }
        }

        transactionViewModel.readExpenseByDay.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.expenseTotal.text = currencyFormatter.format(0)
            } else {
                binding.expenseTotal.text = currencyFormatter.format(it)
            }
        }

        val adapter = IncomeExpenseTransactionsAdapter(args.currency)
        binding.transactionsRC.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionsRC.adapter = adapter





        transactionViewModel.readTransactionsByDay.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                Log.d("checking", "onCreateView: " + it[0].tDescription)
            }
            adapter.submitList(it)
        }

        binding.backButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_detailedView_to_Profile)
        }

        binding.nextDay.setOnClickListener {
            transactionViewModel.setDayOfYear(
                listOf(
                    ++dayOfYear,
                    year
                )
            )
        }

        binding.previousDay.setOnClickListener {
            transactionViewModel.setDayOfYear(
                listOf(
                    --dayOfYear,
                    year
                )
            )
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.payment.fragments.mainScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.R
import com.example.payment.databinding.FragmentIncomeExpenseViewBinding
import com.example.payment.rcAdapter.IncomeExpenseTransactionsAdapter
import com.example.payment.viewModel.TransactionViewModel
import java.text.NumberFormat
import java.util.*


class IncomeExpenseView : Fragment() {
    private var _binding: FragmentIncomeExpenseViewBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<IncomeExpenseViewArgs>()
    private lateinit var transactionsViewModel: TransactionViewModel
    private var currencyFormater = NumberFormat.getCurrencyInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomeExpenseViewBinding.inflate(layoutInflater, container, false)
        transactionsViewModel =
            ViewModelProvider(requireActivity())[TransactionViewModel::class.java]

        currencyFormater.currency = Currency.getInstance(args.currency)
        currencyFormater.maximumFractionDigits = 1

        binding.typeName.text = args.typeName
        binding.transactionDuration.text = "Transactions in ${args.month}"
        binding.transactionTotalAmount.text = currencyFormater.format(0)
        binding.numberOfTransactions.text = "0 Transactions"
        val adapter = IncomeExpenseTransactionsAdapter(args.currency)
        binding.incomeExpenseTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.incomeExpenseTransactions.adapter = adapter
        when (args.typeName) {
            "INCOME" -> {
                transactionsViewModel.incomeSum.observe(viewLifecycleOwner) {
                    if (it != null) {
                        binding.transactionTotalAmount.text = currencyFormater.format(it)
                    }
                }
                transactionsViewModel.readAllIncomeTransaction.observe(viewLifecycleOwner) {
                    if (it != null) {
                        adapter.submitList(it)
                        when (it.size) {
                            1 -> binding.numberOfTransactions.text = "1 Transaction"
                            else -> binding.numberOfTransactions.text = "${it.size} Transactions"
                        }
                    }
                }
            }
            else -> {
                transactionsViewModel.expenseSum.observe(viewLifecycleOwner) {
                    if (it != null) {
                        binding.transactionTotalAmount.text = currencyFormater.format(it)
                    }
                }
                transactionsViewModel.readAllExpenseTransaction.observe(viewLifecycleOwner) {
                    if (it != null) {
                        adapter.submitList(it)
                        when (it.size) {
                            1 -> binding.numberOfTransactions.text = "1 Transaction"
                            else -> binding.numberOfTransactions.text = "${it.size} Transactions"
                        }
                    }
                }
            }
        }
        binding.backToMainScreen.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_incomeExpenseView_to_MainScreen)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
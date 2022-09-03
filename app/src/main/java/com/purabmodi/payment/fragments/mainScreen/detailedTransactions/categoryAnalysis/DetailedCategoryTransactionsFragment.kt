package com.purabmodi.payment.fragments.mainScreen.detailedTransactions.categoryAnalysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.purabmodi.payment.MainActivity
import com.purabmodi.payment.R
import com.purabmodi.payment.databinding.FragmentDetailedCategoryTransactionsBinding
import com.purabmodi.payment.rcAdapter.TransactionsCategoryWiseAdapter
import com.purabmodi.payment.transactionDb.Transaction
import com.purabmodi.payment.userDb.UserViewModel
import com.purabmodi.payment.viewModel.TransactionTypeData
import com.purabmodi.payment.viewModel.TransactionViewModel
import java.text.NumberFormat
import java.util.*

class DetailedCategoryTransactionsFragment : Fragment() {
    private val months = listOf(
        "Jan",
        "Feb",
        "Mar",
        "Apr",
        "May",
        "Jun",
        "Jul",
        "Aug",
        "Sep",
        "Oct",
        "Nov",
        "Dec"
    )
    private lateinit var transactionViewModel: TransactionViewModel
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

        transactionViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

//        setting the back button
        binding.backToSummary.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_detailedCategoryTransactionsFragment_to_detailedTransactionAnalysis)
        }

//        setting the default currency formater
        currencyFormatter.maximumFractionDigits = 1
        currencyFormatter.currency = Currency.getInstance(currency.value)

//        observer for currency change
        lateinit var adapter: TransactionsCategoryWiseAdapter
        currency.observe(viewLifecycleOwner) {
            currencyFormatter.currency = Currency.getInstance(it!!)
            adapter = TransactionsCategoryWiseAdapter(this, it)
            binding.categoryTransactions.adapter = adapter
            binding.categoryTransactions.layoutManager = LinearLayoutManager(requireContext())
            setAmount()
        }


        binding.categoryName.text = args.categoryName

//        setting the adapter with the user viewModel for the currency
        userViewModel.userDetails.observe(viewLifecycleOwner){
            if(it!=null){
                currency.value = it.userCurrency
            }
        }

        transactionViewModel.setTransactionTypeData(
            TransactionTypeData(
                args.categoryName,
                args.startDate,
                args.endDate
            )
        )

        transactionViewModel.readSingleTransactionType.observe(viewLifecycleOwner) {
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
        transactionViewModel.readTransactionsByCategory.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.categoryTransactions.adapter

        return binding.root
    }

    private fun setAmount() {
        binding.categoryTotalAmount.text = "Total: ${currencyFormatter.format(amount)}"
    }

    fun deleteTransaction(transaction: Transaction) {
        transactionViewModel.deleteTransaction(transaction)
        Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
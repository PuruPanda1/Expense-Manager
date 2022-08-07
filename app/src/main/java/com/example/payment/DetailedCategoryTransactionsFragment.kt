package com.example.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.databinding.FragmentDetailedCategoryTransactionsBinding
import com.example.payment.rcAdapter.TransactionsCategoryWiseAdapter
import com.example.payment.transactionDb.Transaction
import com.example.payment.viewModel.TransactionTypeData
import com.example.payment.viewModel.TransactionViewModel

class DetailedCategoryTransactionsFragment : Fragment() {
    private lateinit var viewModel : TransactionViewModel
    private var _binding : FragmentDetailedCategoryTransactionsBinding? = null
    private val args by navArgs<DetailedCategoryTransactionsFragmentArgs>()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailedCategoryTransactionsBinding.inflate(layoutInflater,container,false)

        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        binding.categoryName.text = args.categoryName

        val adapter = TransactionsCategoryWiseAdapter(this)
        binding.categoryTransactions.adapter = adapter
        binding.categoryTransactions.layoutManager = LinearLayoutManager(requireContext())

        viewModel.setTransactionTypeData(TransactionTypeData(args.categoryName,args.startDate,args.endDate))

        viewModel.readSingleTransactionType.observe(viewLifecycleOwner){
            binding.categoryTotalAmount.text = getString(R.string.totalValue,it.amount.toString())
            if(it.count>1){
                binding.categoryNumberOfExpenses.text = getString(R.string.transactionTypeCount,it.count)
            } else {
                binding.categoryNumberOfExpenses.text = getString(R.string.transactionTypeCountSingular,it.count)
            }
        }
        viewModel.transactionsCategoryWise.observe(viewLifecycleOwner){
            adapter.setData(it)
        }

        binding.categoryTransactions.adapter

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
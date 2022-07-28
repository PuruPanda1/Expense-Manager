package com.example.payment.fragments.wallet.cryptoPortfolio

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.R
import com.example.payment.apiModules.CryptoRepository
import com.example.payment.cryptoDb.CryptoTransaction
import com.example.payment.cryptoDb.CryptoViewModel
import com.example.payment.databinding.FragmentCryptoPortfolioBinding
import com.example.payment.rcAdapter.CryptoPortfolioAdapter
import com.example.payment.viewModel.ApiViewModel
import com.example.payment.viewModel.TransactionViewModel
import com.example.payment.viewModelFactory.ApiViewModelFactory

class CryptoPortfolio : Fragment() {
    private lateinit var viewModel: CryptoViewModel
    private lateinit var viewModel2: ApiViewModel
    private var _binding: FragmentCryptoPortfolioBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCryptoPortfolioBinding.inflate(layoutInflater, container, false)

        binding.backToWallet.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_cryptoPortfolio_to_Wallet)
        }

        binding.addCryptoTransactionFloatingBtn.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_cryptoPortfolio_to_cryptoListFragment)
        }

        viewModel = ViewModelProvider(requireActivity())[CryptoViewModel::class.java]

        viewModel.getCryptoData()

//        setting the total amount
        viewModel.totalAmount.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.cryptoPortfolioTotalAmount.text = "₹0"
            } else {
                val number3digits = Math.round(it * 1000.0) / 1000.0
                val number2digits = Math.round(number3digits * 100.0) / 100.0
                val amount = Math.round(number2digits * 10.0) / 10.0
                binding.cryptoPortfolioTotalAmount.text = "₹${amount}"
            }
        }

//        setting the profit/loss amount
        viewModel.profitLoss.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.changesInAmount.text = "₹0"
                binding.changesInAmount.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.income_color
                    )
                )

            } else {
                if (it >= 0) {
                    binding.changesInAmount.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.income_color
                        )
                    )
                } else {
                    binding.changesInAmount.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.expense_color
                        )
                    )
                }
                val number3digits = Math.round(it * 1000.0) / 1000.0
                val number2digits = Math.round(number3digits * 100.0) / 100.0
                val pl = Math.round(number2digits * 10.0) / 10.0
                binding.changesInAmount.text = "₹${pl}"
            }
        }

        val adapter = CryptoPortfolioAdapter(this)
        binding.cryptoPortfolioRC.layoutManager = LinearLayoutManager(requireContext())
        binding.cryptoPortfolioRC.adapter = adapter

        viewModel.cryptoData.observe(viewLifecycleOwner) {
            adapter.setData(it)
            it.forEach {
                Log.d("checkingPrice", "onCreateView: " + it.name)
                Log.d("checkingPrice", "onCreateView: " + it.numberOfCoins)
                Log.d("checkingPrice", "onCreateView: " + it.buyingPrice)
                Log.d("checkingPrice", "onCreateView: " + it.currentPrice)
            }
        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun deleteCryptoTransaction(cryptoTransaction: CryptoTransaction) {
        viewModel.deleteCryptoTransaction(cryptoTransaction)
    }
}
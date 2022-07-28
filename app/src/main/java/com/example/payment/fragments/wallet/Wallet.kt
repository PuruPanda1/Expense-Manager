package com.example.payment.fragments.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.payment.R
import com.example.payment.cryptoDb.CryptoViewModel
import com.example.payment.databinding.FragmentWalletBinding
import java.util.zip.Inflater

class Wallet : Fragment() {
    private lateinit var viewModel:CryptoViewModel
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWalletBinding.inflate(layoutInflater,container,false)

        binding.cryptoCardView.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_Wallet_to_cryptoTransactionList)
        }

        viewModel = ViewModelProvider(requireActivity())[CryptoViewModel::class.java]

        viewModel.getTotalAmount()

        viewModel.totalAmount.observe(viewLifecycleOwner){
            if (it == null) {
                binding.balanceAmount1.text = "₹0"
            } else {
                val number3digits = Math.round(it * 1000.0) / 1000.0
                val number2digits = Math.round(number3digits * 100.0) / 100.0
                val amount = Math.round(number2digits * 10.0) / 10.0
                binding.balanceAmount1.text = "₹${amount}"
            }
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.payment.fragments.wallet.cryptoPortfolio

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.payment.R
import com.example.payment.databinding.FragmentCryptoPortfolioBinding

class CryptoPortfolio : Fragment() {
    private var _binding:FragmentCryptoPortfolioBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCryptoPortfolioBinding.inflate(layoutInflater,container,false)

        binding.backToWallet.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_cryptoPortfolio_to_Wallet)
        }

        binding.addCryptoTransactionFloatingBtn.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_cryptoPortfolio_to_cryptoListFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
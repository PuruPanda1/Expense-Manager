package com.example.payment.fragments.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.payment.R
import com.example.payment.databinding.FragmentWalletBinding
import java.util.zip.Inflater

class Wallet : Fragment() {
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

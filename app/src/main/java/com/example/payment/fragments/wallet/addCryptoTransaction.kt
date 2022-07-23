package com.example.payment.fragments.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.payment.R
import com.example.payment.databinding.FragmentAddCryptoTransactionBinding
import com.example.payment.databinding.FragmentAddTransactionBinding

class addCryptoTransaction : Fragment() {
    private var _binding: FragmentAddCryptoTransactionBinding? = null
    private val args by navArgs<addCryptoTransactionArgs>()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddCryptoTransactionBinding.inflate(layoutInflater, container, false)

//        back button working
        binding.backToCryptoList.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_addCryptoTransaction_to_cryptoListFragment)
        }


        binding.cryptoName.text = args.cryptoName
        val moneyString = String.format(resources.getString(R.string.amountInRupee,args.cryptoPrice.toString()))
        binding.cryptoPrice.text = moneyString
//        val id = args.cryptoId

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
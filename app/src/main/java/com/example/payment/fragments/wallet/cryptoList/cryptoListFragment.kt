package com.example.payment.fragments.wallet.cryptoList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.ApiViewModel
import com.example.payment.ApiViewModelFactory
import com.example.payment.CryptoAdapter
import com.example.payment.R
import com.example.payment.apiModules.CryptoRepository
import com.example.payment.databinding.FragmentCryptoListBinding
import kotlin.math.log

class cryptoListFragment : Fragment() {
    private var _binding:FragmentCryptoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel:ApiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCryptoListBinding.inflate(layoutInflater,container,false)

//        setting search button
        binding.cryptoSearchIcon.setOnClickListener {
            binding.cryptoSearchIcon.isVisible = false
            binding.headerName.isVisible = false
            binding.textInputLayout2.isVisible = true
        }

//        setting the back button
        binding.backToWalletButton.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_cryptoListFragment_to_Wallet)
        }

        val repository = CryptoRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory)[ApiViewModel::class.java]

        viewModel.getTrendingList()
//        setting the crypto list
        val adapter = CryptoAdapter()
        binding.cryptoListRc.layoutManager = LinearLayoutManager(requireContext())
        binding.cryptoListRc.adapter = adapter

        viewModel.coinList.observe(viewLifecycleOwner, Observer {
            if(it.isSuccessful){
                adapter.setData(it.body()!!)
            }
            else{
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
        })


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
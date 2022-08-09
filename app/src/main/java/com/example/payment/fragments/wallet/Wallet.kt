package com.example.payment.fragments.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.payment.databinding.FragmentWalletBinding
import com.example.payment.rcAdapter.AccountsAdapter
import com.example.payment.rcAdapter.WalletAccountDetailsAdapter
import com.example.payment.viewModel.TransactionViewModel

class Wallet : Fragment() {
    private lateinit var transactionViewModel : TransactionViewModel
    private var _binding: FragmentWalletBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWalletBinding.inflate(layoutInflater,container,false)

        transactionViewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        val adapter = WalletAccountDetailsAdapter()
        binding.walletAccountsRecyclerView.adapter = adapter
        binding.walletAccountsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        transactionViewModel.readAccountDetails.observe(viewLifecycleOwner){
            adapter.setData(it)
        }

        binding.addAccount.setOnClickListener {

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

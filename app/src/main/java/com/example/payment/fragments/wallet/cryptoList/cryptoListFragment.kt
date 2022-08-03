package com.example.payment.fragments.wallet.cryptoList

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.payment.R
import com.example.payment.apiModules.CryptoRepository
import com.example.payment.databinding.FragmentCryptoListBinding
import com.example.payment.model.coin
import com.example.payment.rcAdapter.CryptoAdapter
import com.example.payment.viewModel.ApiViewModel
import com.example.payment.viewModelFactory.ApiViewModelFactory


class cryptoListFragment : Fragment() {
    private var _binding: FragmentCryptoListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ApiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCryptoListBinding.inflate(layoutInflater, container, false)

//        setting search button
        binding.cryptoSearchIcon.setOnClickListener {
            binding.cryptoSearchIcon.isVisible = false
            binding.headerName.isVisible = false
            binding.textInputLayout2.isVisible = true
        }

//      setting the onclick listener for search icon in the input text field
        binding.textInputLayout2.setEndIconOnClickListener {
            searchedCoin()
        }

//        setting the showAll button
        binding.showAllButton.setOnClickListener {
            showAllButton()
        }

//        setting the back button
        binding.backToCryptoPortfolio.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_cryptoListFragment_to_cryptoTransactionList)
        }

//        setting the viewModel and calling it
        val repository = CryptoRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        viewModel.getTrendingList()
//        setting the trending crypto list
        val adapter = CryptoAdapter()
        binding.cryptoListRc.layoutManager = LinearLayoutManager(requireContext())
        binding.cryptoListRc.adapter = adapter
//        setting the list in the adapter
        viewModel.coinList.observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                adapter.setData(it.body()!!)
            } else {
                Toast.makeText(requireContext(), "Failed to fetch the list", Toast.LENGTH_SHORT)
                    .show()
            }
        })


        return binding.root
    }

    private fun searchedCoin() {
        val searchedCoin = binding.searchCrypto.text.toString().replace(" ", "")
        if (searchedCoin.isBlank()) {
            Toast.makeText(requireContext(), "Enter coin name", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.getCoinDetails(searchedCoin)
            binding.cryptoListRc.isVisible = false
            binding.searchResultView.isVisible = true
            binding.showAllButton.isVisible = true
            viewModel.coin.observe(viewLifecycleOwner) {
                if (it.isSuccessful) {
                    setSearchedCoin(it.body()!!)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to fetch the results",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showAllButton() {
        binding.cryptoListRc.isVisible = true
        binding.searchResultView.isVisible = false
        binding.showAllButton.isVisible = false
        binding.cryptoSearchIcon.isVisible = true
        binding.headerName.isVisible = true
        binding.textInputLayout2.isVisible = false
        viewModel.coin.removeObservers(viewLifecycleOwner)
    }

    private fun setSearchedCoin(item: coin) {
        binding.searchCrypto.setText("")
        binding.cryptoNameRC.text = item.name
        val priceString = String.format(
            binding.cryptoNameRC.resources
                .getString(R.string.amountInRupee, item.market_data.current_price.inr.toString())
        )
        binding.cryptoPriceRC.text = priceString
        binding.cryptoSymbolRC.text = item.symbol.uppercase()
        val url = item.image.small
        Glide.with(binding.cryptoImageRC)
            .load(url)
            .into(binding.cryptoImageRC)
//        onclick listener
        binding.searchResultView.setOnClickListener {
            val action =
                cryptoListFragmentDirections.actionCryptoListFragmentToAddCryptoTransaction(
                    item.name,
                    item.market_data.current_price.inr.toFloat(),
                    item.symbol,
                    item.id
                )
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
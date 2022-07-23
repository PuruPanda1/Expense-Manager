package com.example.payment.fragments.wallet.cryptoList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.payment.ApiViewModel
import com.example.payment.ApiViewModelFactory
import com.example.payment.R
import com.example.payment.apiModules.CryptoRepository
import com.example.payment.databinding.FragmentCryptoListBinding

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

        val repository = CryptoRepository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory)[ApiViewModel::class.java]

        var coin:String = "defichain"
        var options: HashMap<String,String> = HashMap()
        options["localization"]="false"
        options["tickers"]="false"
        options["community_data"]="false"
        options["developer_data"]="false"
        options["sparkline"]="false"

        viewModel.getCoinDetails(coin,options)

        viewModel.coinData.observe(viewLifecycleOwner,Observer{
            if(it.isSuccessful){
                Log.d("apiChecking", "onCreateView: "+it.body()!!.market_data.current_price.usd)
            }
            else{
                Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
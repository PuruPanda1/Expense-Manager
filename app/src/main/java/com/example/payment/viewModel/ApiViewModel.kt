package com.example.payment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.payment.apiModules.CryptoRepository
import com.example.payment.model.coin
import com.example.payment.model.trendingCoin
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiViewModel(private val repository: CryptoRepository) : ViewModel() {
    val coinData: MutableLiveData<Response<coin>> = MutableLiveData()
    val coinList: MutableLiveData<Response<List<trendingCoin>>> = MutableLiveData()

    //    returns details about a particular coin
    fun getCoinDetails(coin: String, options: Map<String, String>) {
        viewModelScope.launch {
            val response = repository.getCoinDetails(coin, options)
            coinData.value = response
        }
    }

    fun getTrendingList() {
        viewModelScope.launch {
            val response = repository.getTrendingList()
            coinList.value = response
        }
    }
}
package com.example.payment.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.payment.apiModules.CryptoRepository
import com.example.payment.cryptoDb.CryptoTransaction
import com.example.payment.cryptoDb.CryptoTransactionDatabase
import com.example.payment.model.coin
import com.example.payment.model.trendingCoin
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiViewModel(private val repository: CryptoRepository) : ViewModel() {
    val coinList: MutableLiveData<Response<List<trendingCoin>>> = MutableLiveData()
    val coin: MutableLiveData<Response<coin>> = MutableLiveData()
    //    returns details about a particular coin
    fun getCoinDetails(
        selectedCoin: String
    ) {
        val options = HashMap<String, String>()
        options["localization"] = "false"
        options["tickers"] = "false"
        options["community_data"] = "false"
        options["developer_data"] = "false"
        options["sparkline"] = "false"
        viewModelScope.launch {
            val response = repository.getCoinDetails(selectedCoin, options)
            if (response.isSuccessful && response.body() != null) {
                coin.value = response
            }
        }
    }

    fun getTrendingList() {
        viewModelScope.launch {
            val response = repository.getTrendingList()
            coinList.value = response
        }
    }
}
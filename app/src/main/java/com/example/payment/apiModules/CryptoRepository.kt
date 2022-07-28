package com.example.payment.apiModules

import com.example.payment.model.coin
import com.example.payment.model.trendingCoin
import retrofit2.Response

class CryptoRepository {

    suspend fun getCoinDetails(coin:String,options:Map<String,String>):Response<coin>{
        return RetrofitInstance.api.getCoinDetails(coin,options)
    }

    suspend fun getTrendingList():Response<List<trendingCoin>>{
        return RetrofitInstance.api.getTrendingList()
    }
}
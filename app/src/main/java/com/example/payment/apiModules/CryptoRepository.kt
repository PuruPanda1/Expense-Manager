package com.example.payment.apiModules

import com.example.payment.model.coin
import retrofit2.Response

class CryptoRepository {

    suspend fun getCoinDetails(coin:String,options:Map<String,String>):Response<coin>{
        return RetrofitInstance.api.getCoinDetials(coin,options)
    }
}
package com.example.payment.apiModules

import com.example.payment.model.coin
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface CryptoApi {
    @GET("coins/{coinName}")
    suspend fun getCoinDetials(
        @Path("coinName")coinName:String,
        @QueryMap options:Map<String,String>
    ):Response<coin>
}
package com.example.payment.apiModules

import com.example.payment.apiModules.Constant.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val api : CryptoApi by lazy {
        retrofit.create(CryptoApi::class.java)
    }
}
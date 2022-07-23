package com.example.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.payment.apiModules.CryptoRepository

class ApiViewModelFactory(private val repository: CryptoRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApiViewModel(repository) as T
    }
}
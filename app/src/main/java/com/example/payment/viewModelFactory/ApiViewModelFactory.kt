package com.example.payment.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.payment.apiModules.CryptoRepository
import com.example.payment.viewModel.ApiViewModel

class ApiViewModelFactory(private val repository: CryptoRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ApiViewModel(repository) as T
    }
}
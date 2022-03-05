package com.example.wallet.model.viewmodel.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wallet.model.repository.DataStorePreferenceRepository

class WalletStatisticsViewModelFactory(private val dataStorePreferenceRepository: DataStorePreferenceRepository):
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalletStatisticsViewModel::class.java)) {
            return WalletStatisticsViewModel(dataStorePreferenceRepository) as T
        }
        throw IllegalStateException()
    }
}
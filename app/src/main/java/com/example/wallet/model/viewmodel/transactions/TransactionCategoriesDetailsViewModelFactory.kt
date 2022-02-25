package com.example.wallet.model.viewmodel.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wallet.model.repository.DataStorePreferenceRepository

class TransactionCategoriesDetailsViewModelFactory(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionCategoriesDetailsViewModel::class.java)) {
            return TransactionCategoriesDetailsViewModel(dataStorePreferenceRepository) as T
        }
        throw IllegalStateException()
    }
}
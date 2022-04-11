package com.example.wallet.model.viewmodel.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wallet.model.repository.DataStorePreferenceRepository

class ExpenseCategoriesViewModelFactory(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpanseCategoriesViewModel::class.java)) {
            return ExpanseCategoriesViewModel(dataStorePreferenceRepository) as T
        }
        throw IllegalStateException()
    }
}
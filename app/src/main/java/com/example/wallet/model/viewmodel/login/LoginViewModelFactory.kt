package com.example.wallet.model.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wallet.model.repository.DataStorePreferenceRepository

public class LoginViewModelFactory(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(dataStorePreferenceRepository) as T
        }
        throw IllegalStateException()
    }
}
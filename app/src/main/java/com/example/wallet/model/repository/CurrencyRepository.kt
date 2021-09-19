package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService

class CurrencyRepository(private val service: WalletWebService = WalletWebService()) {

    suspend fun getCurrencies(){

    }
}
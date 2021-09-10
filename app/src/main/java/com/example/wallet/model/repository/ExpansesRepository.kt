package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.AllExpansesResponse

class ExpansesRepository (private val service: WalletWebService = WalletWebService()){
    suspend fun getExpanses(): AllExpansesResponse {
        return service.getExpanses()
    }

}
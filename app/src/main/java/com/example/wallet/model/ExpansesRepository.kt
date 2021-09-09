package com.example.wallet.model

import com.example.wallet.api.WalletWebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExpansesRepository (private val service: WalletWebService = WalletWebService()){
    suspend fun getExpanses(): AllExpansesResponse{
        return service.getExpanses()
    }

}
package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.response.ExpanseCategoriesResponse

class ExpanseCategoriesRepository(private val service: WalletWebService = WalletWebService()) {
    suspend fun getExpanseCategories(): ExpanseCategoriesResponse {
        return service.getExpanseCategories()
    }

}
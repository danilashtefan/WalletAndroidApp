package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.AllExpanseCategoriesResponse

class ExpanseCategoriesRepository(private val service: WalletWebService = WalletWebService()) {
    suspend fun getExpanseCategories(): AllExpanseCategoriesResponse {
        return service.getExpanseCategories()
    }

    suspend fun getCategoryForExpanse(expanseId: Int): AllExpanseCategoriesResponse{
         return service.getCategoryForExpanse(expanseId)
    }

}
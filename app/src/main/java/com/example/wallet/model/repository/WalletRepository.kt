package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.SingleExpanseCategoryResponse

class WalletRepository {
    private val service: WalletWebService = WalletWebService()

    suspend fun getWalletForExpanse(expanseId: Int): SingleExpanseCategoryResponse {
        return service.getCategoryForExpanse(expanseId)
    }
}
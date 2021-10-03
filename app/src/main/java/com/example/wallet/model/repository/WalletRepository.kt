package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.SingleTransactionWalletResponse
import com.example.wallet.model.response.transactions.AllTransactionWalletsResponse

class WalletRepository {
    private val service: WalletWebService = WalletWebService()

    suspend fun getWalletForExpanse(expanseId: Int): SingleTransactionWalletResponse {
        return service.getWalletForExpanse(expanseId)
    }
    suspend fun getWallets(): AllTransactionWalletsResponse{
        return service.getWallets()
    }
}
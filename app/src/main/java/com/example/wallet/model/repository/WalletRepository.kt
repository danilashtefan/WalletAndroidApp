package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.SingleTransactionWalletResponse
import com.example.wallet.model.response.transactions.AllTransactionWalletsResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponse
import com.example.wallet.requests.AddOrEditCategoryRequest
import com.example.wallet.requests.AddOrEditWalletRequest

class WalletRepository {
    private val service: WalletWebService = WalletWebService()

    suspend fun getWalletForExpanse(expanseId: Int): SingleTransactionWalletResponse {
        return service.getWalletForExpanse(expanseId)
    }
    suspend fun getFilteredWallets(authToken: String?): SecondAllWalletsResponse {
        return service.getFilteredWallets(authToken)
    }
    suspend fun getWallets(): AllTransactionWalletsResponse{
        return service.getWallets()
    }

    suspend fun addWalletToDb(walletData: AddOrEditWalletRequest) {
        return service.addWalletToDb(walletData)
    }
}
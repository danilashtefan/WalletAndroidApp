package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.SingleTransactionWalletResponse
import com.example.wallet.model.response.transactions.AllTransactionWalletsResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import com.example.wallet.requests.AddOrEditCategoryRequest
import com.example.wallet.requests.AddOrEditWalletRequest
import java.lang.Exception

object WalletRepository {
    private val service: WalletWebService = WalletWebService()
    var walletFiltered = SecondAllWalletsResponse()


    suspend fun getWalletForExpanse(expanseId: Int): SingleTransactionWalletResponse {
        return service.getWalletForExpanse(expanseId)
    }
    suspend fun getFilteredWallets(authToken: String?): SecondAllWalletsResponse {
        walletFiltered = service.getFilteredWallets(authToken)
        return walletFiltered
    }
    suspend fun getWallets(): AllTransactionWalletsResponse{
        return service.getWallets()
    }

    suspend fun addWalletToDb(walletData: AddOrEditWalletRequest) {
        return service.addWalletToDb(walletData)
    }


    fun getWallet(walletId: Int): SecondAllWalletsResponseItem {

        for (wallet in walletFiltered) {
            if (wallet.id === walletId) {
                return wallet
            }
        }
        throw Exception("No wallet found!")

    }

    suspend fun editWalletInDb(
        id: Int,
        walletData: AddOrEditWalletRequest,
        authToken: String
    ) {
        return service.editWalletInDb(id, walletData, authToken)
    }


}
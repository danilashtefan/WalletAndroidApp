package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.SingleTransactionWalletResponse
import com.example.wallet.model.response.transactions.AllTransactionWalletsResponse
import com.example.wallet.model.response.transactions.SecondAPI.*
import com.example.wallet.requests.AddOrEditCategoryRequest
import com.example.wallet.requests.AddOrEditWalletRequest
import java.lang.Exception

object WalletRepository {
    private val service: WalletWebService = WalletWebService()
    var walletFiltered = SecondAllWalletsResponse()



    suspend fun getWalletForExpanse(authToken: String, expanseId: Int): SingleTransactionWalletResponse {
        return service.getWalletForExpanse(authToken, expanseId)
    }
    suspend fun getFilteredWallets(authToken: String?): SecondAllWalletsResponse {
        walletFiltered = service.getFilteredWallets(authToken)
        return walletFiltered
    }


    suspend fun addWalletToDb(authToken: String, walletData: AddOrEditWalletRequest) {
        return service.addWalletToDb(authToken,walletData)
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

    suspend fun deleteWallet(walletId: Int, authToken: String) {
        return service.deleteWalletFromDb(walletId, authToken)
    }

    suspend fun getTopExpenseWallet(authToken: String?, minDate: String, maxDate: String): TopWalletWithAmountResponse {
        val topExpenseCategory = service.getTopExpenseWallet(authToken, minDate, maxDate)
        return topExpenseCategory
    }

    suspend fun getTopIncomeWallet(authToken: String?, minDate: String, maxDate: String): TopWalletWithAmountResponse {
        val topExpenseCategory = service.getTopIncomeWallet(authToken, minDate, maxDate)
        return topExpenseCategory
    }

    suspend fun getWalletsWithExpenses(authToken: String?, minDate: String, maxDate: String): List<TopWalletWithAmountResponse> {
        val wallets = service.getWalletsWithExpenses(authToken, minDate, maxDate)
        return wallets
    }


}
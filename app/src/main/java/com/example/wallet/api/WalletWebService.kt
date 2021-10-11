package com.example.wallet.api

import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.Expanse
import com.example.wallet.model.response.AllExpanseCategoriesResponse
import com.example.wallet.model.response.SingleExpanseCategoryResponse
import com.example.wallet.model.response.SingleTransactionWalletResponse
import com.example.wallet.model.response.transactions.AllTransactionWalletsResponse
import com.example.wallet.requests.AddOrEditCategoryRequest
import com.example.wallet.requests.AddOrEditTransactionRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class WalletWebService {
    private lateinit var api:WalletAPI

    init{
        val retrofit = Retrofit.Builder()
       .baseUrl("http://192.168.0.17:8080/api/")
            //.baseUrl("http://192.168.1.80:8080/api/")
            //.baseUrl("http://152.66.156.198:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(WalletAPI::class.java)
    }


   suspend fun getExpanses(): AllExpansesResponse {
     return api.getExpanses()
    }

    suspend fun getExpanseCategories():AllExpanseCategoriesResponse{
       return api.getExpanseCategories()
    }

    suspend fun getCategoryForExpanse(expenseId: Int): SingleExpanseCategoryResponse{
        return api.getCategoryForExpanse(expenseId)
    }

    suspend fun getWalletForExpanse(expenseId: Int): SingleTransactionWalletResponse {
        return api.getWalletForExpanse(expenseId)
    }

    suspend fun getWallets(): AllTransactionWalletsResponse{
        return api.getWallets()
    }
    suspend fun updateTransactionInDb(transactionId: Int, transactionData: AddOrEditTransactionRequest):Expanse{
        return api.updateTransactionInDb(transactionId,transactionData)
    }

    suspend fun addTransactionToDb(transactionData: AddOrEditTransactionRequest) {
         return api.addTransactionToDb(transactionData)
    }

    suspend fun addCategoryToDb(categoryData: AddOrEditCategoryRequest) {
        return api.addCategoryToDb(categoryData)
    }

    interface WalletAPI{
        @GET("expanses")
        suspend fun getExpanses(): AllExpansesResponse

        @GET("expanseCategories")
        suspend fun getExpanseCategories():AllExpanseCategoriesResponse

        @GET("expanses/{id}/category")
        suspend fun getCategoryForExpanse(@Path("id") expanseId: Int):SingleExpanseCategoryResponse

        @PATCH("expanses/{id}")
        suspend fun updateTransactionInDb(@Path("id") transactionId: Int, @Body transactionData: AddOrEditTransactionRequest):Expanse

        @GET("expanses/{id}/wallet")
        suspend fun getWalletForExpanse(@Path("id") expanseId: Int):SingleTransactionWalletResponse

        @GET("wallets")
        suspend fun getWallets():AllTransactionWalletsResponse

        @POST("expanses")
        suspend fun addTransactionToDb(@Body transactionData: AddOrEditTransactionRequest)

        @POST("expanseCategories")
        suspend fun addCategoryToDb(@Body categoryData: AddOrEditCategoryRequest)


    }
}
package com.example.wallet.api

import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.response.AllExpanseCategoriesResponse
import com.example.wallet.model.response.SingleExpanseCategoryResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


class WalletWebService {
    private lateinit var api:WalletAPI

    init{
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.17:8080/api/")
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

    suspend fun getCategoryForExpanse(expanseId: Int): SingleExpanseCategoryResponse{
        return api.getCategoryForExpanse(expanseId)
    }

    interface WalletAPI{
        @GET("expanses")
        suspend fun getExpanses(): AllExpansesResponse

        @GET("expanseCategories")
        suspend fun getExpanseCategories():AllExpanseCategoriesResponse

        @GET("expanses/{id}/category")
        suspend fun getCategoryForExpanse(@Path("id") expanseId: Int):SingleExpanseCategoryResponse


    }
}
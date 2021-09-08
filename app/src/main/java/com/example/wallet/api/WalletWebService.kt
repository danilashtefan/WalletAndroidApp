package com.example.wallet.api

import com.example.wallet.model.AllExpansesResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


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


    fun getExpanses(): Call<AllExpansesResponse> {
     return api.getExpanses()
    }

    interface WalletAPI{
        @GET("expanses")
        fun getExpanses(): Call<AllExpansesResponse>
    }
}
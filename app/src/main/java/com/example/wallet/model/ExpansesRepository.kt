package com.example.wallet.model

import com.example.wallet.api.WalletWebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExpansesRepository (private val service: WalletWebService = WalletWebService()){
    fun getExpanses(successCallback: (response:AllExpansesResponse?) -> Unit){
        return service.getExpanses().enqueue(object: Callback<AllExpansesResponse>{
            override fun onResponse(
                call: Call<AllExpansesResponse>,
                response: Response<AllExpansesResponse>
            ) {
                if(response.isSuccessful)
            successCallback(response.body())
            }

            override fun onFailure(call: Call<AllExpansesResponse>, t: Throwable) {
              print("ERROR")
            }
        })
    }

}
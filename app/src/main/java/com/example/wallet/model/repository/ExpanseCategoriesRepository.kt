package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.AllExpanseCategoriesResponse
import com.example.wallet.model.response.SingleExpanseCategoryResponse
import com.example.wallet.requests.AddOrEditCategoryRequest

class ExpanseCategoriesRepository(private val service: WalletWebService = WalletWebService()) {
    suspend fun getExpanseCategories(): AllExpanseCategoriesResponse {
        return service.getExpanseCategories()
    }

    suspend fun getCategoryForExpanse(expanseId: Int): SingleExpanseCategoryResponse {
         return service.getCategoryForExpanse(expanseId)
    }

    suspend fun addCategoryToDb(categoryData: AddOrEditCategoryRequest) {
        return service.addCategoryToDb(categoryData)
    }


}
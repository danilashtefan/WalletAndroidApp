package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.AllExpanseCategoriesResponse
import com.example.wallet.model.response.SingleExpanseCategoryResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.requests.AddOrEditCategoryRequest
import java.lang.Exception

object ExpanseCategoriesRepository {
    private val service: WalletWebService = WalletWebService()
    var categoriesFiltered = SecondAllExpenseCategoriesResponse()
    suspend fun getExpanseCategories(): AllExpanseCategoriesResponse {

        return service.getExpanseCategories()
    }

    suspend fun getFilteredExpenseCategories(authToken: String?): SecondAllExpenseCategoriesResponse {
        categoriesFiltered = service.getFilteredExpenseCategories(authToken)
        return categoriesFiltered
    }

    suspend fun getCategoryForExpanse(expanseId: Int): SingleExpanseCategoryResponse {
        return service.getCategoryForExpanse(expanseId)
    }

    suspend fun addCategoryToDb(categoryData: AddOrEditCategoryRequest) {
        return service.addCategoryToDb(categoryData)
    }

    fun getCategory(categoryId: Int): SecondAllExpenseCategoriesResponseItem {

        for (category in categoriesFiltered) {
            if (category.id === categoryId) {
                return category
            }
        }
        throw Exception("No category found!")

    }

    suspend fun editCategoryInDb(
        id: Int,
        categoryData: AddOrEditCategoryRequest,
        authToken: String
    ) {
        return service.editCategoryInDb(id, categoryData, authToken)
    }

    suspend fun deleteCategory(categoryId: Int, authToken: String) {
        return ExpanseCategoriesRepository.service.deleteCategoryFromDb(categoryId, authToken)
    }


}
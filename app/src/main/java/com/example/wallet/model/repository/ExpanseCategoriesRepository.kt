package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.SingleExpanseCategoryResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.TopExpenseCategoryWithAmountResponse
import com.example.wallet.requests.AddOrEditCategoryRequest

object ExpanseCategoriesRepository {
    private val service: WalletWebService = WalletWebService()
    var categoriesFiltered = SecondAllExpenseCategoriesResponse()

    suspend fun getFilteredExpenseCategories(authToken: String?): SecondAllExpenseCategoriesResponse {
        categoriesFiltered = service.getFilteredExpenseCategories(authToken)
        return categoriesFiltered
    }

    suspend fun getCategoryForExpanse(authToken: String, expanseId: Int): SingleExpanseCategoryResponse {
        return service.getCategoryForExpanse(authToken, expanseId)
    }

    suspend fun addCategoryToDb(authToken: String, categoryData: AddOrEditCategoryRequest) {
        return service.addCategoryToDb(authToken,categoryData)
    }

    fun getCategory(categoryId: Int): SecondAllExpenseCategoriesResponseItem {

        for (category in categoriesFiltered) {
            if (category.id === categoryId) {
                return category
            }
        }
        throw Exception("No category found!")

    }

    suspend fun getCategoriesWithExpenses(authToken: String?, minDate: String, maxDate: String): List<TopExpenseCategoryWithAmountResponse> {
        val expenseCategories = service.getCategoriesWithExpenses(authToken, minDate, maxDate)
        return expenseCategories
    }

    suspend fun getTopExpenseCategory(authToken: String?, minDate: String, maxDate: String): TopExpenseCategoryWithAmountResponse {
        val topExpenseCategory = service.getTopExpenseCategory(authToken, minDate, maxDate)
        return topExpenseCategory
    }

    suspend fun getTopIncomeCategory(authToken: String?, minDate: String, maxDate: String): TopExpenseCategoryWithAmountResponse {
        val topExpenseCategory = service.getTopIncomeCategory(authToken, minDate, maxDate)
        return topExpenseCategory
    }

    suspend fun editCategoryInDb(
        id: Int,
        categoryData: AddOrEditCategoryRequest,
        authToken: String
    ) {
        return service.editCategoryInDb(id, categoryData, authToken)
    }

    suspend fun deleteCategory(categoryId: Int, authToken: String) {
        return service.deleteCategoryFromDb(categoryId, authToken)
    }


}
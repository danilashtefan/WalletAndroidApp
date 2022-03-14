package com.example.wallet.api

import android.util.Log
import com.example.wallet.helpers.LinkBuilder
import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.Expanse
import com.example.wallet.model.response.AllExpanseCategoriesResponse
import com.example.wallet.model.response.SingleExpanseCategoryResponse
import com.example.wallet.model.response.SingleTransactionWalletResponse
import com.example.wallet.model.response.login.LoginResponse
import com.example.wallet.model.response.login.RegisterResponse
import com.example.wallet.model.response.transactions.AllTransactionWalletsResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponse
import com.example.wallet.model.response.transactions.SecondAPI.TopExpenseCategoryWithAmountResponse
import com.example.wallet.requests.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


class WalletWebService {
    private lateinit var api: WalletAPI

    init {
        val retrofit = Retrofit.Builder()
            //KAMORA
            //.baseUrl("http://192.168.88.224:8080/api/")
            //HOME
            //.baseUrl("http://10.0.1.60:8080/api/")
            //OFFICE
            //baseUrl("http://192.168.0.116:8080/api/")
            //Iulia
            .baseUrl("http://192.168.1.70:8080/api/")
            //.baseUrl("http://192.168.1.80:8080/api/")
            //.baseUrl("http://152.66.156.198:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(WalletAPI::class.java)
    }

    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return api.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return api.register(registerRequest)
    }

    suspend fun getFilteredWallets(authToken: String?): SecondAllWalletsResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getFilteredWallets(authHeader)
    }

    suspend fun getExpanses(authToken: String?): AllExpansesResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        Log.d("INFO", "Authorization header: $authHeader")
        return api.getExpanses(authHeader = authHeader)
    }

    suspend fun getFilteredExpanses(authToken: String?): SecondAllExpensesResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        Log.d("INFO", "Authorization header: $authHeader")
        return api.getFilteredExpanses(authHeader = authHeader)
    }

    suspend fun getCategoryFilteredExpenses(
        authToken: String?,
        categoryId: Int
    ): SecondAllExpensesResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        Log.d("INFO", "Authorization header: $authHeader")
        return api.getCategoryFilteredExpenses(authHeader = authHeader, categoryId = categoryId)
    }

    suspend fun getTopExpenseCategory(authToken: String?):TopExpenseCategoryWithAmountResponse{
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getTopExpenseCategory(authHeader = authHeader)
    }

    suspend fun getTopIncomeCategory(authToken: String?):TopExpenseCategoryWithAmountResponse{
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getTopIncomeCategory(authHeader = authHeader)
    }

    suspend fun getWalletFilteredExpenses(
        authToken: String?,
        walletId: Int
    ): SecondAllExpensesResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        Log.d("INFO", "Authorization header: $authHeader")
        return api.getWalletFilteredExpenses(authHeader = authHeader, walletId = walletId)
    }

    suspend fun getExpanseCategories(): AllExpanseCategoriesResponse {
        return api.getExpanseCategories()
    }

    suspend fun getFilteredExpenseCategories(authToken: String?): SecondAllExpenseCategoriesResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getFilteredExpenseCategories(authHeader = authHeader)
    }

    suspend fun deleteTransactionFromDb(expenseId: Int, authToken: String) {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.deleteTransactionFromDb(expenseId, authHeader)
    }

    suspend fun deleteCategoryFromDb(categoryId: Int, authToken: String) {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.deleteCategoryFromDb(categoryId, authHeader)
    }

    suspend fun deleteWalletFromDb(walletId: Int, authToken: String) {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.deleteWalletFromDb(walletId, authHeader)
    }

    suspend fun getCategoryForExpanse(expenseId: Int): SingleExpanseCategoryResponse {
        return api.getCategoryForExpanse(expenseId)
    }

    suspend fun getWalletForExpanse(expenseId: Int): SingleTransactionWalletResponse {
        return api.getWalletForExpanse(expenseId)
    }

    suspend fun getWallets(): AllTransactionWalletsResponse {
        return api.getWallets()
    }

    suspend fun updateTransactionInDb(
        transactionId: Int,
        transactionData: AddOrEditTransactionRequest
    ): Expanse {
        return api.updateTransactionInDb(transactionId, transactionData)
    }

    suspend fun addTransactionToDb(transactionData: AddOrEditTransactionRequest) {
        return api.addTransactionToDb(transactionData)
    }

    suspend fun addCategoryToDb(categoryData: AddOrEditCategoryRequest) {
        return api.addCategoryToDb(categoryData)
    }

    suspend fun addWalletToDb(walletData: AddOrEditWalletRequest) {
        return api.addWalletToDb(walletData)
    }

    suspend fun editCategoryInDb(
        id: Int,
        categoryData: AddOrEditCategoryRequest,
        authToken: String
    ) {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.updateCategoryInDb(id, categoryData, authHeader)
    }

    suspend fun editWalletInDb(id: Int, walletData: AddOrEditWalletRequest, authToken: String) {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.updateWalletInDb(id, walletData, authHeader)
    }


    interface WalletAPI {
        @POST("login")
        suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

        @POST("user/save")
        suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

        @GET("expanses")
        suspend fun getExpanses(@Header("Authorization") authHeader: String): AllExpansesResponse

        @PATCH("expanseCategories2/{id}")
        suspend fun updateCategoryInDb(
            @Path("id") categoryId: Int,
            @Body categoryData: AddOrEditCategoryRequest,
            @Header("Authorization") authHeader: String
        )

        @PATCH("wallets2/{id}")
        suspend fun updateWalletInDb(
            @Path("id") categoryId: Int,
            @Body walletData: AddOrEditWalletRequest,
            @Header("Authorization") authHeader: String
        )

        @GET("expanses2")
        suspend fun getFilteredExpanses(@Header("Authorization") authHeader: String): SecondAllExpensesResponse

        @GET("expanseCategories2/{id}/expenses")
        suspend fun getCategoryFilteredExpenses(
            @Header("Authorization") authHeader: String,
            @Path("id") categoryId: Int
        ): SecondAllExpensesResponse

        @GET("expanseCategories2/topExpenseCategory")
        suspend fun getTopExpenseCategory(
            @Header("Authorization") authHeader: String
        ): TopExpenseCategoryWithAmountResponse

        @GET("expanseCategories2/topIncomeCategory")
        suspend fun getTopIncomeCategory(
            @Header("Authorization") authHeader: String
        ): TopExpenseCategoryWithAmountResponse
        @DELETE("expanses2/{id}")
        suspend fun deleteTransactionFromDb(
            @Path("id") expanseId: Int,
            @Header("Authorization") authHeader: String
        )

        @DELETE("expanseCategories2/{id}")
        suspend fun deleteCategoryFromDb(
            @Path("id") categoryId: Int,
            @Header("Authorization") authHeader: String
        )

        @GET("wallets2")
        suspend fun getFilteredWallets(@Header("Authorization") authHeader: String): SecondAllWalletsResponse

        @GET("wallets2/{id}/expenses")
        suspend fun getWalletFilteredExpenses(
            @Header("Authorization") authHeader: String,
            @Path("id") walletId: Int
        ): SecondAllExpensesResponse

        @DELETE("wallets2/{id}")
        suspend fun deleteWalletFromDb(
            @Path("id") categoryId: Int,
            @Header("Authorization") authHeader: String
        )

        @GET("expanseCategories")
        suspend fun getExpanseCategories(): AllExpanseCategoriesResponse

        @GET("expanses/{id}/category")
        suspend fun getCategoryForExpanse(@Path("id") expanseId: Int): SingleExpanseCategoryResponse

        @GET("expanseCategories2")
        suspend fun getFilteredExpenseCategories(@Header("Authorization") authHeader: String): SecondAllExpenseCategoriesResponse

        @PATCH("expanses/{id}")
        suspend fun updateTransactionInDb(
            @Path("id") transactionId: Int,
            @Body transactionData: AddOrEditTransactionRequest
        ): Expanse

        @GET("expanses/{id}/wallet")
        suspend fun getWalletForExpanse(@Path("id") expanseId: Int): SingleTransactionWalletResponse

        @GET("wallets")
        suspend fun getWallets(): AllTransactionWalletsResponse

        @POST("expanses")
        suspend fun addTransactionToDb(@Body transactionData: AddOrEditTransactionRequest)

        @POST("expanseCategories")
        suspend fun addCategoryToDb(@Body categoryData: AddOrEditCategoryRequest)

        @POST("wallets")
        suspend fun addWalletToDb(@Body walletData: AddOrEditWalletRequest)

    }
}
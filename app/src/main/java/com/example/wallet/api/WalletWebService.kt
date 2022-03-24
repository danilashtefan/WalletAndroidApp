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
import com.example.wallet.model.response.transactions.SecondAPI.*
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
            .baseUrl("http://192.168.1.67:8080/api/")
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


    suspend fun getCategoriesWithExpenses(
        authToken: String?,
        minDate: String,
        maxDate: String
    ): List<TopExpenseCategoryWithAmountResponse> {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getCategoriesWithExpenses(authHeader = authHeader, minDate, maxDate)
    }

    suspend fun getTopIncomeCategory(
        authToken: String?,
        minDate: String,
        maxDate: String
    ): TopExpenseCategoryWithAmountResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getTopIncomeCategory(authHeader = authHeader, minDate, maxDate)
    }

    suspend fun getTopExpenseCategory(
        authToken: String?,
        minDate: String,
        maxDate: String
    ): TopExpenseCategoryWithAmountResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getTopExpenseCategory(authHeader = authHeader, minDate, maxDate)
    }

    suspend fun getWalletsWithExpenses(
        authToken: String?,
        minDate: String,
        maxDate: String
    ): List<TopWalletWithAmountResponse> {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getWalletsWithExpenses(authHeader = authHeader, minDate, maxDate)
    }

    suspend fun getTopExpenseWallet(
        authToken: String?,
        minDate: String,
        maxDate: String
    ): TopWalletWithAmountResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getTopExpenseWallet(authHeader = authHeader, minDate, maxDate)
    }

    suspend fun getTopIncomeWallet(
        authToken: String?,
        minDate: String,
        maxDate: String
    ): TopWalletWithAmountResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getTopIncomeWallet(authHeader = authHeader, minDate, maxDate)
    }

    suspend fun getWalletFilteredExpenses(
        authToken: String?,
        walletId: Int
    ): SecondAllExpensesResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        Log.d("INFO", "Authorization header: $authHeader")
        return api.getWalletFilteredExpenses(authHeader = authHeader, walletId = walletId)
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

    suspend fun getCategoryForExpanse(authToken: String, expenseId: Int): SingleExpanseCategoryResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        var answer = api.getCategoryForExpanse(authHeader,expenseId)
        return answer
    }

    suspend fun getWalletForExpanse(authToken:String, expenseId: Int): SingleTransactionWalletResponse {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.getWalletForExpanse(authHeader,expenseId)
    }


    suspend fun updateTransactionInDb(
        transactionId: Int,
        transactionData: AddOrEditTransactionRequest,
        authToken: String
    ){
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.updateTransactionInDb(transactionId, transactionData, authHeader)
    }

    suspend fun addTransactionToDb(authToken: String, transactionData: AddOrEditTransactionRequest) {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.addTransactionToDb(authHeader,transactionData)
    }

    suspend fun addCategoryToDb(authToken: String, categoryData: AddOrEditCategoryRequest) {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.addCategoryToDb(authHeader, categoryData)
    }

    suspend fun addWalletToDb(authToken: String, walletData: AddOrEditWalletRequest) {
        val authHeader = LinkBuilder.builtAuthorizationHeader(authToken = authToken)
        return api.addWalletToDb(authHeader, walletData)
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

        @GET("expanseCategories2/expenseCategoriesWithExpenses")
        suspend fun getCategoriesWithExpenses(
            @Header("Authorization") authHeader: String,
            @Query("minDate") minDate: String,
            @Query("maxDate") maxDate: String
        ): List<TopExpenseCategoryWithAmountResponse>

        @GET("wallets2/walletsWithExpenses")
        suspend fun getWalletsWithExpenses(
            @Header("Authorization") authHeader: String,
            @Query("minDate") minDate: String,
            @Query("maxDate") maxDate: String
        ): List<TopWalletWithAmountResponse>

        @GET("expanseCategories2/topExpenseCategory")
        suspend fun getTopExpenseCategory(
            @Header("Authorization") authHeader: String,
            @Query("minDate") minDate: String,
            @Query("maxDate") maxDate: String
        ): TopExpenseCategoryWithAmountResponse

        @GET("expanseCategories2/topIncomeCategory")
        suspend fun getTopIncomeCategory(
            @Header("Authorization") authHeader: String,
            @Query("minDate") minDate: String,
            @Query("maxDate") maxDate: String
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

        @GET("wallets2/topExpenseWallet")
        suspend fun getTopExpenseWallet(
            @Header("Authorization") authHeader: String,
            @Query("minDate") minDate: String,
            @Query("maxDate") maxDate: String
        ): TopWalletWithAmountResponse

        @GET("wallets2/topIncomeWallet")
        suspend fun getTopIncomeWallet(
            @Header("Authorization") authHeader: String,
            @Query("minDate") minDate: String,
            @Query("maxDate") maxDate: String
        ): TopWalletWithAmountResponse

        @GET("expanseCategories2")
        suspend fun getFilteredExpenseCategories(@Header("Authorization") authHeader: String): SecondAllExpenseCategoriesResponse

        @GET("expanses2/{id}/wallet")
        suspend fun getWalletForExpanse(@Header("Authorization") authHeader: String,@Path("id") expanseId: Int): SingleTransactionWalletResponse

        @GET("expanses2/{id}/category")
        suspend fun getCategoryForExpanse(@Header("Authorization") authHeader: String, @Path("id") expanseId: Int): SingleExpanseCategoryResponse

        @POST("expanses")
        suspend fun addTransactionToDb(@Header("Authorization") authHeader: String, @Body transactionData: AddOrEditTransactionRequest)

        @POST("expanseCategories")
        suspend fun addCategoryToDb(@Header("Authorization") authHeader: String, @Body categoryData: AddOrEditCategoryRequest)

        @POST("wallets")
        suspend fun addWalletToDb(@Header("Authorization") authHeader: String, @Body walletData: AddOrEditWalletRequest)

        @PATCH("expanses2/{id}")
        suspend fun updateTransactionInDb(
            @Path("id") transactionId: Int,
            @Body transactionData: AddOrEditTransactionRequest,
            @Header("Authorization") authHeader: String
        )

    }
}
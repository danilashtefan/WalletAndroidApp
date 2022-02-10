package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.wallet.helpers.LinkBuilder
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.response.transactions.Wallet
import com.example.wallet.requests.AddOrEditCategoryRequest
import com.example.wallet.requests.AddOrEditTransactionRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) : ViewModel() {
    private val categoriesRepository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
    private val walletRepository: WalletRepository = WalletRepository()
    var userName = ""
//RELATED TO TRANSACTION ADD
//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
    var whatToAddstate = mutableStateOf("")
    var nameFieldTemporaryValueBeforeSavingtoDB: String? = null
    var amountFieldTemporaryValueBeforeSavingtoDB: String? = null
    var dateFieldTemporaryValueBeforeSavingtoDB: String? = null
    var commentsFieldTemporaryValueBeforeSavingtoDB: String? = null
    var locationFieldTemporaryValueBeforeSavingtoDB: String? = null
    var transactionCetegoriesState = mutableStateOf((listOf(ExpanseCategory())))
    var transactionWalletsState = mutableStateOf((listOf(Wallet())))
    //This is only Category Name Displayed, not the actual Category name!!
    var categoryNameFieldTemporaryValueBeforeSavingtoDB: String? = "Category of transaction"
    var typeFieldTemporaryValueBeforeSavingtoDB: String? = "Type of transaction"
    var walletNameFieldTemporaryValueBeforeSavingtoDB: String? = "Wallet"
    var categoryLinkTemporaryValueBeforeSavingtoDB: String? = null
    var walletLinkTemporaryValueBeforeSavingtoDB: String? = null
    var dataLoaded = mutableStateOf(false)
//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
    var iconCategoryFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var nameCategoryFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var typeCategoryFieldTemporaryValueBeforeSavingtoDB: String? = ""



init{
    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("EXCEPTION", "Thread exception while fetching categories on Adding Screen")
    }
    viewModelScope.launch(handler + Dispatchers.IO) {
        dataStorePreferenceRepository.getUsername.
        catch { Log.d("ERROR","Could not get Username from Data Store on Add screen") }
            .collect{
                Log.d("TOKEN","Username on Add Screen: $it")
                userName = it
            }
    }
    viewModelScope.launch(handler + Dispatchers.IO) {
        val transactionCategories = getTransactionCategories()
        val transactionWallets = getTransactionWallets()
        transactionCetegoriesState.value = transactionCategories
        transactionWalletsState.value = transactionWallets
        dataLoaded.value = true;
    }

}

    suspend fun getTransactionCategories(): List<ExpanseCategory> {
        var listOfCategories = categoriesRepository.getExpanseCategories()._embedded.expanseCategories
        return listOfCategories
    }

    suspend fun getTransactionWallets(): List<Wallet>{
        var listOfWallets = walletRepository.getWallets()._embedded.wallets
        return listOfWallets
    }

    fun updateTemporaryFieldValueBeforeSavingToDB(field: String, value: String) {
        when (field) {
            "amount" -> amountFieldTemporaryValueBeforeSavingtoDB = value
            "comments" -> commentsFieldTemporaryValueBeforeSavingtoDB = value
            "date" -> dateFieldTemporaryValueBeforeSavingtoDB = value
            "location" -> locationFieldTemporaryValueBeforeSavingtoDB = value
            "name" -> nameFieldTemporaryValueBeforeSavingtoDB = value
            "categoryName" -> categoryNameFieldTemporaryValueBeforeSavingtoDB = value
            "walletName" -> walletNameFieldTemporaryValueBeforeSavingtoDB = value
            "type" -> typeFieldTemporaryValueBeforeSavingtoDB = value
            "nameCategory" -> nameCategoryFieldTemporaryValueBeforeSavingtoDB = value
            "typeCategory" -> typeCategoryFieldTemporaryValueBeforeSavingtoDB = value
            "iconCategory" -> iconCategoryFieldTemporaryValueBeforeSavingtoDB = value

        }

    }

    fun updateCategoryLinkValueBeforeSavingToDB(category: ExpanseCategory) {
        this.categoryLinkTemporaryValueBeforeSavingtoDB = LinkBuilder.buildCategoryLinkForAddingToExpanse(categoryId = category.id)
        updateTemporaryFieldValueBeforeSavingToDB("categoryName", category.expanseCategoryName)
    }

    fun getFieldToUpdateInDB(field: String): String? {
        when (field) {
            "amount" -> return amountFieldTemporaryValueBeforeSavingtoDB.toString()
            "comments" -> return commentsFieldTemporaryValueBeforeSavingtoDB
            "date" -> return dateFieldTemporaryValueBeforeSavingtoDB
            "location" -> return locationFieldTemporaryValueBeforeSavingtoDB
            "name" -> return nameFieldTemporaryValueBeforeSavingtoDB
            "categoryName" -> return categoryNameFieldTemporaryValueBeforeSavingtoDB
            "walletName" -> return walletNameFieldTemporaryValueBeforeSavingtoDB
            "type" -> return typeFieldTemporaryValueBeforeSavingtoDB
        }
        return null
    }

    fun addTransactionToDb() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when saving to DB")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            addTransactionToDb(
                    transactionData = AddOrEditTransactionRequest(
                    name = nameFieldTemporaryValueBeforeSavingtoDB,
                    amount = amountFieldTemporaryValueBeforeSavingtoDB?.toInt(),
                    date = dateFieldTemporaryValueBeforeSavingtoDB,
                    comments = commentsFieldTemporaryValueBeforeSavingtoDB,
                    location = locationFieldTemporaryValueBeforeSavingtoDB,
                    type = typeFieldTemporaryValueBeforeSavingtoDB,
                    category = categoryLinkTemporaryValueBeforeSavingtoDB,
                    wallet = walletLinkTemporaryValueBeforeSavingtoDB
                )
            )
        }

    }

    fun addCategoryToDb(){
        val accessToken: LiveData<String> = dataStorePreferenceRepository.getAccessToken.asLiveData()

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when adding category to Db")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            addCategoryToDb(
                categoryData = AddOrEditCategoryRequest(
                    expanseCategoryName = nameCategoryFieldTemporaryValueBeforeSavingtoDB,
                    type = typeCategoryFieldTemporaryValueBeforeSavingtoDB,
                    icon = iconCategoryFieldTemporaryValueBeforeSavingtoDB

                )
            )
        }


    }

    suspend fun addTransactionToDb( transactionData: AddOrEditTransactionRequest) {
        TransactionsRepository.addTransactionToDb(transactionData)
    }

    suspend fun addCategoryToDb(categoryData:AddOrEditCategoryRequest){
        categoriesRepository.addCategoryToDb(categoryData)
    }


    fun updateWalletLinkValueBeforeSavingToDB(wallet: Wallet) {
        this.walletLinkTemporaryValueBeforeSavingtoDB = LinkBuilder.buildWalletLinkForAddingToExpanse(walletId = wallet.id)
        updateTemporaryFieldValueBeforeSavingToDB("walletName", wallet.walletName)
    }

}
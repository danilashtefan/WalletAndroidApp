package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
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
import com.example.wallet.model.response.transactions.SecondAPI.*
import com.example.wallet.model.response.transactions.Wallet
import com.example.wallet.requests.AddOrEditCategoryRequest
import com.example.wallet.requests.AddOrEditTransactionRequest
import com.example.wallet.requests.AddOrEditWalletRequest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class AddViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) : ViewModel() {
    var userName = ""
    var authToken = ""

//RELATED TO TRANSACTION ADD
//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
    var whatToAddstate = mutableStateOf("")
    var nameFieldTemporaryValueBeforeSavingtoDB: String? = null
    var amountFieldTemporaryValueBeforeSavingtoDB: String? = null
    var dateFieldTemporaryValueBeforeSavingtoDB: String = "Date"
    var commentsFieldTemporaryValueBeforeSavingtoDB: String? = null
    var locationFieldTemporaryValueBeforeSavingtoDB: String? = null
    //var transactionCetegoriesState = mutableStateOf((listOf(ExpanseCategory())))
    var transactionCetegoriesState = mutableStateOf(emptyList<SecondAllExpenseCategoriesResponseItem>())
    //var transactionWalletsState = mutableStateOf((listOf(Wallet())))
    var transactionWalletsState = mutableStateOf(emptyList<SecondAllWalletsResponseItem>())
    //This is only Category Name Displayed, not the actual Category name!!
    var categoryNameFieldTemporaryValueBeforeSavingtoDB: String? = "Category of transaction"
    var typeFieldTemporaryValueBeforeSavingtoDB: String? = "Type of transaction"
    var walletNameFieldTemporaryValueBeforeSavingtoDB: String? = "Wallet"
    var categoryLinkTemporaryValueBeforeSavingtoDB: String? = null
    var walletLinkTemporaryValueBeforeSavingtoDB: String? = null
    var dataLoaded = mutableStateOf(false)
    var expandedCalendar = mutableStateOf(false)
    var datePicked = mutableStateOf("1000-01-01")
//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
    var iconCategoryFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var nameCategoryFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var typeCategoryFieldTemporaryValueBeforeSavingtoDB: String? = ""
//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
    var iconWalletFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var nameWalletFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var currencyWalletFieldTemporaryValueBeforeSavingtoDB: String? = ""

    var locationState = mutableStateOf("")



init{
    val handler = CoroutineExceptionHandler { _, exception ->
        Log.d("EXCEPTION", "Thread exception while fetching categories on Adding Screen")
    }
    viewModelScope.launch(handler + Dispatchers.IO) {
        dataStorePreferenceRepository.getAccessToken.
        catch { Log.d("ERROR","EXPECTION while getting the token in the add screen") }
            .collect{
                Log.d("TOKEN","Access token on add screen: $it")
                authToken = it
            }
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
        val transactionCategories = getFilteredTransactionCategories()
        val transactionWallets = getFilteredWallets()
        transactionCetegoriesState.value = transactionCategories
        transactionWalletsState.value = transactionWallets
        dataLoaded.value = true;
    }

}

    suspend fun getFilteredTransactionCategories(): SecondAllExpenseCategoriesResponse{
        var listOfCategories = ExpanseCategoriesRepository.getFilteredExpenseCategories(authToken)
        return listOfCategories
    }

    suspend fun getFilteredWallets(): SecondAllWalletsResponse{
        var listOfWallets = WalletRepository.getFilteredWallets(authToken)
        return listOfWallets
    }

    suspend fun getTransactionWallets(): List<Wallet>{
        var listOfWallets = WalletRepository.getWallets()._embedded.wallets
        return listOfWallets
    }

    fun updateLocation(value: String){
        locationState.value = value
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
            "iconWallet" -> iconWalletFieldTemporaryValueBeforeSavingtoDB = value
            "currencyWallet" -> currencyWalletFieldTemporaryValueBeforeSavingtoDB = value
            "nameWallet" -> nameWalletFieldTemporaryValueBeforeSavingtoDB = value

        }

    }

    fun updateCategoryLinkValueBeforeSavingToDB(category: SecondAllExpenseCategoriesResponseItem) {
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
                    wallet = walletLinkTemporaryValueBeforeSavingtoDB,
                    username = userName
                )
            )
        }

    }

    fun addCategoryToDb(){

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when adding category to Db")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            addCategoryToDb(
                categoryData = AddOrEditCategoryRequest(
                    expanseCategoryName = nameCategoryFieldTemporaryValueBeforeSavingtoDB,
                    type = typeCategoryFieldTemporaryValueBeforeSavingtoDB,
                    icon = iconCategoryFieldTemporaryValueBeforeSavingtoDB,
                    username = userName

                )
            )
        }


    }

    fun addWalletToDb(){

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when adding wallet to Db")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            addWalletToDb(
                walletData = AddOrEditWalletRequest(
                    walletName = nameWalletFieldTemporaryValueBeforeSavingtoDB,
                    currency = currencyWalletFieldTemporaryValueBeforeSavingtoDB,
                    icon = iconWalletFieldTemporaryValueBeforeSavingtoDB,
                    username = userName

                )
            )
        }


    }

    suspend fun addTransactionToDb( transactionData: AddOrEditTransactionRequest) {
        TransactionsRepository.addTransactionToDb(transactionData)
    }

    suspend fun addCategoryToDb(categoryData:AddOrEditCategoryRequest){
        ExpanseCategoriesRepository.addCategoryToDb(categoryData)
    }

    suspend fun addWalletToDb(walletData: AddOrEditWalletRequest){
        WalletRepository.addWalletToDb(walletData)
    }


    fun updateWalletLinkValueBeforeSavingToDB(wallet: SecondAllWalletsResponseItem) {
        this.walletLinkTemporaryValueBeforeSavingtoDB = LinkBuilder.buildWalletLinkForAddingToExpanse(walletId = wallet.id)
        updateTemporaryFieldValueBeforeSavingToDB("walletName", wallet.walletName)
    }

}
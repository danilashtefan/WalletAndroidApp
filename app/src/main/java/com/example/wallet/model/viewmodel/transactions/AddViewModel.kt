package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import com.example.wallet.requests.AddOrEditCategoryRequest
import com.example.wallet.requests.AddOrEditTransactionRequest
import com.example.wallet.requests.AddOrEditWalletRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) : ViewModel() {
    var userName = ""
    var authToken = ""

//RELATED TO TRANSACTION ADD
//--------------------------------------------------------------------------
//--------------------------------------------------------------------------
    var whatToAddstate = mutableStateOf("")
    var nameFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var amountFieldTemporaryValueBeforeSavingtoDB: String? = null
    var dateFieldTemporaryValueBeforeSavingtoDB: String = "Date"
    var commentsFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var locationFieldTemporaryValueBeforeSavingtoDB: MutableState<String> = mutableStateOf("No location")
    //var transactionCetegoriesState = mutableStateOf((listOf(ExpanseCategory())))
    var transactionCetegoriesState = mutableStateOf(emptyList<SecondAllExpenseCategoriesResponseItem>())
    //var transactionWalletsState = mutableStateOf((listOf(Wallet())))
    var transactionWalletsState = mutableStateOf(emptyList<SecondAllWalletsResponseItem>())
    //This is only Category Name Displayed, not the actual Category name!!
    var categoryNameFieldTemporaryValueBeforeSavingtoDB: MutableState<String> = mutableStateOf("None")
    var typeFieldTemporaryValueBeforeSavingtoDB: MutableState<String> = mutableStateOf( "None")
    var walletNameFieldTemporaryValueBeforeSavingtoDB: MutableState<String> = mutableStateOf("None")
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
    var showIncorrectDataAlertDialog = mutableStateOf(false)

    var incorrectDataAlertDialogText = "Some of the fields are empty. Please, introduce:\n\n"+
            "\u2022" + "Type\n" + "\u2022" + "Category\n" + "\u2022" + "Wallet\n" + "\u2022" + "Date\n"+"\u2022" +"Amount"
//var incorrectDataAlertDialogText = "Some text"



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
        dataLoaded.value = true
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



    fun updateLocation(value: String){
        locationState.value = value
    }
    fun updateTemporaryFieldValueBeforeSavingToDB(field: String, value: String) {
        when (field) {
            "amount" -> amountFieldTemporaryValueBeforeSavingtoDB = value
            "comments" -> commentsFieldTemporaryValueBeforeSavingtoDB = value
            "date" -> dateFieldTemporaryValueBeforeSavingtoDB = value
            "location" -> locationFieldTemporaryValueBeforeSavingtoDB.value = value
            "name" -> nameFieldTemporaryValueBeforeSavingtoDB = value
            "categoryName" -> categoryNameFieldTemporaryValueBeforeSavingtoDB.value = value
            "walletName" -> walletNameFieldTemporaryValueBeforeSavingtoDB.value = value
            "type" -> typeFieldTemporaryValueBeforeSavingtoDB.value = value
            "nameCategory" -> nameCategoryFieldTemporaryValueBeforeSavingtoDB = value
            "typeCategory" -> typeCategoryFieldTemporaryValueBeforeSavingtoDB = value
            "iconCategory" -> iconCategoryFieldTemporaryValueBeforeSavingtoDB = value
            "iconWallet" -> iconWalletFieldTemporaryValueBeforeSavingtoDB = value
            "currencyWallet" -> currencyWalletFieldTemporaryValueBeforeSavingtoDB = value
            "nameWallet" -> nameWalletFieldTemporaryValueBeforeSavingtoDB = value

        }

    }

    fun updateCategoryLinkValueBeforeSavingToDB(category: SecondAllExpenseCategoriesResponseItem) {
        this.categoryLinkTemporaryValueBeforeSavingtoDB = category.id.toString()
            //LinkBuilder.buildCategoryLinkForAddingToExpanse(categoryId = category.id)
        updateTemporaryFieldValueBeforeSavingToDB("categoryName", category.expanseCategoryName)
    }

    fun getFieldToUpdateInDB(field: String): String? {
        when (field) {
            "amount" -> return amountFieldTemporaryValueBeforeSavingtoDB.toString()
            "comments" -> return commentsFieldTemporaryValueBeforeSavingtoDB
            "date" -> return dateFieldTemporaryValueBeforeSavingtoDB
            "location" -> return locationFieldTemporaryValueBeforeSavingtoDB.value
            "name" -> return nameFieldTemporaryValueBeforeSavingtoDB
            "categoryName" -> return categoryNameFieldTemporaryValueBeforeSavingtoDB.value
            "walletName" -> return walletNameFieldTemporaryValueBeforeSavingtoDB.value
            "type" -> return typeFieldTemporaryValueBeforeSavingtoDB.value
        }
        return null
    }

    fun addTransactionToDb() : Boolean {
        if(typeFieldTemporaryValueBeforeSavingtoDB.value == "Type of transaction" || categoryLinkTemporaryValueBeforeSavingtoDB == null ||
             walletLinkTemporaryValueBeforeSavingtoDB == null ||
               dateFieldTemporaryValueBeforeSavingtoDB == "Date" ||
                amountFieldTemporaryValueBeforeSavingtoDB == null){
            incorrectDataDialogShow()
            return false
        }
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
                    location = locationFieldTemporaryValueBeforeSavingtoDB.value,
                    type = typeFieldTemporaryValueBeforeSavingtoDB.value,
                    category = categoryLinkTemporaryValueBeforeSavingtoDB,
                    wallet = walletLinkTemporaryValueBeforeSavingtoDB,
                    username = userName
                )
            )
        }
        return true
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

    suspend fun addTransactionToDb(transactionData: AddOrEditTransactionRequest) {
        TransactionsRepository.addTransactionToDb(authToken, transactionData)
    }

    suspend fun addCategoryToDb(categoryData:AddOrEditCategoryRequest){
        ExpanseCategoriesRepository.addCategoryToDb(authToken,categoryData)
    }

    suspend fun addWalletToDb(walletData: AddOrEditWalletRequest){
        WalletRepository.addWalletToDb(authToken, walletData)
    }


    fun updateWalletLinkValueBeforeSavingToDB(wallet: SecondAllWalletsResponseItem) {
        this.walletLinkTemporaryValueBeforeSavingtoDB = wallet.id.toString()
            //LinkBuilder.buildWalletLinkForAddingToExpanse(walletId = wallet.id)
        updateTemporaryFieldValueBeforeSavingToDB("walletName", wallet.walletName)
    }

    fun incorrectDataDialogShow(){
        showIncorrectDataAlertDialog.value = true
    }
    fun incorrectDataDialogClose(){
        showIncorrectDataAlertDialog.value = false
    }

}
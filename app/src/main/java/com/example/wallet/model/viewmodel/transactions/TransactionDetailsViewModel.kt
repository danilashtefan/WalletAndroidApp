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
import com.example.wallet.model.response.transactions.SecondAPI.*
import com.example.wallet.requests.AddOrEditTransactionRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TransactionDetailsViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModel() {

    // private val walletRepository: WalletRepository = WalletRepository()
    private var transactionId: Int = 0
    var dataLoaded = mutableStateOf(false)

    //var transactionCetegoriesState = mutableStateOf((listOf(ExpanseCategory())))
    var transactionCetegoriesState =
        mutableStateOf((emptyList<SecondAllExpenseCategoriesResponseItem>()))
    var transactionWalletsState = mutableStateOf(emptyList<SecondAllWalletsResponseItem>())
    var transaction = mutableStateOf(SecondAllExpensesItem())

    var nameFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var amountFieldTemporaryValueBeforeSavingtoDB: String? = null
    var photoUrlFieldTemporaryValueBeforeSavingtoDB: String? = null
    var dateFieldTemporaryValueBeforeSavingtoDB: String? = null
    var commentsFieldTemporaryValueBeforeSavingtoDB: String? = ""
    var locationFieldTemporaryValueBeforeSavingtoDB: String? = ""

    //This is only Category Name Displayed, not the actual Category name!!
    var categoryNameFieldTemporaryValueBeforeSavingtoDB : MutableState<String> = mutableStateOf("")
    var typeFieldTemporaryValueBeforeSavingtoDB : MutableState<String> = mutableStateOf("")
    var walletNameFieldTemporaryValueBeforeSavingtoDB : MutableState<String> = mutableStateOf("")

    var categoryLinkTemporaryValueBeforeSavingtoDB: String? = null
    var walletLinkTemporaryValueBeforeSavingtoDB: String? = null
    var categoryIconTemporaryValueBeforeSavingtoDB: String? = null

    var expandedCalendar = mutableStateOf(false)
    var datePicked = mutableStateOf("")

    var username: String = ""
    var authToken = ""

    var locationState = mutableStateOf("Location is not specified")

    var showIncorrectDataAlertDialog = mutableStateOf(false)
    var incorrectDataAlertDialogText = "Some of the fields are empty. Please, introduce:\n\n"+
            "\u2022" + "Type\n" + "\u2022" + "Category\n" + "\u2022" + "Wallet\n" + "\u2022" + "Date\n"+"\u2022" +"Amount"

    var authTokenJob: Job

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d(
                "EXCEPTION",
                "Thread exception while getting username on the transaction details screen"
            )
        }
        authTokenJob = viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getAccessToken.catch {
                Log.d(
                    "ERROR",
                    "EXPECTION while getting the token in the add screen"
                )
            }
                .collect {
                    authToken = it
                    Log.d("TOKEN", "Access token on transaction details screen: $authToken")
                }
        }

        val usernameJob: Job = viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getUsername.catch {
                Log.d(
                    "ERROR",
                    "Could not get Username from Data Store on Transaction details screen screen"
                )
            }
                .collect {
                    Log.d("TOKEN", "Username on Transaction details Screen: $it")
                    username = it
                }
        }
    }

    fun updateLocation(value: String) {
        locationState.value = value
    }

    fun chooseCategory(category: String) {
        val transaction =
            TransactionsRepository.updateExpenseCategory(category, this.transactionId)
        this.transaction.value = transaction
    }

    fun updateField(field: String, value: String?) {
        val transaction = TransactionsRepository.updateField(field, value, this.transactionId)
        this.transaction.value = transaction
    }

    fun updateTransactionInDb():Boolean {

        if(typeFieldTemporaryValueBeforeSavingtoDB.value == "" || categoryLinkTemporaryValueBeforeSavingtoDB == null ||
            walletLinkTemporaryValueBeforeSavingtoDB == null ||
            dateFieldTemporaryValueBeforeSavingtoDB == "Date" ||
            amountFieldTemporaryValueBeforeSavingtoDB == null){
            incorrectDataDialogShow()
            return false
        }

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when saving transaction to DB : $exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            updateTransactionInDb(
                transactionId = transactionId, transactionData = AddOrEditTransactionRequest(
                    name = nameFieldTemporaryValueBeforeSavingtoDB,
                    amount = amountFieldTemporaryValueBeforeSavingtoDB?.toInt(),
                    date = dateFieldTemporaryValueBeforeSavingtoDB,
                    comments = commentsFieldTemporaryValueBeforeSavingtoDB,
                    location = locationFieldTemporaryValueBeforeSavingtoDB,
                    type = typeFieldTemporaryValueBeforeSavingtoDB.value,
                    category = categoryLinkTemporaryValueBeforeSavingtoDB,
                    wallet = walletLinkTemporaryValueBeforeSavingtoDB,
                    username = username
                )
            )
        }
        return true
    }


    suspend fun updateTransactionInDb(
        transactionId: Int,
        transactionData: AddOrEditTransactionRequest
    ) {
        TransactionsRepository.updateTransactionInDb(transactionId, transactionData, authToken)
    }

    init {
    }


    suspend fun getFilteredTransactionCategories(): SecondAllExpenseCategoriesResponse {
        var listOfCategories = ExpanseCategoriesRepository.getFilteredExpenseCategories(authToken)
        return listOfCategories
    }

    suspend fun getFilteredWallets(): SecondAllWalletsResponse {
        var listOfWallets = WalletRepository.getFilteredWallets(authToken)
        return listOfWallets
    }


    fun setTransactionId(transactionId: Int) {
        if (transactionId === this.transactionId) {
            return
        }
        this.transactionId = transactionId
        val transaction = TransactionsRepository.getExpense(this.transactionId)
        this.transaction.value = transaction


        this.nameFieldTemporaryValueBeforeSavingtoDB = transaction.name
        this.amountFieldTemporaryValueBeforeSavingtoDB = transaction.amount.toString()
        this.typeFieldTemporaryValueBeforeSavingtoDB.value = transaction.type
        this.categoryNameFieldTemporaryValueBeforeSavingtoDB.value = transaction.categoryName
        this.categoryIconTemporaryValueBeforeSavingtoDB = transaction.categoryIcon
        this.walletNameFieldTemporaryValueBeforeSavingtoDB.value = transaction.walletName
        this.dateFieldTemporaryValueBeforeSavingtoDB = transaction.date
        this.commentsFieldTemporaryValueBeforeSavingtoDB = transaction.comments
        this.locationFieldTemporaryValueBeforeSavingtoDB = transaction.location
        this.photoUrlFieldTemporaryValueBeforeSavingtoDB = transaction.photoUrl
        this.categoryLinkTemporaryValueBeforeSavingtoDB = transaction.categoryId.toString()
        //LinkBuilder.buildCategoryLinkForAddingToExpanse(transaction.categoryId)
        this.walletLinkTemporaryValueBeforeSavingtoDB = transaction.walletId.toString()
        //LinkBuilder.buildWalletLinkForAddingToExpanse(transaction.walletId)
        this.datePicked.value = dateFieldTemporaryValueBeforeSavingtoDB as String

        this.locationState.value =
            if (transaction.location != null) transaction.location else "Location is not specified"


        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception setTransactionId")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            Thread.sleep(500)
            val transactionWallets = getFilteredWallets()
            transactionWalletsState.value = transactionWallets
            val transactionCategories = getFilteredTransactionCategories()
            transactionCetegoriesState.value = transactionCategories
            dataLoaded.value = true
        }
    }

    fun updateCategoryLinkValueBeforeSavingToDB(category: SecondAllExpenseCategoriesResponseItem) {
        this.categoryLinkTemporaryValueBeforeSavingtoDB = category.id.toString()
        //LinkBuilder.buildCategoryLinkForAddingToExpanse(categoryId = category.id)
        updateTemporaryFieldValueBeforeSavingToDB("categoryName", category.expanseCategoryName)
    }

    fun updateWalletLinkValueBeforeSavingToDB(wallet: SecondAllWalletsResponseItem) {
        this.walletLinkTemporaryValueBeforeSavingtoDB = wallet.id.toString()
        //LinkBuilder.buildWalletLinkForAddingToExpanse(walletId = wallet.id)
        updateTemporaryFieldValueBeforeSavingToDB("walletName", wallet.walletName)
    }

    fun updateTemporaryFieldValueBeforeSavingToDB(field: String, value: String) {
        when (field) {
            "amount" -> amountFieldTemporaryValueBeforeSavingtoDB = value
            "comments" -> commentsFieldTemporaryValueBeforeSavingtoDB = value
            "date" -> dateFieldTemporaryValueBeforeSavingtoDB = value
            "location" -> locationFieldTemporaryValueBeforeSavingtoDB = value
            "name" -> nameFieldTemporaryValueBeforeSavingtoDB = value
            "categoryName" -> categoryNameFieldTemporaryValueBeforeSavingtoDB.value = value
            "walletName" -> walletNameFieldTemporaryValueBeforeSavingtoDB.value = value
            "type" -> typeFieldTemporaryValueBeforeSavingtoDB.value = value
            "categoryIcon" -> categoryIconTemporaryValueBeforeSavingtoDB = value
        }

    }

    fun getFieldToUpdateInDB(field: String): String? {
        when (field) {
            "amount" -> return amountFieldTemporaryValueBeforeSavingtoDB.toString()
            "comments" -> return commentsFieldTemporaryValueBeforeSavingtoDB
            "date" -> return dateFieldTemporaryValueBeforeSavingtoDB
            "location" -> return locationFieldTemporaryValueBeforeSavingtoDB
            "name" -> return nameFieldTemporaryValueBeforeSavingtoDB
            "categoryName" -> return categoryNameFieldTemporaryValueBeforeSavingtoDB.value
            "walletName" -> return walletNameFieldTemporaryValueBeforeSavingtoDB.value
            "type" -> return typeFieldTemporaryValueBeforeSavingtoDB.value
            "categoryIcon" -> return categoryIconTemporaryValueBeforeSavingtoDB
        }
        return null
    }

    fun deleteTransaction(expenseId: Int) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while deleting the transaction : $exception")
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            //authTokenJob.join()
            Thread.sleep(500)
            Log.d("INFO", "Auth token for delete is $authToken")
            TransactionsRepository.deleteTransaction(expenseId, authToken)
        }
    }

    fun incorrectDataDialogShow(){
        showIncorrectDataAlertDialog.value = true
    }
    fun incorrectDataDialogClose(){
        showIncorrectDataAlertDialog.value = false
    }


}
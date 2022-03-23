package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.helpers.LinkBuilder
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.response.transactions.SecondAPI.*
import com.example.wallet.model.response.transactions.Wallet
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
    private var transactionId: Int = 0;
    var dataLoaded = mutableStateOf(false)

    //var transactionCetegoriesState = mutableStateOf((listOf(ExpanseCategory())))
    var transactionCetegoriesState =
        mutableStateOf((emptyList<SecondAllExpenseCategoriesResponseItem>()))
    var transactionWalletsState = mutableStateOf(emptyList<SecondAllWalletsResponseItem>())
    var transaction = mutableStateOf(SecondAllExpensesItem())

    var nameFieldTemporaryValueBeforeSavingtoDB: String? = null
    var amountFieldTemporaryValueBeforeSavingtoDB: String? = null
    var photoUrlFieldTemporaryValueBeforeSavingtoDB: String? = null
    var dateFieldTemporaryValueBeforeSavingtoDB: String? = null
    var commentsFieldTemporaryValueBeforeSavingtoDB: String? = null
    var locationFieldTemporaryValueBeforeSavingtoDB: String? = null

    //This is only Category Name Displayed, not the actual Category name!!
    var categoryNameFieldTemporaryValueBeforeSavingtoDB: String? = null
    var typeFieldTemporaryValueBeforeSavingtoDB: String? = null
    var walletNameFieldTemporaryValueBeforeSavingtoDB: String? = null

    var categoryLinkTemporaryValueBeforeSavingtoDB: String? = null
    var walletLinkTemporaryValueBeforeSavingtoDB: String? = null
    var categoryIconTemporaryValueBeforeSavingtoDB: String? = null

    var expandedCalendar = mutableStateOf(false)
    var datePicked = mutableStateOf("")

    var username: String = ""
    var authToken = ""

    var locationState = mutableStateOf("Location is not specified")

    var authTokenJob : Job

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

    fun updateLocation(value: String){
       locationState.value = value
    }
    fun chooseCategory(category: String) {
        val transaction =
            TransactionsRepository.updateExpenseCategory(category, this.transactionId);
        this.transaction.value = transaction
    }

    fun updateField(field: String, value: String?) {
        val transaction = TransactionsRepository.updateField(field, value, this.transactionId);
        this.transaction.value = transaction
    }

    fun updateTransactionInDb() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when saving transaction to DB")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            updateTransactionInDb(
                transactionId = transactionId, transactionData = AddOrEditTransactionRequest(
                    name = nameFieldTemporaryValueBeforeSavingtoDB,
                    amount = amountFieldTemporaryValueBeforeSavingtoDB?.toInt(),
                    date = dateFieldTemporaryValueBeforeSavingtoDB,
                    comments = commentsFieldTemporaryValueBeforeSavingtoDB,
                    location = locationFieldTemporaryValueBeforeSavingtoDB,
                    type = typeFieldTemporaryValueBeforeSavingtoDB,
                    category = categoryLinkTemporaryValueBeforeSavingtoDB,
                    wallet = walletLinkTemporaryValueBeforeSavingtoDB,
                    username = username
                )
            )
        }

    }


    suspend fun updateTransactionInDb(
        transactionId: Int,
        transactionData: AddOrEditTransactionRequest
    ) {
        TransactionsRepository.updateTransactionInDb(transactionId, transactionData)
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

    suspend fun getTransactionWallets(): List<Wallet> {
        var listOfWallets = WalletRepository.getWallets()._embedded.wallets
        return listOfWallets
    }

    fun setTransactionId(transactionId: Int) {
        if (transactionId === this.transactionId) {
            return;
        }
        this.transactionId = transactionId
        val transaction = TransactionsRepository.getExpense(this.transactionId)
        this.transaction.value = transaction


        this.nameFieldTemporaryValueBeforeSavingtoDB = transaction.name
        this.amountFieldTemporaryValueBeforeSavingtoDB = transaction.amount.toString()
        this.typeFieldTemporaryValueBeforeSavingtoDB = transaction.type
        this.categoryNameFieldTemporaryValueBeforeSavingtoDB = transaction.categoryName
        this.categoryIconTemporaryValueBeforeSavingtoDB = transaction.categoryIcon
        this.walletNameFieldTemporaryValueBeforeSavingtoDB = transaction.walletName
        this.dateFieldTemporaryValueBeforeSavingtoDB = transaction.date
        this.commentsFieldTemporaryValueBeforeSavingtoDB = transaction.comments
        this.locationFieldTemporaryValueBeforeSavingtoDB = transaction.location
        this.photoUrlFieldTemporaryValueBeforeSavingtoDB = transaction.photoUrl
        this.categoryLinkTemporaryValueBeforeSavingtoDB =
            LinkBuilder.buildCategoryLinkForAddingToExpanse(transaction.categoryId)
        this.walletLinkTemporaryValueBeforeSavingtoDB =
            LinkBuilder.buildWalletLinkForAddingToExpanse(transaction.walletId)
        this.datePicked.value = dateFieldTemporaryValueBeforeSavingtoDB as String

        this.locationState.value = if(transaction.location != null) transaction.location else "Location is not specified"


        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception setTransactionId")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            Thread.sleep(500)
            val transactionWallets = getFilteredWallets()
            transactionWalletsState.value = transactionWallets
            val transactionCategories = getFilteredTransactionCategories()
            transactionCetegoriesState.value = transactionCategories
            dataLoaded.value = true;
        }
    }

    fun updateCategoryLinkValueBeforeSavingToDB(category: SecondAllExpenseCategoriesResponseItem) {
        this.categoryLinkTemporaryValueBeforeSavingtoDB =
            LinkBuilder.buildCategoryLinkForAddingToExpanse(categoryId = category.id)
        updateTemporaryFieldValueBeforeSavingToDB("categoryName", category.expanseCategoryName)
    }

    fun updateWalletLinkValueBeforeSavingToDB(wallet: SecondAllWalletsResponseItem) {
        this.walletLinkTemporaryValueBeforeSavingtoDB =
            LinkBuilder.buildWalletLinkForAddingToExpanse(walletId = wallet.id)
        updateTemporaryFieldValueBeforeSavingToDB("walletName", wallet.walletName)
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
            "categoryName" -> return categoryNameFieldTemporaryValueBeforeSavingtoDB
            "walletName" -> return walletNameFieldTemporaryValueBeforeSavingtoDB
            "type" -> return typeFieldTemporaryValueBeforeSavingtoDB
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


}
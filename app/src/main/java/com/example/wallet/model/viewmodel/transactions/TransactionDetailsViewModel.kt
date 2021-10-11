package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.helpers.LinkBuilder
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.response.transactions.Wallet
import com.example.wallet.requests.AddOrEditTransactionRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionDetailsViewModel() : ViewModel() {
    private val categoriesRepository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
    private val walletRepository: WalletRepository = WalletRepository()
   // private val walletRepository: WalletRepository = WalletRepository()
    private var transactionId: Int = 0;
    var dataLoaded = mutableStateOf(false)
    var transactionCetegoriesState = mutableStateOf((listOf(ExpanseCategory())))
    var transactionWalletsState = mutableStateOf((listOf(Wallet())))
    var transaction = mutableStateOf(Expanse())
   // var chosenCategory= ExpanseCategory()


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


    fun chooseCategory(category: String) {
        val transaction = TransactionsRepository.updateExpenseCategory(category, this.transactionId);
        this.transaction.value = transaction
    }

    fun updateField(field: String, value: String?) {
        val transaction = TransactionsRepository.updateField(field, value, this.transactionId);
        this.transaction.value = transaction
    }

    fun updateTransactionInDb() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when saving to DB")
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
                    wallet = walletLinkTemporaryValueBeforeSavingtoDB
                )
            )
        }

    }


    suspend fun updateTransactionInDb(transactionId: Int, transactionData: AddOrEditTransactionRequest) {
         TransactionsRepository.updateTransactionInDb(transactionId, transactionData)
    }

    init {
    }

    suspend fun getTransactionCategories(): List<ExpanseCategory> {
        var listOfCategories = categoriesRepository.getExpanseCategories()._embedded.expanseCategories
        return listOfCategories
    }

    suspend fun getTransactionWallets(): List<Wallet>{
        var listOfWallets = walletRepository.getWallets()._embedded.wallets
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
        this.categoryLinkTemporaryValueBeforeSavingtoDB = LinkBuilder.buildCategoryLinkForAddingToExpanse(transaction.categoryId)
        this.walletLinkTemporaryValueBeforeSavingtoDB = LinkBuilder.buildWalletLinkForAddingToExpanse(transaction.walletId)


        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception setTransactionId")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            val transactionCategories = getTransactionCategories()
            val transactionWallets = getTransactionWallets()
            transactionCetegoriesState.value = transactionCategories
            transactionWalletsState.value = transactionWallets
            dataLoaded.value = true;
        }
    }

    fun updateCategoryLinkValueBeforeSavingToDB(category: ExpanseCategory) {
        this.categoryLinkTemporaryValueBeforeSavingtoDB = LinkBuilder.buildCategoryLinkForAddingToExpanse(categoryId = category.id)
        updateTemporaryFieldValueBeforeSavingToDB("categoryName", category.expanseCategoryName)
    }

    fun updateWalletLinkValueBeforeSavingToDB(wallet: Wallet){
        this.walletLinkTemporaryValueBeforeSavingtoDB = LinkBuilder.buildWalletLinkForAddingToExpanse(walletId = wallet.id)
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


}
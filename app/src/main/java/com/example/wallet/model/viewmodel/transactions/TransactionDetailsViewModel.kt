package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.requests.EditExpenseRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionDetailsViewModel() : ViewModel() {
    private val repository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
    private var transactionId: Int = 0;
    var dataLoaded = mutableStateOf(false)
    val transactionCetegoriesStateNames = mutableStateOf((listOf("")))
    var transaction = mutableStateOf(Expanse())


    var nameFieldTemporaryValueBeforeSavingtoDB: String? = null
    var amountFieldTemporaryValueBeforeSavingtoDB: String? = null
    var photoUrlFieldTemporaryValueBeforeSavingtoDB: String? = null
    var dateFieldTemporaryValueBeforeSavingtoDB: String? = null
    var commentsFieldTemporaryValueBeforeSavingtoDB: String? = null
    var locationFieldTemporaryValueBeforeSavingtoDB: String? = null
    var categoryNameFieldTemporaryValueBeforeSavingtoDB: String? = null
    var typeFieldTemporaryValueBeforeSavingtoDB: String? = null
    var walletFieldTemporaryValueBeforeSavingtoDB: String? = null


    fun chooseCategory(category: String) {
        val transaction =
            TransactionsRepository.updateExpenseCategory(category, this.transactionId);
        this.transaction.value = transaction
    }

    fun updateField(field: String, value: String?) {
        val transaction = TransactionsRepository.updateField(field, value, this.transactionId);
        this.transaction.value = transaction
    }

    fun updateTransactionInDb(){
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when saving to DB")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            updateTransactionInDb(transactionId =transactionId, transactionData = EditExpenseRequest(
                name = nameFieldTemporaryValueBeforeSavingtoDB,
                amount = amountFieldTemporaryValueBeforeSavingtoDB?.toInt(),
                date = dateFieldTemporaryValueBeforeSavingtoDB,
                comments = commentsFieldTemporaryValueBeforeSavingtoDB,
                location = locationFieldTemporaryValueBeforeSavingtoDB,
                type = typeFieldTemporaryValueBeforeSavingtoDB
            ))
        }

    }


    suspend fun updateTransactionInDb(transactionId: Int, transactionData: EditExpenseRequest) {
        val transactionAfterUpdate =
            TransactionsRepository.updateTransactionInDb(transactionId,transactionData)
    }

    init {
    }

    suspend fun getTransactionCategoriesNames(): List<String> {
        var listOfCategories = repository.getExpanseCategories()._embedded.expanseCategories
        var transactionCategoriesStrings = mutableListOf<String>()
        for (category in listOfCategories) {
            transactionCategoriesStrings.add(category.expanseCategoryName)
        }
        return transactionCategoriesStrings
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
        this.dateFieldTemporaryValueBeforeSavingtoDB = transaction.date
        this.commentsFieldTemporaryValueBeforeSavingtoDB = transaction.comments
        this.locationFieldTemporaryValueBeforeSavingtoDB = transaction.location
        this.photoUrlFieldTemporaryValueBeforeSavingtoDB = transaction.photoUrl
        /*TODO: Decide what to do with the walllet and category themselves... How to get category and wallet ID, Name etc...*/


        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception setTransactionId")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            val expanseCategories = getTransactionCategoriesNames()
            transactionCetegoriesStateNames.value = expanseCategories
            dataLoaded.value = true;
        }
    }

    fun updateTemporaryFieldValueBeforeSavingToDB(field: String, value: String) {
        when (field) {
            "amount" -> amountFieldTemporaryValueBeforeSavingtoDB = value
            "comments" -> commentsFieldTemporaryValueBeforeSavingtoDB = value
            "date" -> dateFieldTemporaryValueBeforeSavingtoDB = value
            "location" -> locationFieldTemporaryValueBeforeSavingtoDB = value
            "name" -> nameFieldTemporaryValueBeforeSavingtoDB = value
            "categoryName" -> categoryNameFieldTemporaryValueBeforeSavingtoDB = value
            "type" -> typeFieldTemporaryValueBeforeSavingtoDB = value
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
            "type" -> return typeFieldTemporaryValueBeforeSavingtoDB
        }
        return null
    }


}
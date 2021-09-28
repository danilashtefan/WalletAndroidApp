package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
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
    var typeFieldTemporaryValueBeforeSavingtoDB: String? = null
    var dateFieldTemporaryValueBeforeSavingtoDB: String? = null
    var commentFieldTemporaryValueBeforeSavingtoDB: String? = null
    var locationFieldTemporaryValueBeforeSavingtoDB: String? = null
    var categoryFieldTemporaryValueBeforeSavingtoDB: String? = null

    fun chooseCategory(category: String) {
        val transaction = TransactionsRepository.updateExpenseCategory(category, this.transactionId);
        this.transaction.value = transaction
    }

    fun updateField(field: String, value: String?) {
        val transaction = TransactionsRepository.updateField(field, value, this.transactionId);
        this.transaction.value = transaction
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
        this.categoryFieldTemporaryValueBeforeSavingtoDB = transaction.categoryName
        this.dateFieldTemporaryValueBeforeSavingtoDB = transaction.date
        this.commentFieldTemporaryValueBeforeSavingtoDB = transaction.comments


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
            "comments" -> commentFieldTemporaryValueBeforeSavingtoDB = value
            "date" -> dateFieldTemporaryValueBeforeSavingtoDB = value
            "location" -> locationFieldTemporaryValueBeforeSavingtoDB = value
            "name" -> nameFieldTemporaryValueBeforeSavingtoDB = value
            "categoryName" -> categoryFieldTemporaryValueBeforeSavingtoDB = value
            "type" -> typeFieldTemporaryValueBeforeSavingtoDB = value
        }

    }

    fun getFieldToUpdateInDB(field: String): String? {
        when (field) {
            "amount" -> return amountFieldTemporaryValueBeforeSavingtoDB.toString()
            "comments" -> return commentFieldTemporaryValueBeforeSavingtoDB
            "date" -> return dateFieldTemporaryValueBeforeSavingtoDB
            "location" -> return locationFieldTemporaryValueBeforeSavingtoDB
            "name" -> return nameFieldTemporaryValueBeforeSavingtoDB
            "categoryName" -> return categoryFieldTemporaryValueBeforeSavingtoDB
            "type" -> return typeFieldTemporaryValueBeforeSavingtoDB
        }
        return null
    }

}
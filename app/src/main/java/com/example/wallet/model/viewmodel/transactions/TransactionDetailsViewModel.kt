package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
import com.example.wallet.model.classesFromResponse.Transaction
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.ExpansesRepository
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.response.Expanses
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TransactionDetailsViewModel(): ViewModel() {
    private val repository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
    private var transactionId: Int = 0;
    var dataLoaded = mutableStateOf(false)
    val transactionCetegoriesStateNames = mutableStateOf((listOf("")))
    var expense = mutableStateOf<Transaction>(Transaction())

    fun chooseCategory(category: String) {
        val transaction = ExpansesRepository.updateExpenseCategory(category, this.transactionId);
        expense.value = Transaction(
            transaction.amount,
            transaction.comments,
            transaction.date,
            transaction.location,
            transaction.type,
            transaction.categoryName
        )
    }

    init {
    }

    suspend fun getExpanseCategories(): List<ExpanseCategory>{
        return repository.getExpanseCategories()._embedded.expanseCategories
    }

    suspend fun getTransactionCategoriesNames(): List<String>{
        var listOfCategories =  repository.getExpanseCategories()._embedded.expanseCategories
        var transactionCategoriesStrings = mutableListOf<String>()
        for(category in listOfCategories){
            transactionCategoriesStrings.add(category.expanseCategoryName)
        }
        return transactionCategoriesStrings
    }

    fun setTransactionId(transactionId: Int) {
        if (transactionId === this.transactionId) {
            return;
        }
        this.transactionId = transactionId
        val transaction = ExpansesRepository.getExpense(this.transactionId)
        expense.value = Transaction(
            transaction.amount,
            transaction.comments,
            transaction.date,
            transaction.location,
            transaction.type,
            transaction.categoryName
        )

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            val expanseCategories = getTransactionCategoriesNames()
            transactionCetegoriesStateNames.value = expanseCategories
            dataLoaded.value = true;
        }
    }

    fun loadViewData(transactionId: Int) {

    }

}
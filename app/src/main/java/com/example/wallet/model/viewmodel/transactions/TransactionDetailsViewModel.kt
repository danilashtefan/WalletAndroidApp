package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.ExpansesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionDetailsViewModel(): ViewModel() {
    private val repository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
    private var transactionId: Int = 0;
    var dataLoaded = mutableStateOf(false)
    val transactionCetegoriesStateNames = mutableStateOf((listOf("")))
    var transaction = mutableStateOf(Expanse())

    fun chooseCategory(category: String) {
        val transaction = ExpansesRepository.updateExpenseCategory(category, this.transactionId);
        this.transaction.value = transaction
    }

    fun updateField(field: String, value: String) {
        val transaction = ExpansesRepository.updateField(field, value, this.transactionId);
        this.transaction.value = transaction
    }

    init {
    }

//    suspend fun getExpanseCategories(): List<ExpanseCategory>{
//        return repository.getExpanseCategories()._embedded.expanseCategories
//    }


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
        this.transaction.value = transaction

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception setTransactionId")
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
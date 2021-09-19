package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.response.ExpanseCategory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TransactionDetailsViewModel(private val repository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()):ViewModel() {
    var chosenCategory = mutableStateOf("")
    var dataLoaded = mutableStateOf(false)
    val transactionCetegoriesStateNames = mutableStateOf((emptyList<String>()))

    fun chooseCategory(category: String) {
        this.chosenCategory.value = category;
    }

    init{
        transactionCetegoriesStateNames.value = listOf("")
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION","Thread exception")
        }

        viewModelScope.launch(handler+ Dispatchers.IO){
           val expanseCategories = getTransactionCategoriesNames()
           transactionCetegoriesStateNames.value = expanseCategories
            dataLoaded.value = true;
        }

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

}
package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpansesViewModel(
    private val expansesRepository: TransactionsRepository = TransactionsRepository,
    private val expanseCategoriesRepository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
) : ViewModel() {

    var minDatePicked = mutableStateOf("Start Date")
    var maxDatePicked = mutableStateOf("End Date")
    var expandedCalendarMin = mutableStateOf(false)
    var expandedCalendarMax = mutableStateOf(false)
    val expansesState = mutableStateOf((emptyList<Expanse>()))
    var dataLoaded = mutableStateOf(false)
    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            var expanses = getExpanses()
            for (expanse in expanses) {
             expanse.categoryName = getAndSetCategoriesForExpanses(expanse.id)
            }
            expansesState.value = expanses
            dataLoaded.value = true
        }
    }

    suspend fun getExpanses(): List<Expanse> {
        return expansesRepository.getExpanses()._embedded.expanses
    }

   // Method to get the category of particular expanse
    suspend fun getAndSetCategoriesForExpanses(expanseId : Int):String{
      return expanseCategoriesRepository.getCategoryForExpanse(expanseId).expanseCategoryName
    }

    fun getTransaction(transactionId: Int): Expanse {
        for(tx in expansesState.value) {
            if (tx.id === transactionId) {
                return tx;
            }
        }
        throw Exception("No transaction found!");
    }

    fun getCurrency(expanse: Expanse) {

    }

    fun getSign(expanse: Expanse){

    }


}
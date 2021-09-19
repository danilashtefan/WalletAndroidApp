package com.example.wallet.model.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.ExpansesRepository
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.response.SingleExpanseCategoryResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpansesViewModel(
    private val expansesRepository: ExpansesRepository = ExpansesRepository(),
    private val expanseCategoriesRepository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
) : ViewModel() {


    val expansesState = mutableStateOf((emptyList<Expanse>()))

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Network exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            var expanses = getExpanses()
            for (expanse in expanses) {
             expanse.categoryName = getAndSetCategoriesForExpanses(expanse.id)
            }
            expansesState.value = expanses
        }
    }

    suspend fun getExpanses(): List<Expanse> {
        return expansesRepository.getExpanses()._embedded.expanses
    }

   // Method to get the category of particular expanse
    suspend fun getAndSetCategoriesForExpanses(expanseId : Int):String{
      return expanseCategoriesRepository.getCategoryForExpanse(expanseId).expanseCategoryName
    }

    fun getCurrency(expanse: Expanse) {
        
    }

    fun getSign(expanse: Expanse){

    }


}
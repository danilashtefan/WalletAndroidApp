package com.example.wallet.model.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.response.ExpanseCategory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpanseCategoriesViewModel(private val repository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()): ViewModel() {

    val expanseCategoriesState = mutableStateOf((emptyList<ExpanseCategory>()))
    init{
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION","Network exception")
        }

        viewModelScope.launch(handler+ Dispatchers.IO){
            val expanseCategories = getExpanseCategories()
            expanseCategoriesState.value = expanseCategories
        }
    }
    suspend fun getExpanseCategories(): List<ExpanseCategory>{
        return repository.getExpanseCategories()._embedded.expanseCategories
    }

}
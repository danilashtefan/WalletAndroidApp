package com.example.wallet.model.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.ExpansesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpansesViewModel (private val repository: ExpansesRepository = ExpansesRepository()):ViewModel(){


   val expansesState = mutableStateOf((emptyList<Expanse>()))

   init{
       val handler = CoroutineExceptionHandler { _, exception ->
           Log.d("EXCEPTION","Network exception")
       }

          viewModelScope.launch(handler+Dispatchers.IO){
             val expanses = getExpanses()
             expansesState.value = expanses
          }
   }


suspend fun getExpanses():List<Expanse> {
   return repository.getExpanses()._embedded.expanses

}

}
package com.example.wallet.model

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.model.*
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
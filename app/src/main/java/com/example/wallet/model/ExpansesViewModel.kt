package com.example.wallet.model

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpansesViewModel (private val repository: ExpansesRepository = ExpansesRepository()):ViewModel(){


   val expansesState = mutableStateOf((emptyList<Expanse>()))
   init{
          viewModelScope.launch(Dispatchers.IO){
             val expanses = getExpanses()
             expansesState.value = expanses
          }
   }


suspend fun getExpanses():List<Expanse> {
   return repository.getExpanses()._embedded.expanses

}

}
package com.example.wallet.ui

import androidx.lifecycle.ViewModel
import com.example.wallet.model.*

class ExpansesViewModel (private val repository: ExpansesRepository = ExpansesRepository()):ViewModel(){
suspend fun getMeals():List<Expanse> {
   return repository.getExpanses()._embedded.expanses

}

}
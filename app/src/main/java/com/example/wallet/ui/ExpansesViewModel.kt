package com.example.wallet.ui

import androidx.lifecycle.ViewModel
import com.example.wallet.model.*

class ExpansesViewModel (private val repository: ExpansesRepository = ExpansesRepository()):ViewModel(){
fun getMeals(successfulCallback:(response:AllExpansesResponse)->Unit) {
   return repository.getExpanses(){ response ->
      if (response != null) {
         successfulCallback(response)
      }

   }
}

}
package com.example.wallet.model.viewmodel

import androidx.lifecycle.ViewModel
import com.example.wallet.model.repository.ExpanseCategoryRepository
import com.example.wallet.model.repository.ExpansesRepository
import com.example.wallet.model.response.ExpanseCategory

class ExpanseCategoriesViewModel(private val repository: ExpanseCategoryRepository = ExpanseCategoryRepository()): ViewModel() {

    suspend fun getExpanseCategories(): List<ExpanseCategory>{
        return repository.getExpanseCategories()._embedded.expanseCategories
    }

}
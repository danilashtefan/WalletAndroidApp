package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.repository.WalletRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpansesViewModel(
    private val expansesRepository: TransactionsRepository = TransactionsRepository,
    private val transactionCategoriesRepository: ExpanseCategoriesRepository = ExpanseCategoriesRepository(),
    private val transactionWalletsRepository: WalletRepository = WalletRepository()
) : ViewModel() {

    var minDatePicked = mutableStateOf("Start Date")
    var maxDatePicked = mutableStateOf("End Date")
    var expandedCalendarMin = mutableStateOf(false)
    var expandedCalendarMax = mutableStateOf(false)
    val transactionState = mutableStateOf((emptyList<Expanse>()))
    var dataLoaded = mutableStateOf(false)

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            var expanses = getExpanses()
            for (transaction in expanses) {
                val transactionCategoryNameAndIdAndIcon = getAndSetCategoriesForTransactions(transaction.id)
                transaction.categoryName = transactionCategoryNameAndIdAndIcon.first
                transaction.categoryId = transactionCategoryNameAndIdAndIcon.second
                transaction.categoryIcon = transactionCategoryNameAndIdAndIcon.third
                val transactionWalletNameAndId = getAndSetWalletForTransactions(transaction.id)
                transaction.walletName = transactionWalletNameAndId.first
                transaction.walletId = transactionWalletNameAndId.second
            }
            transactionState.value = expanses
            dataLoaded.value = true
        }
    }

    suspend fun getExpanses(): List<Expanse> {
        return expansesRepository.getExpanses()._embedded.expanses
    }

    // Method to get the category of particular expanse
    suspend fun getAndSetCategoriesForTransactions(expanseId: Int): Triple<String, Int, String> {
        val categoryResponse = transactionCategoriesRepository.getCategoryForExpanse(expanseId)
        return Triple(categoryResponse.expanseCategoryName, categoryResponse.id, categoryResponse.icon)
    }

    suspend fun getAndSetWalletForTransactions(expanseId: Int): Pair<String, Int> {
        val walletResponse = transactionWalletsRepository.getWalletForExpanse(expanseId)
        return Pair(walletResponse.walletName, walletResponse.id)
    }

    fun getTransaction(transactionId: Int): Expanse {
        for (tx in transactionState.value) {
            if (tx.id === transactionId) {
                return tx;
            }
        }
        throw Exception("No transaction found!");
    }

    fun getCurrency(expanse: Expanse) {

    }

    fun getSign(expanse: Expanse) {

    }


}
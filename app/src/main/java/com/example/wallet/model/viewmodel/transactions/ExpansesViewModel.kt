package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.model.Expanse
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ExpansesViewModel(
    private val dataStorePreferenceRepository: DataStorePreferenceRepository
) : ViewModel() {
    private val expansesRepository: TransactionsRepository = TransactionsRepository
    private val transactionCategoriesRepository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
    private val transactionWalletsRepository: WalletRepository = WalletRepository()
    var minDatePicked = mutableStateOf("Start Date")
    var maxDatePicked = mutableStateOf("End Date")
    private val _accessToken = MutableLiveData("")
    var accessToken = ""
    var expandedCalendarMin = mutableStateOf(false)
    var expandedCalendarMax = mutableStateOf(false)
    //val transactionState = mutableStateOf((emptyList<Expanse>()))
    val transactionState = mutableStateOf((emptyList<SecondAllExpensesItem>()))
    var dataLoaded = mutableStateOf(false)
    var totalExpenses = 0

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while fetching expanses to the initial screen")
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getAccessToken.
            catch { Log.d("ERROR","EXPECTION while getting the token in the expense screen") }
                .collect{
                    Log.d("TOKEN","Access token on all expense screen: $it")
                    accessToken = it
                }
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            while (accessToken.equals("")){
                Log.d("INFO","Access token is not set up yet")
            }
            var expanses = getFilteredExpenses()
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


        }
        dataLoaded.value = true
    }

    //Method to get expenses from the JpaRepository default API, there is no filtering by username avalable
    suspend fun getExpanses(): List<Expanse> {
        Log.d("INFO","getExpanses is called")
        return expansesRepository.getExpanses(accessToken)._embedded.expanses
    }

    //Method to get expenses filtered by the token used
    suspend fun getFilteredExpenses(): SecondAllExpensesResponse {
        Log.d("INFO","getFiltered expenses is called")
        return expansesRepository.getFilteredExpanses(accessToken)
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

    fun getTransaction(transactionId: Int): SecondAllExpensesItem {
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
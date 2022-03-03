package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.CategoryWrapperWithColor
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class CategoryStatisticsViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModel()  {

    private var categoryId: Int = 0
    var dataLoaded = mutableStateOf(false)

    //Transactions of the particular wallet
    val transactionState = mutableStateOf((emptyList<CategoryWrapperWithColor>()))
    var expanseState = mutableStateOf((emptyList<CategoryWrapperWithColor>()))
    var incomeState = mutableStateOf((emptyList<CategoryWrapperWithColor>()))

    //Total amount to show on the screen
    var amount = mutableStateOf(0)

    var username: String = ""
    var authToken = ""
    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d(
                "EXCEPTION",
                "Thread exception while getting username on the category statistics screen: $exception"
            )
        }

        val authTokenJob = viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getAccessToken.catch {
                Log.d(
                    "ERROR",
                    "EXPECTION while getting the token in the category statistics screen"
                )
            }
                .collect {
                    authToken = it
                    Log.d("TOKEN", "Access token on category statistics screen: $authToken")
                }
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            while (authToken.equals("")) {
                Log.d("INFO", "Access token is not set up yet")
            }
            var transactions = getFilteredExpenses()
            var wrappedTransactions = arrayListOf<CategoryWrapperWithColor>()
            val rnd = Random()
            for (transaction in transactions){
                wrappedTransactions.add(CategoryWrapperWithColor(
                    transaction = transaction, Color(
                    (0..255).random(),
                    (0..255).random(),
                    (0..255).random())))
            }
            transactionState.value = wrappedTransactions

            var totalAmountTemp = 0
            for (transaction in wrappedTransactions){
                if(transaction.transaction.type.equals("Expense")){
                    totalAmountTemp -= transaction.transaction.amount
                    //toMutableList().also{it.remove(expanse)}
//                    expanseState.value.toMutableList().also{it.add(transaction)}
                    expanseState.value += transaction
                }else{
                    totalAmountTemp += transaction.transaction.amount
                   // incomeState.value.toMutableList().also{it.add(transaction)}
                    incomeState.value += transaction
                }
            }
            amount.value = totalAmountTemp

        }
        dataLoaded.value = true
    }

    fun setCategoryId(id:Int){
        categoryId = id
    }

    suspend fun getFilteredExpenses(): SecondAllExpensesResponse {
        Log.d("INFO","getFiltered expenses is called")
        return TransactionsRepository.getCategoryFilteredExpenses(authToken = authToken, categoryId = categoryId)

    }

}
package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.response.transactions.SecondAPI.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReportViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository):
    ViewModel(){
    var dataLoaded = mutableStateOf(false)
    var minDatePicked = mutableStateOf("1000-01-01")
    var maxDatePicked = mutableStateOf("3000-12-12")
    private val _accessToken = MutableLiveData("")
    var authToken = ""

    var expandedCalendarMin = mutableStateOf(false)
    var expandedCalendarMax = mutableStateOf(false)
    var topExpense = mutableStateOf(SecondAllExpensesItem())
    var topIncome = mutableStateOf(SecondAllExpensesItem())

    var topExpenseCategory = mutableStateOf(TopExpenseCategoryWithAmountResponse())
    var topIncomeCategory = mutableStateOf(TopExpenseCategoryWithAmountResponse())

    var topExpenseWallet = mutableStateOf(SecondAllWalletsResponseItem())
    var topIncomeWallet = mutableStateOf(SecondAllWalletsResponseItem())

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while fetching to the report screen: $exception")
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getAccessToken.catch {
                Log.d(
                    "ERROR",
                    "EXPECTION while getting the token in the report screen"
                )
            }
                .collect {
                    Log.d("TOKEN", "Access token on report screen: $it")
                    authToken = it
                }
        }
            viewModelScope.launch(handler + Dispatchers.IO) {
                while (authToken.equals("")) {
                    Log.d("INFO", "Access token is not set up yet")
                }
            topExpenseCategory.value = getTopExpenseCategory()
            Log.d("INFO", "Top expense category: ${topExpenseCategory.value}")
            topIncomeCategory.value = getTopIncomeCategory()
        }
        dataLoaded.value = true
    }


    suspend fun getTopExpenseCategory(): TopExpenseCategoryWithAmountResponse {
        Log.d("INFO", "getFiltered expenses is called")
        return ExpanseCategoriesRepository.getTopExpenseCategory(authToken)
    }

    suspend fun getTopIncomeCategory(): TopExpenseCategoryWithAmountResponse {
        Log.d("INFO", "getFiltered expenses is called")
        return ExpanseCategoriesRepository.getTopIncomeCategory(authToken)
    }
}
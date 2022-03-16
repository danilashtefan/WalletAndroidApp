package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.CategoryWrapperWithColor
import com.example.wallet.model.TransactionWrapperWithColor
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.transactions.SecondAPI.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class ReportViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModel() {
    var dataLoaded = mutableStateOf(false)
    var minDatePicked = mutableStateOf("1000-01-01")
    var maxDatePicked = mutableStateOf("3000-12-12")
    private val _accessToken = MutableLiveData("")
    var authToken = ""

    var expandedCalendarMin = mutableStateOf(false)
    var expandedCalendarMax = mutableStateOf(false)

    var allCategories = mutableStateOf(emptyList<CategoryWrapperWithColor>())
    var totalCategoriesExpenses = mutableStateOf(0)
    var totalCategoriesIncomes = mutableStateOf(0)
    var topExpense = mutableStateOf(SecondAllExpensesItem())
    var topIncome = mutableStateOf(SecondAllExpensesItem())

    var topExpenseCategory = mutableStateOf(TopExpenseCategoryWithAmountResponse())
    var topIncomeCategory = mutableStateOf(TopExpenseCategoryWithAmountResponse())

    var topExpenseWallet = mutableStateOf(TopWalletWithAmountResponse())
    var topIncomeWallet = mutableStateOf(TopWalletWithAmountResponse())


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

        val dataFetchingJob: Job = viewModelScope.launch(handler + Dispatchers.IO) {
            while (authToken.equals("")) {
                Log.d("INFO", "Access token is not set up yet")
            }
            topExpenseCategory.value = getTopExpenseCategory()
            topIncomeCategory.value = getTopIncomeCategory()
            topExpenseWallet.value = getTopExpenseWallet()
            topIncomeWallet.value = getTopIncomeWallet()
            var categoriesWithExpenses = getCategoriesWithExpenses() as ArrayList<TopExpenseCategoryWithAmountResponse>
            var wrappedCategoriesWithExpenses = arrayListOf<CategoryWrapperWithColor>()
            for(category in categoriesWithExpenses){
                wrappedCategoriesWithExpenses.add(CategoryWrapperWithColor(
                    category = category, Color(
                        (0..255).random(),
                        (0..255).random(),
                        (0..255).random())
                ))
            }
            allCategories.value = wrappedCategoriesWithExpenses






            dataLoaded.value = true;
        }
    }

    suspend fun getCategoriesWithExpenses():List<TopExpenseCategoryWithAmountResponse>{
        return ExpanseCategoriesRepository.getCategoriesWithExpenses(authToken)
    }
    suspend fun getTopExpenseCategory(): TopExpenseCategoryWithAmountResponse {
        return ExpanseCategoriesRepository.getTopExpenseCategory(authToken)
    }

    suspend fun getTopIncomeCategory(): TopExpenseCategoryWithAmountResponse {
        return ExpanseCategoriesRepository.getTopIncomeCategory(authToken)
    }

    suspend fun getTopExpenseWallet(): TopWalletWithAmountResponse {
        return WalletRepository.getTopExpenseWallet(authToken)
    }

    suspend fun getTopIncomeWallet(): TopWalletWithAmountResponse {
        return WalletRepository.getTopIncomeWallet(authToken)
    }


}
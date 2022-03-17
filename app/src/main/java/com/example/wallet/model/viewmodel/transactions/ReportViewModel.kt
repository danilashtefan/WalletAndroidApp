package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.CategoryWrapperWithColor
import com.example.wallet.model.WalletWrapperWithColor
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

    var topExpense = mutableStateOf(SecondAllExpensesItem())
    var topIncome = mutableStateOf(SecondAllExpensesItem())


    var allCategories = mutableStateOf(emptyList<CategoryWrapperWithColor>())
    var totalCategoriesExpenses = mutableStateOf(0)
    var totalCategoriesIncomes = mutableStateOf(0)
    var topExpenseCategory = mutableStateOf(TopExpenseCategoryWithAmountResponse())
    var topIncomeCategory = mutableStateOf(TopExpenseCategoryWithAmountResponse())


    var allWallets = mutableStateOf(emptyList<WalletWrapperWithColor>())
    var totalWalletsExpenses = mutableStateOf(0)
    var totalWalletsIncomes = mutableStateOf(0)
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

            var walletsWithExpenses = getWalletsWithExpenses() as ArrayList<TopWalletWithAmountResponse>
            var wrappedWalletsWithExpenses = arrayListOf<WalletWrapperWithColor>()

            for(category in categoriesWithExpenses){
                wrappedCategoriesWithExpenses.add(CategoryWrapperWithColor(
                    category = category, Color(
                        (0..255).random(),
                        (0..255).random(),
                        (0..255).random())
                ))
            }

            for(wallet in walletsWithExpenses){
                wrappedWalletsWithExpenses.add(
                    WalletWrapperWithColor(
                    wallet = wallet,Color(
                            (0..255).random(),
                            (0..255).random(),
                            (0..255).random())
                ))
            }
            allCategories.value = wrappedCategoriesWithExpenses
            allWallets.value = wrappedWalletsWithExpenses

            var categoryExpenseAmountTemp = 0
            var categoryIncomeAmountTemp = 0

            for(category in allCategories.value){
                categoryExpenseAmountTemp += category.category.expenseAmount
                categoryIncomeAmountTemp += category.category.incomeAmount
            }
            totalCategoriesExpenses.value = categoryExpenseAmountTemp
            totalCategoriesIncomes.value = categoryIncomeAmountTemp

            var walletExpenseAmountTemp = 0
            var walletIncomeAmountTemp = 0

            for(wallet in allWallets.value){
                walletExpenseAmountTemp += wallet.wallet.expenseAmount
                walletIncomeAmountTemp += wallet.wallet.incomeAmount
            }
            totalWalletsExpenses.value = walletExpenseAmountTemp
            totalWalletsIncomes.value = walletIncomeAmountTemp

            dataLoaded.value = true;
        }
    }
    suspend fun getWalletsWithExpenses():List<TopWalletWithAmountResponse>{
        return WalletRepository.getWalletsWithExpenses(authToken)
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
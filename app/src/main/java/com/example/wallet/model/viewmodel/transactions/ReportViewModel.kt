package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.helpers.DateFormatter
import com.example.wallet.model.CategoryWrapperWithColor
import com.example.wallet.model.WalletWrapperWithColor
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.transactions.SecondAPI.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat

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


    var allCategories = emptyList<CategoryWrapperWithColor>()
    var filteredAllCategories = mutableStateOf(emptyList<CategoryWrapperWithColor>())
    var totalCategoriesExpenses = mutableStateOf(0)
    var totalCategoriesIncomes = mutableStateOf(0)
    var topExpenseCategory = mutableStateOf(TopExpenseCategoryWithAmountResponse())
    var topIncomeCategory = mutableStateOf(TopExpenseCategoryWithAmountResponse())


    val allWallets = mutableStateOf(emptyList<WalletWrapperWithColor>())
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

        loadScreen()
    }

    fun loadScreen() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while fetching to the report screen: $exception")
        }
       // dataLoaded.value = false
        val dataFetchingJob: Job = viewModelScope.launch(handler + Dispatchers.IO) {
            while (authToken.equals("")) {
                Log.d("INFO", "Access token is not set up yet")
            }

            var categoriesWithExpenses =
                getCategoriesWithExpenses() as ArrayList<TopExpenseCategoryWithAmountResponse>
            var wrappedCategoriesWithExpenses = arrayListOf<CategoryWrapperWithColor>()

            var walletsWithExpenses =
                getWalletsWithExpenses() as ArrayList<TopWalletWithAmountResponse>
            var wrappedWalletsWithExpenses = arrayListOf<WalletWrapperWithColor>()

            for (category in categoriesWithExpenses) {
                wrappedCategoriesWithExpenses.add(
                    CategoryWrapperWithColor(
                        category = category, Color(
                            (0..255).random(),
                            (0..255).random(),
                            (0..255).random()
                        )
                    )
                )
            }

            for (wallet in walletsWithExpenses) {
                wrappedWalletsWithExpenses.add(
                    WalletWrapperWithColor(
                        wallet = wallet, Color(
                            (0..255).random(),
                            (0..255).random(),
                            (0..255).random()
                        )
                    )
                )
            }

            allCategories = wrappedCategoriesWithExpenses
            filteredAllCategories.value = allCategories.map { it.copy() }

            allWallets.value = wrappedWalletsWithExpenses

            topExpenseCategory.value = getTopExpenseCategory() //Need to filter by time
            topIncomeCategory.value = getTopIncomeCategory() //Need to filter by time
            topExpenseWallet.value = getTopExpenseWallet() //Need to filter by time
            topIncomeWallet.value = getTopIncomeWallet() //Need to filter by time

            var categoryExpenseAmountTemp = 0
            var categoryIncomeAmountTemp = 0

            for (category in allCategories) {
                categoryExpenseAmountTemp += category.category.expenseAmount
                categoryIncomeAmountTemp += category.category.incomeAmount
            }
            totalCategoriesExpenses.value = categoryExpenseAmountTemp
            totalCategoriesIncomes.value = categoryIncomeAmountTemp

            var walletExpenseAmountTemp = 0
            var walletIncomeAmountTemp = 0

            for (wallet in allWallets.value) {
                walletExpenseAmountTemp += wallet.wallet.expenseAmount
                walletIncomeAmountTemp += wallet.wallet.incomeAmount
            }
            totalWalletsExpenses.value = walletExpenseAmountTemp
            totalWalletsIncomes.value = walletIncomeAmountTemp

            dataLoaded.value = true;
        }
    }

//    fun reLoadScreen(){
//        filterCategories()
//    }

//    private fun filterCategories() {
//        val tempFilteredCategories = allCategories.map { it.copy() }
//        for (category in tempFilteredCategories.map{it.copy()}) {
//            var filteredExpenses = category.category.expenses.filter {
//                SimpleDateFormat("yyyy-MM-dd").parse(DateFormatter.formatDate(it.date)) >= SimpleDateFormat(
//                    "yyyy-MM-dd"
//                ).parse(minDatePicked.value) &&
//                        SimpleDateFormat("yyyy-MM-dd").parse(DateFormatter.formatDate(it.date)) <= SimpleDateFormat(
//                    "yyyy-MM-dd"
//                ).parse(maxDatePicked.value)
//            }
//            category.category.expenses = filteredExpenses
//        }
//
//        for(category in tempFilteredCategories){
//            var expenses = category.category.expenses
//            var totalExpensesAmount = 0
//            var totalIncomesAmount = 0
//
//            for(expense in expenses){
//                if(expense.type.equals("Expense")){
//                    totalExpensesAmount += expense.amount
//                } else if(expense.type.equals("Income")){
//                    totalIncomesAmount += expense.amount
//                }
//            }
//            category.category.incomeAmount = totalIncomesAmount
//            category.category.expenseAmount = totalExpensesAmount
//        }
//        filteredAllCategories.value = tempFilteredCategories.map { it.copy() }
//
//        var categoryExpenseAmountTemp = 0
//        var categoryIncomeAmountTemp = 0
//
//        for (category in filteredAllCategories.value) {
//            categoryExpenseAmountTemp += category.category.expenseAmount
//            categoryIncomeAmountTemp += category.category.incomeAmount
//        }
//        totalCategoriesExpenses.value = categoryExpenseAmountTemp
//        totalCategoriesIncomes.value = categoryIncomeAmountTemp
//
//    }



    suspend fun getCategoriesWithExpenses():List<TopExpenseCategoryWithAmountResponse>{
        return ExpanseCategoriesRepository.getCategoriesWithExpenses(authToken, minDatePicked.value, maxDatePicked.value)
    }
    suspend fun getTopExpenseCategory(): TopExpenseCategoryWithAmountResponse {
        return ExpanseCategoriesRepository.getTopExpenseCategory(authToken, minDatePicked.value, maxDatePicked.value)
    }

    suspend fun getTopIncomeCategory(): TopExpenseCategoryWithAmountResponse {
        return ExpanseCategoriesRepository.getTopIncomeCategory(authToken, minDatePicked.value, maxDatePicked.value)
    }




    suspend fun getWalletsWithExpenses():List<TopWalletWithAmountResponse>{
        return WalletRepository.getWalletsWithExpenses(authToken, minDatePicked.value, maxDatePicked.value)
    }

    suspend fun getTopExpenseWallet(): TopWalletWithAmountResponse {
        return WalletRepository.getTopExpenseWallet(authToken, minDatePicked.value, maxDatePicked.value)
    }

    suspend fun getTopIncomeWallet(): TopWalletWithAmountResponse {
        return WalletRepository.getTopIncomeWallet(authToken, minDatePicked.value, maxDatePicked.value)
    }





}
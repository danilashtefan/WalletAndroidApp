package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Math.abs

class ExpansesViewModel(
    private val dataStorePreferenceRepository: DataStorePreferenceRepository
) : ViewModel() {
    private val expansesRepository: TransactionsRepository = TransactionsRepository
    var minDatePicked = mutableStateOf("1000-01-01")
    var maxDatePicked = mutableStateOf("3000-12-12")
    private val _accessToken = MutableLiveData("")
    var authToken = ""
    var expandedCalendarMin = mutableStateOf(false)
    var expandedCalendarMax = mutableStateOf(false)

    //val transactionState = mutableStateOf((emptyList<Expanse>()))
    val transactionState = mutableStateOf((emptyList<SecondAllExpensesItem>()))
    var expanseState = mutableStateOf((emptyList<SecondAllExpensesItem>()))
    var incomeState = mutableStateOf((emptyList<SecondAllExpensesItem>()))
    var dataLoaded = mutableStateOf(false)
    var totalExpenses = mutableStateOf(0)
    var totalIncome = mutableStateOf(0)
    var sortedBy = mutableStateOf("No sort")
    var budgetSet = mutableStateOf(0)
    var budgetLeft = mutableStateOf(0)

    var showBudgetSetDialog = mutableStateOf(false)
    var showLowBudgetAlertDialog = mutableStateOf(false)
    var userAknowledgedAboutLowBudget = mutableStateOf(false)

    val lowBugetDialogText = mutableStateOf("You have exceeded the budget set!")


    init {
        if (budgetLeft.value < 0) {
            showLowBudgetAlertDialog.value = true
        }
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d(
                "EXCEPTION",
                "Thread exception while fetching expanses to the initial screen: $exception"
            )
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getAccessToken.catch {
                Log.d(
                    "ERROR",
                    "EXPECTION while getting the token in the expense screen"
                )
            }
                .collect {
                    Log.d("TOKEN", "Access token on all expense screen: $it")
                    authToken = it
                    fetchExpensesToTheScreen()
                }
        }

        dataLoaded.value = true
    }

    private suspend fun ExpansesViewModel.fetchExpensesToTheScreen() {
        var transactions = getFilteredExpenses()
        for (transaction in transactions) {
            val transactionCategoryNameAndIdAndIcon =
                getAndSetCategoriesForTransactions(transaction.id)
            transaction.categoryName = transactionCategoryNameAndIdAndIcon.first
            transaction.categoryId = transactionCategoryNameAndIdAndIcon.second
            transaction.categoryIcon = transactionCategoryNameAndIdAndIcon.third
            val transactionWalletNameAndId =
                getAndSetWalletForTransactions(authToken, transaction.id)
            transaction.walletName = transactionWalletNameAndId.first
            transaction.walletId = transactionWalletNameAndId.second
        }

        transactionState.value = transactions
        calculateAmounts(transactions)

        dataStorePreferenceRepository.getBudget.catch {
            Log.d(
                "ERROR",
                "Could not get Budget from Data Store on Expenses screen"
            )
        }
            .collect {
                updateBudgetSet(it)
                updateBudgetLeft_()
            }
    }

    fun calculateAmounts(transactions: SecondAllExpensesResponse) {
        var totalExpensesTemp = 0
        var totalIncomeTemp = 0
        for (transaction in transactions) {
            if (transaction.type.equals("Expense")) {
                totalExpensesTemp += transaction.amount
            }
            if (transaction.type.equals("Income")) {
                totalIncomeTemp += transaction.amount
            }
        }
        totalExpenses.value = totalExpensesTemp
        totalIncome.value = totalIncomeTemp
    }

    private fun updateBudgetLeft_() {
        budgetLeft.value = budgetSet.value - abs(totalExpenses.value)
        if (budgetLeft.value < 0) {
            showLowBudgetAlertDialog.value = true
        }
    }

    private fun updateBudgetSet(it: String) {
        budgetSet.value = it.toInt()
    }

    fun dismissLowBudgetAlertDialog() {
        showLowBudgetAlertDialog.value = false
        setUserAknowledgedAboutLowBudgetValue()
    }

    fun setUserAknowledgedAboutLowBudgetValue() {
        userAknowledgedAboutLowBudget.value = true
    }

    fun showBudgetSetAlertDialog() {
        showBudgetSetDialog.value = true
    }

    fun updateSortBy(value: String) {
        sortedBy.value = value
    }

    fun updateBudgetSet(value: Int) {
        budgetSet.value = value
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d(
                "EXCEPTION",
                "Thread exception while setting budget on the expenses screen: $exception"
            )
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.setBudget(value.toString())
        }
    }

    fun updateBudgetLeft() {
        budgetLeft.value = budgetSet.value - abs(totalExpenses.value)
    }


    //Method to get expenses filtered by the token used
    suspend fun getFilteredExpenses(): SecondAllExpensesResponse {
        Log.d("INFO", "getFiltered expenses is called")
        return expansesRepository.getFilteredExpanses(authToken)
    }

    // Method to get the category of particular expanse
    suspend fun getAndSetCategoriesForTransactions(expanseId: Int): Triple<String, Int, String> {
        val categoryResponse =
            ExpanseCategoriesRepository.getCategoryForExpanse(authToken, expanseId)
        return Triple(
            categoryResponse.expanseCategoryName,
            categoryResponse.id,
            categoryResponse.icon
        )
    }

    suspend fun getAndSetWalletForTransactions(
        authToken: String,
        expanseId: Int
    ): Pair<String, Int> {
        val walletResponse = WalletRepository.getWalletForExpanse(authToken, expanseId)
        return Pair(walletResponse.walletName, walletResponse.id)
    }

    fun getTransaction(transactionId: Int): SecondAllExpensesItem {
        for (tx in transactionState.value) {
            if (tx.id === transactionId) {
                return tx
            }
        }
        throw Exception("No transaction found!")
    }

    fun deleteExpense(expanse: SecondAllExpensesItem) {
        //transactionState.value.remove(expanse)
        transactionState.value = transactionState.value.toMutableList().also { it.remove(expanse) }
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while deleting the transaction : $exception")
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            //authTokenJob.join()
            Log.d("INFO", "Auth token for delete is $authToken")
            TransactionsRepository.deleteTransaction(expanse.id, authToken)
        }
    }


}
package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.Expanse
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
    var totalExpenses = 0
    var totalIncome = 0
    var sortedBy = mutableStateOf("No sort")
    var budgetSet = mutableStateOf(0)
    var budgetLeft = mutableStateOf(0)

    var showBudgetSetDialog = mutableStateOf(false)

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while fetching expanses to the initial screen")
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
                }
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            while (authToken.equals("")) {
                Log.d("INFO", "Access token is not set up yet")
            }
            var expanses = getFilteredExpenses()
            for (transaction in expanses) {
                val transactionCategoryNameAndIdAndIcon =
                    getAndSetCategoriesForTransactions(transaction.id)
                transaction.categoryName = transactionCategoryNameAndIdAndIcon.first
                transaction.categoryId = transactionCategoryNameAndIdAndIcon.second
                transaction.categoryIcon = transactionCategoryNameAndIdAndIcon.third
                val transactionWalletNameAndId = getAndSetWalletForTransactions(transaction.id)
                transaction.walletName = transactionWalletNameAndId.first
                transaction.walletId = transactionWalletNameAndId.second
            }
            transactionState.value = expanses
            var totalExpensesTemp = 0
            var totalIncomeTemp = 0
            for (expense in expanses) {
                if (expense.type.equals("Expense")) {
                    totalExpensesTemp += expense.amount
                    expanseState.value += expense
                }
                if (expense.type.equals("Income")) {
                    totalIncomeTemp += expense.amount
                    incomeState.value += expense
                }
            }
            totalExpenses = totalExpensesTemp
            totalIncome = totalIncomeTemp
            budgetLeft.value = budgetSet.value + totalIncome - totalExpenses
        }
        dataLoaded.value = true
    }

    fun showBudgetSetAlertDialog() {
        showBudgetSetDialog.value = true
    }

    fun updateSortBy(value: String) {
        sortedBy.value = value
    }

    fun updateBudgetSet(value: Int) {
        budgetSet.value = value
    }

    fun updateBudgetLeft() {
        budgetLeft.value = budgetSet.value + totalIncome - totalExpenses
    }

    //Method to get expenses from the JpaRepository default API, there is no filtering by username avalable
    suspend fun getExpanses(): List<Expanse> {
        Log.d("INFO", "getExpanses is called")
        return expansesRepository.getExpanses(authToken)._embedded.expanses
    }

    //Method to get expenses filtered by the token used
    suspend fun getFilteredExpenses(): SecondAllExpensesResponse {
        Log.d("INFO", "getFiltered expenses is called")
        return expansesRepository.getFilteredExpanses(authToken)
    }

    // Method to get the category of particular expanse
    suspend fun getAndSetCategoriesForTransactions(expanseId: Int): Triple<String, Int, String> {
        val categoryResponse = ExpanseCategoriesRepository.getCategoryForExpanse(expanseId)
        return Triple(
            categoryResponse.expanseCategoryName,
            categoryResponse.id,
            categoryResponse.icon
        )
    }

    suspend fun getAndSetWalletForTransactions(expanseId: Int): Pair<String, Int> {
        val walletResponse = WalletRepository.getWalletForExpanse(expanseId)
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

    fun deleteExpense(expanse: SecondAllExpensesItem) {
        //transactionState.value.remove(expanse)
        transactionState.value = transactionState.value.toMutableList().also { it.remove(expanse) }
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while deleting the transaction : $exception")
        }
        viewModelScope.launch(handler + Dispatchers.IO) {
            //authTokenJob.join()
            Thread.sleep(500)
            Log.d("INFO", "Auth token for delete is $authToken")
            TransactionsRepository.deleteTransaction(expanse.id, authToken)
        }
    }


}
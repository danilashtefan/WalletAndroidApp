package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.TransactionWrapperWithColor
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.TransactionsRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class WalletStatisticsViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository):
    ViewModel() {
    private var walletId: Int = 0
    var dataLoaded = mutableStateOf(false)

    //Transactions of the particular wallet
    val transactionState = mutableStateOf((emptyList<TransactionWrapperWithColor>()))
    var expanseState = mutableStateOf((emptyList<TransactionWrapperWithColor>()))
    var incomeState = mutableStateOf((emptyList<TransactionWrapperWithColor>()))
    var typeOfTransactionsToDisplay = mutableStateOf("expense")
    var walletName = mutableStateOf("")

    //Total amount to show on the screen
    var totalAmount = mutableStateOf(0)
    var expenseAmount = mutableStateOf(0)
    var incomeAmount = mutableStateOf(0)

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
            var wrappedTransactions = arrayListOf<TransactionWrapperWithColor>()
            val rnd = Random()
            for (transaction in transactions){
                val transactionCategoryNameAndIdAndIcon = getAndSetCategoriesForTransactions(transaction.id)
                transaction.categoryName = transactionCategoryNameAndIdAndIcon.first
                transaction.categoryId = transactionCategoryNameAndIdAndIcon.second
                transaction.categoryIcon = transactionCategoryNameAndIdAndIcon.third
                val transactionWalletNameAndId = getAndSetWalletForTransactions(transaction.id)
                transaction.walletName = transactionWalletNameAndId.first
                walletName.value = transactionWalletNameAndId.first
                transaction.walletId = transactionWalletNameAndId.second
                wrappedTransactions.add(TransactionWrapperWithColor(
                    transaction = transaction, Color(
                        (0..255).random(),
                        (0..255).random(),
                        (0..255).random())
                ))
            }
            transactionState.value = wrappedTransactions

            var expenseAmountTemp = 0
            var incomeAmountTemp = 0
            for (transaction in wrappedTransactions){


                if(transaction.transaction.type.equals("Expense")){
                    expenseAmountTemp += transaction.transaction.amount
                    expanseState.value += transaction
                }else{
                    incomeAmountTemp += transaction.transaction.amount
                    incomeState.value += transaction
                }
            }
            expenseAmount.value = expenseAmountTemp
            incomeAmount.value = incomeAmountTemp
            totalAmount.value = incomeAmountTemp - expenseAmountTemp
        }
        dataLoaded.value = true
    }

    fun setWalletId(id:Int){
        walletId = id
    }

    suspend fun getFilteredExpenses(): SecondAllExpensesResponse {
        Log.d("INFO","getFiltered expenses is called")
        return TransactionsRepository.getWalletFilteredExpenses(authToken = authToken, walletId = walletId)

    }

    suspend fun getAndSetCategoriesForTransactions(expanseId: Int): Triple<String, Int, String> {
        val categoryResponse = ExpanseCategoriesRepository.getCategoryForExpanse(expanseId)
        return Triple(categoryResponse.expanseCategoryName, categoryResponse.id, categoryResponse.icon)
    }

    suspend fun getAndSetWalletForTransactions(expanseId: Int): Pair<String, Int> {
        val walletResponse = WalletRepository.getWalletForExpanse(expanseId)
        return Pair(walletResponse.walletName, walletResponse.id)
    }
}
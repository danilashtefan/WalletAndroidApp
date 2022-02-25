package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ExpanseCategoriesViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModel() {
    private val categoriesRepository: ExpanseCategoriesRepository = ExpanseCategoriesRepository()
    val expanseCategoriesState =
        mutableStateOf((emptyList<SecondAllExpenseCategoriesResponseItem>()))
    var whatToSeeState = mutableStateOf("")
    var dataLoaded = mutableStateOf(false)
    var transactionCetegoriesState = mutableStateOf((emptyList<SecondAllExpenseCategoriesResponseItem>()))
    var transactionWalletsState = mutableStateOf(emptyList<SecondAllWalletsResponseItem>())
    var userName = ""
    var authToken = ""

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Network exception in Expense Categories screen: $exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getAccessToken.catch {
                Log.d(
                    "ERROR",
                    "EXPECTION while getting the token in the Expense Categories screen"
                )
            }
                .collect {
                    Log.d("TOKEN", "Access token on add screen: $it")
                    authToken = it
                }
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getUsername.catch {
                Log.d(
                    "ERROR",
                    "Could not get Username from Data Store on Expense Categories screen"
                )
            }
                .collect {
                    Log.d("TOKEN", "Username on Add Screen: $it")
                    userName = it
                }
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            val transactionCategories = getFilteredTransactionCategories()
            //val transactionWallets = getFilteredWallets()
            transactionCetegoriesState.value = transactionCategories
            //transactionWalletsState.value = transactionWallets
            dataLoaded.value = true;
        }
    }

    suspend fun getFilteredTransactionCategories(): SecondAllExpenseCategoriesResponse {
        var listOfCategories = categoriesRepository.getFilteredExpenseCategories(authToken)
        return listOfCategories
    }

}
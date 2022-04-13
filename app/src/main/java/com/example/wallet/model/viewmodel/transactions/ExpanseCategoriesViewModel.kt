package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ExpanseCategoriesViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModel() {
    val expanseCategoriesState =
        mutableStateOf((emptyList<SecondAllExpenseCategoriesResponseItem>()))
    var whatToSeeState = mutableStateOf("")
    var dataLoaded = mutableStateOf(false)
    var transactionCetegoriesState =
        mutableStateOf((emptyList<SecondAllExpenseCategoriesResponseItem>()))
    var transactionWalletsState = mutableStateOf(emptyList<SecondAllWalletsResponseItem>())
    var authToken = ""
    var categoryToDelete = mutableStateOf(SecondAllExpenseCategoriesResponseItem())
    var walletToDelete = mutableStateOf(SecondAllWalletsResponseItem())

    var showCategoryAlertDialog = mutableStateOf(false)
    var showWalletAlertDialog = mutableStateOf(false)
    var dialogText =
        mutableStateOf("By deleting this element you delete all the transactions associated with it")

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
                    val transactionCategories = getFilteredTransactionCategories()
                    val transactionWallets = getFilteredWallets()
                    transactionCetegoriesState.value = transactionCategories
                    transactionWalletsState.value = transactionWallets
                    dataLoaded.value = true
                }
        }

    }

    suspend fun getFilteredTransactionCategories(): SecondAllExpenseCategoriesResponse {
        var listOfCategories = ExpanseCategoriesRepository.getFilteredExpenseCategories(authToken)
        return listOfCategories
    }

    suspend fun getFilteredWallets(): SecondAllWalletsResponse {
        var listOfWallets = WalletRepository.getFilteredWallets(authToken)
        return listOfWallets
    }

    fun deleteCategoryDialogShow() {
        showCategoryAlertDialog.value = true
    }

    fun deleteCategory(category: SecondAllExpenseCategoriesResponseItem) {
        transactionCetegoriesState.value =
            transactionCetegoriesState.value.toMutableList().also { it.remove(category) }

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while deleting the category : $exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            //authTokenJob.join()
            Thread.sleep(500)
            Log.d("INFO", "Auth token for delete is $authToken")
            ExpanseCategoriesRepository.deleteCategory(category.id, authToken)
        }
    }

    fun deleteWalletDialogShow() {
        showWalletAlertDialog.value = true
    }

    fun deleteWallet(wallet: SecondAllWalletsResponseItem) {
        transactionWalletsState.value =
            transactionWalletsState.value.toMutableList().also { it.remove(wallet) }

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while deleting the wallet : $exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            //authTokenJob.join()
            Thread.sleep(500)
            Log.d("INFO", "Auth token for delete is $authToken")
            WalletRepository.deleteWallet(wallet.id, authToken)
        }
    }

}
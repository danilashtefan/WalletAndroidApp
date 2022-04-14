package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.repository.WalletRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import com.example.wallet.requests.AddOrEditCategoryRequest
import com.example.wallet.requests.AddOrEditWalletRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class WalletsDetailsViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModel() {

    private var walletId: Int = 0
    var dataLoaded = mutableStateOf(false)
    var wallet = mutableStateOf(SecondAllWalletsResponseItem())
    var nameFieldTemporaryValueBeforeSavingtoDB: String? = null
    var iconFieldTemporaryValueBeforeSavingtoDB: String? = null
    var typeFieldTemporaryValueBeforeSavingtoDB: String? = null
    var username: String = ""
    var authToken = ""

    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d(
                "EXCEPTION",
                "Thread exception while getting username on the wallet details screen"
            )
        }

        val authTokenJob = viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getAccessToken.catch {
                Log.d(
                    "ERROR",
                    "EXPECTION while getting the token in the wallet details screen"
                )
            }
                .collect {
                    authToken = it
                    Log.d("TOKEN", "Access token on wallet details screen: $authToken")
                }
        }

        val usernameJob: Job = viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getUsername.catch {
                Log.d(
                    "ERROR",
                    "Could not get Username from Data Store on wallet details screen "
                )
            }
                .collect {
                    Log.d("TOKEN", "Username on wallet details screen: $it")
                    username = it
                }
        }


    }

    fun setWalletId(walletId: Int) {
        if (walletId === this.walletId) {
            return
        }
        this.walletId = walletId
        val wallet = WalletRepository.getWallet(this.walletId)
        this.wallet.value = wallet

        this.nameFieldTemporaryValueBeforeSavingtoDB = wallet.walletName
        this.typeFieldTemporaryValueBeforeSavingtoDB = wallet.currency
        this.iconFieldTemporaryValueBeforeSavingtoDB = wallet.icon

        dataLoaded.value = true

    }

    fun updateTemporaryFieldValueBeforeSavingToDB(field: String, value: String) {
        when (field) {
            "name" -> nameFieldTemporaryValueBeforeSavingtoDB = value
            "icon" -> iconFieldTemporaryValueBeforeSavingtoDB = value
            "type" -> typeFieldTemporaryValueBeforeSavingtoDB = value
        }
    }


    fun editWalletInDb() {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when adding category to Db: $exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            editWalletInDb(
                id = walletId,
                walletData = AddOrEditWalletRequest(
                    walletName = nameFieldTemporaryValueBeforeSavingtoDB,
                    currency = typeFieldTemporaryValueBeforeSavingtoDB,
                    icon = iconFieldTemporaryValueBeforeSavingtoDB,
                    username = username

                )
            )
        }

    }

    suspend fun editWalletInDb(id: Int, walletData:AddOrEditWalletRequest){
        WalletRepository.editWalletInDb(id, walletData, authToken)
    }

}
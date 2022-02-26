package com.example.wallet.model.viewmodel.transactions

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.ExpanseCategoriesRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.requests.AddOrEditCategoryRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TransactionCategoriesDetailsViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModel() {
    private var categoryId: Int = 0;
    var dataLoaded = mutableStateOf(false)
    var category = mutableStateOf(SecondAllExpenseCategoriesResponseItem())
    var nameFieldTemporaryValueBeforeSavingtoDB: String? = null
    var iconFieldTemporaryValueBeforeSavingtoDB: String? = null
    var typeFieldTemporaryValueBeforeSavingtoDB: String? = null
    var username: String = ""
    var authToken = ""


    init {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d(
                "EXCEPTION",
                "Thread exception while getting username on the categories transaction details screen"
            )
        }

        val authTokenJob = viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getAccessToken.catch {
                Log.d(
                    "ERROR",
                    "EXPECTION while getting the token in the add screen"
                )
            }
                .collect {
                    authToken = it
                    Log.d("TOKEN", "Access token on transaction details screen: $authToken")
                }
        }

        val usernameJob: Job = viewModelScope.launch(handler + Dispatchers.IO) {
            dataStorePreferenceRepository.getUsername.catch {
                Log.d(
                    "ERROR",
                    "Could not get Username from Data Store on Transaction details screen screen"
                )
            }
                .collect {
                    Log.d("TOKEN", "Username on Transaction details Screen: $it")
                    username = it
                }
        }


    }

    fun setCategoryId(categoryId: Int) {
        if (categoryId === this.categoryId) {
            return;
        }
        this.categoryId = categoryId
        val category = ExpanseCategoriesRepository.getCategory(this.categoryId)
        this.category.value = category


        this.nameFieldTemporaryValueBeforeSavingtoDB = category.expanseCategoryName
        this.typeFieldTemporaryValueBeforeSavingtoDB = category.type
        this.iconFieldTemporaryValueBeforeSavingtoDB = category.icon

            dataLoaded.value = true;

    }

    fun updateTemporaryFieldValueBeforeSavingToDB(field: String, value: String) {
        when (field) {
            "name" -> nameFieldTemporaryValueBeforeSavingtoDB = value
            "icon" -> iconFieldTemporaryValueBeforeSavingtoDB = value
            "type" -> typeFieldTemporaryValueBeforeSavingtoDB = value
        }

    }

    fun editCategoryInDb(){

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception when adding category to Db")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            editCategoryInDb( id = categoryId,
                categoryData = AddOrEditCategoryRequest(
                    expanseCategoryName = nameFieldTemporaryValueBeforeSavingtoDB,
                    type = typeFieldTemporaryValueBeforeSavingtoDB,
                    icon = iconFieldTemporaryValueBeforeSavingtoDB,
                    username = username

                )
            )
        }


    }

    suspend fun editCategoryInDb(id: Int, categoryData:AddOrEditCategoryRequest){
        ExpanseCategoriesRepository.editCategoryInDb(id, categoryData, authToken)
    }




}
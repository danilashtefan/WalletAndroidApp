package com.example.wallet.model.viewmodel.transactions

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem

class ReportViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository):
    ViewModel(){
    var dataLoaded = mutableStateOf(true)
    var minDatePicked = mutableStateOf("1000-01-01")
    var maxDatePicked = mutableStateOf("3000-12-12")
    private val _accessToken = MutableLiveData("")
    var authToken = ""

    var expandedCalendarMin = mutableStateOf(false)
    var expandedCalendarMax = mutableStateOf(false)
    var topExpense = mutableStateOf(SecondAllExpensesItem())
    var topIncome = mutableStateOf(SecondAllExpensesItem())

    var topExpenseCategory = mutableStateOf(SecondAllExpenseCategoriesResponseItem())
    var topIncomeCategory = mutableStateOf(SecondAllExpenseCategoriesResponseItem())

    var topExpenseWallet = mutableStateOf(SecondAllWalletsResponseItem())
    var topIncomeWallet = mutableStateOf(SecondAllWalletsResponseItem())


}
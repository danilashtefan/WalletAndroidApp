package com.example.wallet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.viewmodel.transactions.WalletStatisticsViewModel
import com.example.wallet.model.viewmodel.transactions.WalletStatisticsViewModelFactory
import java.util.*

private const val DividerLengthInDegrees = 1.8f

@Composable
fun WalletStatisticsScreen(
    navController: NavHostController,
    walletId: Int,
    walletName: String,
    walletIcon: String,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {
    val viewModel: WalletStatisticsViewModel = viewModel(
        factory = WalletStatisticsViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    )
    viewModel.setWalletId(walletId)
    val expenseAmount = viewModel.expenseAmount.value
    val incomeAmount = viewModel.incomeAmount.value
    var expenseItems = viewModel.expanseState.value
    var incomeItems = viewModel.incomeState.value
    var dataLoaded = viewModel.dataLoaded.value
    val listOfButtons = listOf<String>("Expense", "Income")

    while (!dataLoaded) {
        return
    }
    Column()
    {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 15.dp), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "Wallet: $walletIcon $walletName", style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = Color.White
                )
            )
        }
        LazyRow(modifier = Modifier.padding(start = 60.dp)) {
            items(listOfButtons) { element ->
                OutlinedButton(onClick = {
                    viewModel.typeOfTransactionsToDisplay.value =
                        element.lowercase(Locale.getDefault())
                }, Modifier.padding(20.dp)) {
                    Text(text = element)
                }
            }
        }

        var transactionsToShow =
            if (viewModel.typeOfTransactionsToDisplay.value.equals("expense")) expenseItems else incomeItems
        var totalAmount =
            if (viewModel.typeOfTransactionsToDisplay.value.equals("expense")) expenseAmount else incomeAmount
        PieChartBase(
            transactions = transactionsToShow,
            colors = { item -> item.color },
            amounts = { item -> item.transaction.amount.toFloat() },
            totalAmount = totalAmount,
            list = {
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    items(transactionsToShow) { item ->
                        val itemId = item.transaction.id
                        TransactionStatisticsRow(
                            color = item.color,
                            categoryIcon = item.transaction.categoryIcon,
                            categoryName = item.transaction.categoryName,
                            walletName = item.transaction.walletName,
                            date = item.transaction.date,
                            location = item.transaction.location,
                            amount = item.transaction.amount,
                            comments = item.transaction.comments,
                            type = item.transaction.type,
                            editClickAction = { navController.navigate("transactionDetails/$itemId") }) {

                        }
                    }
                }
            })
    }

}
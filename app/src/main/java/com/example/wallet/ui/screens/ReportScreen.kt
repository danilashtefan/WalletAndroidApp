package com.example.wallet.ui.screens

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.CategoryWrapperWithColor
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.*
import com.example.wallet.model.viewmodel.transactions.ExpansesViewModel
import com.example.wallet.model.viewmodel.transactions.ReportViewModel
import com.example.wallet.model.viewmodel.transactions.ReportViewModelFactory

@Composable
fun ReportScreen (navController: NavHostController){

    val viewModel: ReportViewModel = viewModel(
        factory = ReportViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    )

    var dataLoaded = viewModel.dataLoaded.value
    var topExpense = viewModel.topExpense.value
    var topIncome = viewModel.topIncome.value

    var allCategories = viewModel.allCategories.value
    var topExpenseCategory = viewModel.topExpenseCategory.value
    var topIncomeCategory = viewModel.topIncomeCategory.value

    var topExpenseWallet = viewModel.topExpenseWallet.value
    var topIncomeWallet = viewModel.topIncomeWallet.value

    if (!dataLoaded) {
        return;
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        LogoSection(pictureSize = 90)
        Spacer(modifier = Modifier.padding(5.dp))
        PeriodPicking(viewModel = viewModel)
        Spacer(modifier = Modifier.padding(15.dp))
        TopExpenseTransaction(topExpense)
        TopIncomeTransaction(topIncome)
        Spacer(modifier = Modifier.padding(15.dp))

        TopExpenseCategory(topExpenseCategory)
        //PieChart of categories with respect to expenses
//        StatementBody(listOfButtons = ArrayList<String>(),
//            transactions = allCategories,
//            expenses = ArrayList<CategoryWrapperWithColor>(),
//            incomes = ArrayList<CategoryWrapperWithColor>(),
//            colors ={item ->item.color},
//            amounts = { item -> item.category.expenseAmount.toFloat() },
//            totalAmount = totalAmount,
//
//            row = { item ->
//                val itemId = item.transaction.id
//                StatisticsRow(
//                    color = item.color,
//                    categoryIcon = item.transaction.categoryIcon,
//                    categoryName = item.transaction.categoryName,
//                    walletName = item.transaction.walletName,
//                    date = item.transaction.date,
//                    location = item.transaction.location,
//                    amount = item.transaction.amount,
//                    comments = item.transaction.comments,
//                    type = item.transaction.type,
//                    editClickAction = { navController.navigate("transactionDetails/$itemId") }) {
//
//                }
//            })
        //List of all categories with colors
        TopIncomeCategory(topIncomeCategory)
        //PieChart of categories with respect to Incomes
        //List of all categories with colors
        Spacer(modifier = Modifier.padding(15.dp))
        TopExpenseWallet(topExpenseWallet)
        //PieChart of Wallets with respect to expenses and total expenses indicated
        //List of of Wallets with Colors
        TopIncomeWallet(topIncomeWallet)
        //PieChart of Wallets with respect to incomes and total incomes indicated
        //List of of Wallets with Colors
    }

}



@Composable
fun TopIncomeWallet(topIncomeWallet: TopWalletWithAmountResponse) {
    if(topIncomeWallet.wallet != null){
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)){
            Text("Top income wallet: ${topIncomeWallet.wallet.walletName}${topIncomeWallet.wallet.icon} + ${topIncomeWallet.amount} $",style = MaterialTheme.typography.h6, color = Color.White)
        }
    }else{
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)){

            Text(
                "User has no wallets to determine top income wallet",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    }
}

@Composable
fun TopExpenseWallet(topExpenseWallet: TopWalletWithAmountResponse) {
    if(topExpenseWallet.wallet != null){

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text("Top expense wallet: ${topExpenseWallet.wallet.walletName}${topExpenseWallet.wallet.icon} - ${topExpenseWallet.amount} $",style = MaterialTheme.typography.h6, color = Color.White)
        }
    }else{
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)){
            Text(
                "User has no wallets to determine top expense wallet",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    }

}

@Composable
fun TopIncomeCategory(topIncomeCategory: TopExpenseCategoryWithAmountResponse) {
    if(topIncomeCategory.category != null){
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)){
            Text("Top income category: ${topIncomeCategory.category.expanseCategoryName}${topIncomeCategory.category.icon} + ${topIncomeCategory.incomeAmount} $",style = MaterialTheme.typography.h6, color = Color.White)
        }
    }else{
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)){
            Text(
                "User has no categories to determine top income category",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    }
}

@Composable
fun TopExpenseCategory(topExpenseCategory: TopExpenseCategoryWithAmountResponse) {
    if (topExpenseCategory.category != null) {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(
                "Top expense category: ${topExpenseCategory.category.expanseCategoryName}${topExpenseCategory.category.icon} - ${topExpenseCategory.expenseAmount} $",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    }else{
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(
                "User has no categories to determine top expense category",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    }

}

@Composable
fun TopIncomeTransaction(income: SecondAllExpensesItem) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)){
        Text("Top income transaction: ${income.name} - ${income.amount} $",style = MaterialTheme.typography.h6, color = Color.White)
    }
}

@Composable
fun TopExpenseTransaction(expense: SecondAllExpensesItem) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)){
        Text("Top expense transaction: ${expense.name} - ${expense.amount} $",style = MaterialTheme.typography.h6, color = Color.White)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PeriodPicking(viewModel: ReportViewModel){
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
        Text(text = "Period: ", style = MaterialTheme.typography.h6, color = Color.White)
        OutlinedButton(onClick = {
            viewModel.expandedCalendarMin.value = !viewModel.expandedCalendarMin.value
        }, enabled = !viewModel.expandedCalendarMax.value) {
            Text(text = viewModel.minDatePicked.value.toString())
        }
        OutlinedButton(onClick = {
            viewModel.expandedCalendarMax.value = !viewModel.expandedCalendarMax.value
        }, enabled = !viewModel.expandedCalendarMin.value) {
            Text(text = viewModel.maxDatePicked.value.toString())
        }
    }
    AnimatedVisibility(visible = viewModel.expandedCalendarMin.value) {
        ReportCalendarDatePicker(viewModel, "minimum")
    }
    AnimatedVisibility(visible = viewModel.expandedCalendarMax.value) {
        ReportCalendarDatePicker(viewModel, "maximum")
    }
}

@Composable
private fun ReportCalendarDatePicker(
    viewModel: ReportViewModel,
    dateType: String
) {
    AndroidView(
        { CalendarView(it) },
        modifier = Modifier.wrapContentWidth(),
        update = { views ->
            views.setOnDateChangeListener { calendarView, year, month, day ->
                if (dateType == "minimum") {
                    viewModel.minDatePicked.value =
                        year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                } else {
                    viewModel.maxDatePicked.value =
                        year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                }
            }
        }
    )
}
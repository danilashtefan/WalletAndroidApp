package com.example.wallet.ui.screens

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.wallet.model.WalletWrapperWithColor
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.*
import com.example.wallet.model.viewmodel.transactions.ExpansesViewModel
import com.example.wallet.model.viewmodel.transactions.ReportViewModel
import com.example.wallet.model.viewmodel.transactions.ReportViewModelFactory

@Composable
fun ReportScreen(navController: NavHostController) {

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

    var allWallets = viewModel.allWallets.value
    var topExpenseWallet = viewModel.topExpenseWallet.value
    var topIncomeWallet = viewModel.topIncomeWallet.value

    var totalCategoriesExpenses = viewModel.totalCategoriesExpenses.value
    var totalCategoriesIncomes = viewModel.totalCategoriesIncomes.value

    var totalWalletsExpenses = viewModel.totalWalletsExpenses.value
    var totalWalletsIncomes = viewModel.totalWalletsIncomes.value

    if (!dataLoaded) {
        return;
    }

    LazyColumn(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        item {
            LogoSection(pictureSize = 90)
            Spacer(modifier = Modifier.padding(5.dp))
            PeriodPicking(viewModel = viewModel)
            Spacer(modifier = Modifier.padding(15.dp))
            TopExpenseTransaction(topExpense)
            TopIncomeTransaction(topIncome)
            Spacer(modifier = Modifier.padding(15.dp))
            TopExpenseCategory(topExpenseCategory)
        }
        item { //PieChart of categories with respect to expenses
            StatementBody(listOfButtons = ArrayList<String>(),
                transactions = allCategories,
                expenses = ArrayList<CategoryWrapperWithColor>(),
                incomes = ArrayList<CategoryWrapperWithColor>(),
                colors = { item -> item.color },
                amounts = { item -> item.category.expenseAmount.toFloat() },
                totalAmount = totalCategoriesExpenses,
                list = {
                    LazyRow(modifier = Modifier.padding(12.dp)) {
                        items(allCategories) { item ->
                            val itemId = item.category.category.id
                            CategoryAndWalletStatisticsRow(
                                color = item.color,
                                icon = item.category.category.icon,
                                amount = item.category.expenseAmount,
                                name = item.category.category.expanseCategoryName,
                                type = item.category.category.type,
                                id = itemId,
                                route = "categoryStatistics/$itemId",
                                editClickAction = { navController.navigate("categoriesDetails/$itemId") },
                                deleteClickAction = { },
                                navController = navController
                            )
                        }
                    }
                })
        }
        //List of all categories with colors
        item {
            TopIncomeCategory(topIncomeCategory)
        }
        item {
            StatementBody(listOfButtons = ArrayList<String>(),
                transactions = allCategories,
                expenses = ArrayList<CategoryWrapperWithColor>(),
                incomes = ArrayList<CategoryWrapperWithColor>(),
                colors = { item -> item.color },
                amounts = { item -> item.category.incomeAmount.toFloat() },
                totalAmount = totalCategoriesIncomes,
                list = {
                    LazyRow(modifier = Modifier.padding(12.dp)) {
                        items(allCategories) { item ->
                            val itemId = item.category.category.id
                            CategoryAndWalletStatisticsRow(
                                color = item.color,
                                icon = item.category.category.icon,
                                amount = item.category.incomeAmount,
                                name = item.category.category.expanseCategoryName,
                                type = item.category.category.type,
                                id = itemId,
                                route = "categoryStatistics/$itemId",
                                editClickAction = { navController.navigate("categoriesDetails/$itemId") },
                                deleteClickAction = { },
                                navController = navController
                            )
                        }
                    }
                })
        }
        item {
            Spacer(modifier = Modifier.padding(15.dp))
            TopExpenseWallet(topExpenseWallet)
        }
        item {
            StatementBody(listOfButtons = ArrayList<String>(),
                transactions = allWallets,
                expenses = ArrayList<WalletWrapperWithColor>(),
                incomes = ArrayList<WalletWrapperWithColor>(),
                colors = { item -> item.color},
                amounts = { item -> item.wallet.expenseAmount.toFloat() },
                totalAmount = totalWalletsExpenses,
                list = {
                    LazyRow(modifier = Modifier.padding(12.dp)) {
                        items(allWallets) { item ->
                            val itemId = item.wallet.wallet.id
                            CategoryAndWalletStatisticsRow(
                                color = item.color,
                                icon = item.wallet.wallet.icon,
                                amount = item.wallet.expenseAmount,
                                name = item.wallet.wallet.walletName,
                                type = item.wallet.wallet.currency,
                                id = itemId,
                                route = "walletStatistics/$itemId",
                                editClickAction = { navController.navigate("walletsDetails/$itemId") },
                                deleteClickAction = { },
                                navController = navController
                            )
                        }
                    }
                })
        }
        item {
            TopIncomeWallet(topIncomeWallet)
        }
        item{
            StatementBody(listOfButtons = ArrayList<String>(),
                transactions = allWallets,
                expenses = ArrayList<WalletWrapperWithColor>(),
                incomes = ArrayList<WalletWrapperWithColor>(),
                colors = { item -> item.color},
                amounts = { item -> item.wallet.incomeAmount.toFloat() },
                totalAmount = totalWalletsIncomes,
                list = {
                    LazyRow(modifier = Modifier.padding(12.dp)) {
                        items(allWallets) { item ->
                            val itemId = item.wallet.wallet.id
                            CategoryAndWalletStatisticsRow(
                                color = item.color,
                                icon = item.wallet.wallet.icon,
                                amount = item.wallet.incomeAmount,
                                name = item.wallet.wallet.walletName,
                                type = item.wallet.wallet.currency,
                                id = itemId,
                                route = "walletStatistics/$itemId",
                                editClickAction = { navController.navigate("walletsDetails/$itemId") },
                                deleteClickAction = { },
                                navController = navController
                            )
                        }
                    }
                })
        }

    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CategoryAndWalletStatisticsRow(
    color: Color,
    amount: Int,
    icon: String,
    name: String,
    type: String,
    id: Int,
    route: String,
    editClickAction: () -> Unit,
    deleteClickAction: () -> Unit,
    navController: NavHostController
) {
    Row() {
        Spacer(
            Modifier
                .size(10.dp, 20.dp)
                .background(color = color)
        )
        ReusableCategoryAndWalletRow(
            icon = icon,
            name = name,
            amount = amount,
            displayAmount = true,
            type = type,
            id = id,
            route = route,
            editClickAction = editClickAction,
            deleteClickAction = deleteClickAction,
            navController = navController
        )
    }
}

@Composable
fun TopIncomeWallet(topIncomeWallet: TopWalletWithAmountResponse) {
    if (topIncomeWallet.wallet != null) {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(
                "Top income wallet: ${topIncomeWallet.wallet.walletName}${topIncomeWallet.wallet.icon} + ${topIncomeWallet.incomeAmount} $",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    } else {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {

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
    if (topExpenseWallet.wallet != null) {

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(
                "Top expense wallet: ${topExpenseWallet.wallet.walletName}${topExpenseWallet.wallet.icon} - ${topExpenseWallet.expenseAmount} $",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    } else {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
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
    if (topIncomeCategory.category != null) {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(
                "Top income category: ${topIncomeCategory.category.expanseCategoryName}${topIncomeCategory.category.icon} + ${topIncomeCategory.incomeAmount} $",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
    } else {
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
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
    } else {
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
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
        Text(
            "Top income transaction: ${income.name} - ${income.amount} $",
            style = MaterialTheme.typography.h6,
            color = Color.White
        )
    }
}

@Composable
fun TopExpenseTransaction(expense: SecondAllExpensesItem) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
        Text(
            "Top expense transaction: ${expense.name} - ${expense.amount} $",
            style = MaterialTheme.typography.h6,
            color = Color.White
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PeriodPicking(viewModel: ReportViewModel) {
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
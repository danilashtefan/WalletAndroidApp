package com.example.wallet.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.viewmodel.transactions.ExpansesViewModel
import com.example.wallet.R
import com.example.wallet.helpers.DateFormatter
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem
import com.example.wallet.model.viewmodel.transactions.ExpensesViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ExpansesScreen(
    navController: NavHostController,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {
    val viewModel: ExpansesViewModel = viewModel(
        factory = ExpensesViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    ) //ViewModel is bound to a composable
    val expanses = viewModel.transactionState.value
    var dataLoaded = viewModel.dataLoaded.value
    val accessToken = viewModel.authToken
    var expenseItems = viewModel.expanseState.value
    var incomeItems = viewModel.incomeState.value
    val totalExpenses = viewModel.totalExpenses
    val totalIncome = viewModel.totalIncome
    val budgetSet = viewModel.budgetSet
    val budgetLeft = viewModel.budgetLeft
    val sortBy = viewModel.sortedBy
    viewModel.updateBudgetLeft()

    if (!dataLoaded) {
        return
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        TransactionListSection(
            viewModel,
            totalExpenses.value,
            totalIncome.value,
            budgetSet.value,
            budgetLeft.value
        )
        ExpanseSection(expanses, navController, viewModel, sortBy.value)
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransactionListSection(
    viewModel: ExpansesViewModel,
    totalExpenses: Int,
    totalIncome: Int,
    budgetSet: Int,
    budgetLeft: Int
) {

    Column {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LogoSection(pictureSize = 90)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Transaction list",
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
        }

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(text = "Period: ", style = MaterialTheme.typography.h6, color = Color.White)
            OutlinedButton(onClick = {
                viewModel.expandedCalendarMin.value = !viewModel.expandedCalendarMin.value
            }, enabled = !viewModel.expandedCalendarMax.value) {
                Text(text = viewModel.minDatePicked.value.toString())
            }
            Spacer(Modifier.width(12.dp))
            OutlinedButton(onClick = {
                viewModel.expandedCalendarMax.value = !viewModel.expandedCalendarMax.value
            }, enabled = !viewModel.expandedCalendarMin.value) {
                Text(text = viewModel.maxDatePicked.value.toString())
            }
        }
        AnimatedVisibility(visible = viewModel.expandedCalendarMin.value) {
            CalendarDatePicker(viewModel, "minimum")
        }
        AnimatedVisibility(visible = viewModel.expandedCalendarMax.value) {
            CalendarDatePicker(viewModel, "maximum")
        }

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(
                text = "Total expenses: ${totalExpenses} HUF",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(
                text = "Total income: ${totalIncome} HUF",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Column(modifier = Modifier.weight(2F)) {
                Text(
                    text = "Budget set: ${budgetSet} HUF",
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
            }
            Column(modifier = Modifier.weight(1F)) {
                var showBudgetSetDialog = viewModel.showBudgetSetDialog.value
                var showLowBudgetAlertDialog = viewModel.showLowBudgetAlertDialog.value
                var userAknowledgedAboutLowBudget = viewModel.userAknowledgedAboutLowBudget.value
                val budgetSetTextState = remember { mutableStateOf(TextFieldValue("")) }
                var budgetForUpdate = "0"
                if (showLowBudgetAlertDialog) {
                    OneButtonAlertDialogComponent(onDismiss = {
                        viewModel.dismissLowBudgetAlertDialog()
                    },
                        bodyText = {
                            Text(
                                viewModel.lowBugetDialogText.value,
                                color = Color.White
                            )
                        },
                        buttonText = "DISMISS"
                    )
                }
                if (showBudgetSetDialog) {
                    OneButtonAlertDialogComponent(onDismiss = {
                        viewModel.showBudgetSetDialog.value = false
                        viewModel.updateBudgetSet(budgetForUpdate.toInt())
                        viewModel.updateBudgetLeft()
                    }, bodyText = {
                        TextField(
                            value = budgetSetTextState.value,
                            onValueChange = {
                                budgetSetTextState.value = it
                                if (budgetSetTextState.value.text.equals("")) {
                                    budgetForUpdate = "0"
                                } else {
                                    budgetForUpdate = budgetSetTextState.value.text
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    }, buttonText = "Confirm")
                }

            }
        }
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(
                text = "Budget left: ${budgetLeft} HUF",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 25.dp)
        ) {
            Text(
                text = "Sort by:",
                style = MaterialTheme.typography.h6,
                color = Color.White
            )
            SpinnerView(viewModel = viewModel)
        }
Row(horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier.padding(start = 25.dp)){
    OutlinedButton(
        onClick = { viewModel.showBudgetSetAlertDialog() },
        modifier = Modifier.wrapContentSize(
            Alignment.Center
        ).height(35.dp)
    ) {
        Text(text = "Set budget")
    }
}
    }
}


@Composable
fun LogoSection(pictureSize: Int) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.wallet_no_background_cropped),
            contentDescription = "",
            modifier = Modifier
                .size(pictureSize.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun CalendarDatePicker(
    viewModel: ExpansesViewModel,
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


@Composable
fun ExpanseSection(
    expanses: List<SecondAllExpensesItem>, navController: NavHostController,
    viewModel: ExpansesViewModel,
    sortBy: String
) {

    var filteredTransactions = expanses.filter {
        SimpleDateFormat("yyyy-MM-dd").parse(DateFormatter.formatDate(it.date)) >= SimpleDateFormat(
            "yyyy-MM-dd"
        ).parse(viewModel.minDatePicked.value) &&
                SimpleDateFormat("yyyy-MM-dd").parse(DateFormatter.formatDate(it.date)) <= SimpleDateFormat(
            "yyyy-MM-dd"
        ).parse(viewModel.maxDatePicked.value)

    }
//    for (transaction in filteredTransactions) {
//        if (transaction.type.equals("Expense")) {
//            transaction.amount *= -1
//        }
//    }
    val dateStrToLocalDate: (String) -> LocalDate = {
        LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
//"Category", "Location", "Time", "Date", "Amount", "Wallet"
    var sortedTransactions = filteredTransactions
    if (!sortedTransactions.isEmpty()) {
        when (sortBy) {
            "Location" -> sortedTransactions =
                filteredTransactions.sortedBy { it.location } as MutableList<SecondAllExpensesItem>
            "Category" -> sortedTransactions =
                filteredTransactions.sortedBy { it.categoryName } as MutableList<SecondAllExpensesItem>
            "Date" -> sortedTransactions =
                filteredTransactions.sortedBy {
                    LocalDate.parse(
                        it.date,
                        DateTimeFormatter.ISO_DATE
                    )
                } as MutableList<SecondAllExpensesItem>
            "Amount" -> {
                sortedTransactions =
                    filteredTransactions.sortedBy { it.amount } as MutableList<SecondAllExpensesItem>
            }
        }
    }
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(sortedTransactions) { expanse ->
            val expanseId = expanse.id
            ReusableRow(
                categoryIcon = expanse.categoryIcon,
                categoryName = expanse.categoryName,
                walletName = expanse.walletName,
                date = expanse.date,
                location = expanse.location,
                amount = expanse.amount,
                comments = expanse.comments,
                type = expanse.type,
                editClickAction = {
                    navController.navigate("transactionDetails/$expanseId")
                },
                deleteClickAction = {
                    Log.d("INFO", "Delete button pressed")
                    viewModel.deleteExpense(expanse)
                })
        }
    }
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun SpinnerView(viewModel: ExpansesViewModel) {
    val sampleList = mutableListOf("Category", "Location", "Date", "Amount", "Wallet")
    var sampleName: String by remember { mutableStateOf("No sort") }
    var expanded by remember { mutableStateOf(false) }
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(targetState = transitionState, label = "transition")
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = 300)
    }, label = "rotationDegree") {
        if (expanded) 180f else 0f
    }
    val context = LocalContext.current

    Column {

        Spacer(modifier = Modifier.height(5.dp))
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clickable {
                            expanded = !expanded
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sampleName,
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Spinner",
                        modifier = Modifier.rotate(arrowRotationDegree)
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        sampleList.forEach { data ->
                            DropdownMenuItem(
                                onClick = {
                                    expanded = false
                                    sampleName = data
                                    viewModel.updateSortBy(data)
                                }
                            ) {
                                Text(
                                    text = data,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReusableRow(
    categoryIcon: String,
    categoryName: String,
    walletName: String,
    date: String,
    location: String?,
    amount: Int,
    comments: String?,
    type: String,
    editClickAction: () -> Unit,
    deleteClickAction: () -> Unit
) {
    val currency = " HUF"
    var sign = "+"
    if (type == "Expense" && amount > 0) {
        sign = "-"
    } else if (type == "Expense" && amount <= 0) {
        sign = ""
    }

    Card(
        backgroundColor = Color.White,
        modifier = Modifier.padding(6.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column(Modifier.clickable { expanded = !expanded }) {
            Row(
                modifier = Modifier
                    .height(68.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(0.5f)) {
                    CategoryImage(categoryIcon, 30)
                    Spacer(Modifier.width(15.dp))
                    Text(text = categoryName,maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                //Spacer(Modifier.weight(0.4f))
               Column {
                    Text("Press for details")
                }

                //Spacer(Modifier.weight(0.3f))
                Column(Modifier.weight(1f).wrapContentSize(Alignment.CenterEnd)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = sign,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 3.dp)
                        )

                        Text(
                            text = amount.toString(),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Text(
                            text = currency,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

               // Spacer(Modifier.width(16.dp))
//                Column(Modifier.weight(0.1f)) {
//                    IconButton({ expanded = !expanded }) {
//                        Icon(
//                            imageVector = Icons.Filled.ChevronRight,
//                            contentDescription = null,
//                            modifier = Modifier
//                                .padding(end = 12.dp)
//                                .size(24.dp),
//                        )
//                    }
//                }

            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier=Modifier.padding(start=5.dp)) {
                    Text(text = "Location: " + location)
                    Text(text = "Date: " + date)
                    Text(text = "Comment: " + comments)
                    Text(text = "Type: " + type)
                    Text(text = "Wallet: " + walletName)
                    Row {
                        OutlinedButton(onClick = { editClickAction.invoke() }) {
                            Text(text = "Edit")
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        OutlinedButton(onClick = { deleteClickAction.invoke() }) {
                            Text(text = "Delete", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryImage(categoryIcon: String, size: Int) {
    Emoji(emojiCode = categoryIcon, size = size)
}

@Composable
fun Emoji(emojiCode: String, size: Int) {
    Box {
        Canvas(modifier = Modifier
            .size(40.dp)
            .align(Alignment.Center), onDraw = {
            drawCircle(color = Color.White)
        })
        Text(
            text = emojiCode,
            fontSize = size.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 2.dp)
        )
    }
}





package com.example.wallet.ui.screens

import android.util.Log
import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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

@Composable
fun ExpansesScreen(
    navController: NavHostController,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {
    val viewModel: ExpansesViewModel = viewModel(factory = ExpensesViewModelFactory(DataStorePreferenceRepository(
        LocalContext.current))
    ) //ViewModel is bound to a composable
    val expanses = viewModel.transactionState.value
    var dataLoaded = viewModel.dataLoaded.value
    val accessToken = viewModel.authToken
    val totalExpenses = viewModel.totalExpenses
    val totalIncome = viewModel.totalIncome

    if (!dataLoaded) {
        return;
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        TransactionListSection(viewModel, totalExpenses, totalIncome)
        ExpanseSection(expanses ,navController,viewModel)
    }

}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransactionListSection(
    viewModel: ExpansesViewModel,
    totalExpenses:Int,
    totalIncome:Int
){

    Column() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LogoSection(pictureSize = 120)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Transaction list",
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
        }

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(text = "Period: ",style =MaterialTheme.typography.h6, color = Color.White )
            OutlinedButton(onClick = {viewModel.expandedCalendarMin.value = !viewModel.expandedCalendarMin.value}, enabled = !viewModel.expandedCalendarMax.value) {
                Text(text = viewModel.minDatePicked.value.toString())
            }
            OutlinedButton(onClick = {viewModel.expandedCalendarMax.value = !viewModel.expandedCalendarMax.value},enabled = !viewModel.expandedCalendarMin.value) {
                Text(text = viewModel.maxDatePicked.value.toString())
            }
        }
        AnimatedVisibility(visible = viewModel.expandedCalendarMin.value) {
            CalendarDatePicker(viewModel, "minimum")
        }
        AnimatedVisibility(visible = viewModel.expandedCalendarMax.value) {
            CalendarDatePicker(viewModel,"maximum")
        }

        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(text = "Total expenses: ${totalExpenses}",style =MaterialTheme.typography.h6, color = Color.White )
        }
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(text = "Total income: ${totalIncome}",style =MaterialTheme.typography.h6, color = Color.White )
        }
    }
}

@Composable
fun LogoSection(pictureSize: Int) {
    Spacer(modifier = Modifier.padding(5.dp))
    Row() {
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
private fun CalendarDatePicker(viewModel: ExpansesViewModel,
                               dateType:String) {
    AndroidView(
        { CalendarView(it) },
        modifier = Modifier.wrapContentWidth(),
        update = { views ->
            views.setOnDateChangeListener { calendarView, year, month, day ->
                if(dateType == "minimum") {
                    viewModel.minDatePicked.value = year.toString() + "-" + (month+1).toString() + "-" + day.toString()
                }
                else{
                    viewModel.maxDatePicked.value = year.toString() + "-" + (month+1).toString() + "-" + day.toString()
                }
            }
        }
    )
}


@Composable
fun ExpanseSection(
    expanses: List<SecondAllExpensesItem>, navController: NavHostController,
    viewModel: ExpansesViewModel){

    var filteredTransactions = expanses.filter {SimpleDateFormat("yyyy-MM-dd").parse(DateFormatter.formatDate(it.date)) >= SimpleDateFormat("yyyy-MM-dd").parse(viewModel.minDatePicked.value) &&
            SimpleDateFormat("yyyy-MM-dd").parse(DateFormatter.formatDate(it.date)) <= SimpleDateFormat("yyyy-MM-dd").parse(viewModel.maxDatePicked.value)
    }
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(filteredTransactions) { expanse ->
            val expanseId = expanse.id
            ReusableRow(categoryIcon = expanse.categoryIcon,categoryName = expanse.categoryName, date = expanse.date, location = "Location", amount = expanse.amount, comments = expanse.comments as String, type = expanse.type, editClickAction = {
                navController.navigate("transactionDetails/$expanseId")
            },deleteClickAction = {
                Log.d("INFO", "Delete button pressed")
                viewModel.deleteExpense(expanse)
            })
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReusableRow(categoryIcon:String, categoryName: String, date: String, location: String?, amount: Int, comments: String?, type: String, editClickAction:() -> Unit, deleteClickAction:() -> Unit) {
    val currency = "$"
    var sign = "+"
    if(type == "Expense") {
        sign = "-"
    }

    Card(
        backgroundColor = Color.White,
        modifier = Modifier.padding(6.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column(Modifier.clickable{expanded= !expanded}) {
            Row(
                modifier = Modifier
                    .height(68.dp),
                verticalAlignment = Alignment.CenterVertically,
                ) {
                Spacer(Modifier.width(12.dp))
                Column(Modifier) {
                    CategoryImage(categoryIcon,30)
                    Spacer(Modifier.width(15.dp))
                    Text(text = categoryName)
                }
                Spacer(Modifier.weight(0.4f))
                Column(Modifier) {
                    Text("Wallet")
                    Text("Date")
                    Text("Location")
                }
                Spacer(Modifier.weight(0.3f))
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
                Spacer(Modifier.width(16.dp))
                IconButton({ println("Pressed") }) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(24.dp),
                    )
                }

            }
            AnimatedVisibility(visible = expanded) {
                Column() {
                    Text(text = "Location: " +location)
                    Text(text= "Date: " + date)
                    Text(text = "Comment: " + comments)
                    Text(text="Type: " +type )
                    Row(){
                        OutlinedButton(onClick = {editClickAction.invoke()}) {
                            Text(text = "Edit")
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        OutlinedButton(onClick = {deleteClickAction.invoke()}) {
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
    Box() {
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





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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.viewmodel.transactions.ExpansesViewModel
import com.example.wallet.R
import com.example.wallet.model.Expanse

@Composable
fun ExpansesScreen(navController: NavHostController) {
    val viewModel: ExpansesViewModel = viewModel() //ViewModel is bound to a composable
    val expanses = viewModel.transactionState.value
    var dataLoaded = viewModel.dataLoaded.value

    if (!dataLoaded) {
        return;
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        TransactionListSection(viewModel)
        ExpanseSection(expanses ,navController,viewModel)
    }

}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransactionListSection(
    viewModel: ExpansesViewModel
){

    Column() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.padding(5.dp))
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.wallet_no_background_cropped),
                    contentDescription = "",
                    modifier = Modifier
                        .size(120.dp)
                        .weight(1f)
                )
            }
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
            Text(text = "Total expanses:",style =MaterialTheme.typography.h6, color = Color.White )
        }
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
                    viewModel.minDatePicked.value = day.toString()
                }
                else{
                    viewModel.maxDatePicked.value = day.toString()
                }
            }
        }
    )
}


@Composable
fun ExpanseSection(expanses: List<Expanse>, navController: NavHostController,viewModel: ExpansesViewModel){
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(expanses) { expanse ->
            ReusableRow(categoryName = expanse.categoryName, date = expanse.date, location = "Location", amount = expanse.amount, comments = expanse.comments as String, type = expanse.type){
                val expanseId = expanse.id
                expanse._links?.category?.let { Log.d("Expanse Category Link", it.href) }
                navController.navigate("transactionDetails/$expanseId")
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ReusableRow(categoryName: String, date: String, location: String, amount: Int, comments: String, type: String,clickAction:() -> Unit) {
    val currency = "$"
    var sign = "+"
    if(type == "expanse") {
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
                    CategoryImage()
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
                        modifier = Modifier.align(Alignment.CenterVertically).
                                padding(end = 3.dp)
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
            //Spacer(Modifier.width(30.dp))
            AnimatedVisibility(visible = expanded) {
                Column() {
                    Text(text = "Location: " +location)
                    Text(text= "Date: " + date)
                    Text(text = "Comment: " + comments)
                    Text(text="Type: " +type )
                    OutlinedButton(onClick = {clickAction.invoke()}) {
                        Text(text = "Edit")
                    }
                }
            }
        }
    }
}
@Composable
fun CategoryImage() {
    Image(
        painter = painterResource(id = R.drawable.groceries),
        contentDescription = "",
        modifier = Modifier.size(25.dp),
    )
}

@Preview
@Composable
fun ComposablePreview() {
    ReusableRow("","","",1, "comment", "expanse"){}
}




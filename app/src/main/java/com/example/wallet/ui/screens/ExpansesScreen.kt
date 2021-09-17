package com.example.wallet.ui.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.viewmodel.ExpansesViewModel
import com.example.wallet.R
import com.example.wallet.components.DatePickerView
import com.example.wallet.model.Expanse

@Composable
fun ExpansesScreen(navHostController: NavHostController) {
    val viewModel: ExpansesViewModel = viewModel() //ViewModel is bound to a composable
    val expanses = viewModel.expansesState.value
    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        TransactionListSection()
        ExpanseSection(expanses)
    }

}

@Composable
fun TransactionListSection(){
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
//            var datePicked : String? by remember {
//                mutableStateOf(null)
//            }
//
//            val updatedDate = { date : Long? ->
//                datePicked = date?.toString()?:""
//            }
//            Scaffold(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//            ){
//            DatePickerView(datePicked = datePicked, updatedDate = updatedDate )}
        }
        Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(start = 25.dp)) {
            Text(text = "Total expanses:",style =MaterialTheme.typography.h6, color = Color.White )
        }
    }
}


@Composable
fun ExpanseSection(expanses: List<Expanse>){
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(expanses) { expanse ->
            ReusableRow(categoryName = expanse.categoryName, date = "Date", location = "Location", amount = expanse.amount)
        }
    }
}


@Composable
//Parameters: CategoryName, wallet, date, location,amount
private fun ExpanseRow(categoryName: String, date: String, location: String, amount: Int) {
    ReusableRow(categoryName,date, location, amount  )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ReusableRow(categoryName: String, date: String, location: String, amount: Int) {
    val amountSign = "$ "

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
                        text = amountSign,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "121",
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
                Text(
                    text = "Animation",
                    style = MaterialTheme.typography.body1,

                    )
            }


        }
    }

}

@Composable
fun CategoryImage() {
    Image(
        painter = painterResource(id = R.drawable.groceries),
        contentDescription = "",
        modifier = Modifier.size(25.dp)
    )
}

@Preview
@Composable
fun ComposablePreview() {
    ReusableRow("","","",1)
}




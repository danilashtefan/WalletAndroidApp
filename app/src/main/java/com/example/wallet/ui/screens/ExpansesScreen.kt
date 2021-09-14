package com.example.wallet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.viewmodel.ExpansesViewModel
import com.example.wallet.R
import com.example.wallet.model.Expanse

@Composable
fun ExpansesScreen(navHostController: NavHostController) {
    val viewModel: ExpansesViewModel = viewModel() //ViewModel is bound to a composable
    val expanses = viewModel.expansesState.value
    Column {
        ExpanseSection(expanses)

    }

}


@Composable
fun ExpanseSection(expanses: List<Expanse>){
    LazyColumn {
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

@Composable
private fun ReusableRow(categoryName: String, date: String, location: String, amount: Int) {
    val amountSign = "$ "

Card() {
    Row(
        modifier = Modifier
            .height(68.dp),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Spacer(Modifier.width(12.dp))
        Column(Modifier) {
            Image(
                painter = painterResource(id = R.drawable.groceries),
                contentDescription = "",
                modifier = Modifier.size(25.dp)
            )
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
    Spacer(Modifier.width(30.dp))
}
}

@Preview
@Composable
fun ComposablePreview() {
    ReusableRow("","","",1)
}




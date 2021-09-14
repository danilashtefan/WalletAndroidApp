package com.example.wallet.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.Expanse
import com.example.wallet.model.viewmodel.ExpansesViewModel
import com.example.wallet.ui.theme.WalletTheme
import com.example.wallet.R

@Composable
fun ExpansesScreen(navHostController: NavHostController) {
    val viewModel: ExpansesViewModel = viewModel() //ViewModel is bound to a composable
    val expanses = viewModel.expansesState.value
    Column() {
        LazyColumn {
            items(expanses) { expanse ->
                Text(text = expanse.categoryName)
            }
        }
    }

}

@Composable
//Parameters: CategoryName, wallet, date, location,amount
private fun ExpanseRow(categoryName: String, date: String, location: String, amount: Int) {
    BaseRow(/*TODO*/)
}

@Composable
private fun BaseRow(/*TODO*/) {

    val amountSign = "$ "


    Row( modifier = Modifier
        .height(68.dp),
        verticalAlignment =  Alignment.CenterVertically
    ){
        Spacer(Modifier.width(12.dp))
        Column(Modifier) {
            Image(
                painter = painterResource(id = R.drawable.groceries ),
                contentDescription = "",
            modifier = Modifier.size(25.dp))
            Spacer(Modifier.width(15.dp))
            Text(text = "Bills")
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
//    Card(
//        shape = CircleShape,
//        border = BorderStroke(width = 2.dp, color = Color.Cyan),
//        modifier = Modifier.padding(16.dp),
//        elevation = 4.dp
//    ){
//        Image(painter = painterResource(id = R.drawable.groceries),
//            contentDescription ="",
//        modifier = Modifier.size(72.dp),
//        contentScale = ContentScale.Crop)
//
//    }

}

@Preview
@Composable
fun ComposablePreview() {
    BaseRow()
}




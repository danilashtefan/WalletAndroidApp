package com.example.wallet.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.model.ExpansesViewModel
import com.example.wallet.ui.theme.WalletTheme

@Composable
fun ExpansesScreen() {
    val viewModel: ExpansesViewModel = viewModel() //ViewModel is bound to a composable
    val expanses = viewModel.expansesState.value
    Column() {
        LazyColumn{
            items(expanses){expanse->
                Text(text = expanse.name)

            }
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Something")

        }
    }



}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WalletTheme {
        ExpansesScreen()
    }
}
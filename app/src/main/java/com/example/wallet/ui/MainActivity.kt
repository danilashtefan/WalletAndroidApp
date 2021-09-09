package com.example.wallet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.Expanse
import com.example.wallet.ui.theme.WalletTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import kotlin.math.exp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletTheme() {
                ExpansesReportScreen()
            }


        }
    }
}

@Composable
fun ExpansesReportScreen() {
    val viewModel: ExpansesViewModel = viewModel() //ViewModel is bound to a composable
    val expanses = viewModel.expansesState.value
    LazyColumn{
        items(expanses){expanse->
            Text(text = expanse.name)
            
        }
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WalletTheme {
        ExpansesReportScreen()

    }
}
package com.example.wallet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.Expanse
import com.example.wallet.ui.theme.WalletTheme
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
    val expanses_ = remember { mutableStateOf((emptyList<Expanse>()))}
    viewModel.getMeals(){response ->
        val expanses = response?._embedded.expanses
        expanses_.value = expanses.orEmpty()
    }
Text(text = expanses_.value.toString())
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WalletTheme {
        ExpansesReportScreen()

    }
}
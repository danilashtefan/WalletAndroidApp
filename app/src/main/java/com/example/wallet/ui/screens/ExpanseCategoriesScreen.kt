package com.example.wallet.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.viewmodel.transactions.AddViewModel
import com.example.wallet.model.viewmodel.transactions.ExpanseCategoriesViewModel
import com.example.wallet.model.viewmodel.transactions.ExpenseCategoriesViewModelFactory
import java.util.*


@Composable
fun ExpanseCategoriesScreen(
    navHostController: NavHostController,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {

    val viewModel: ExpanseCategoriesViewModel = viewModel(factory = ExpenseCategoriesViewModelFactory(DataStorePreferenceRepository(
        LocalContext.current)))
    val listOfButtons = listOf<String>("Category", "Wallet")
    val expanseCategories = viewModel.expanseCategoriesState.value
    val accessToken = viewModel.accessToken.value
    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ){
        LogoSection(pictureSize = 93)
        ChooseWhatToAddSection(listOfButtons = listOfButtons, viewModel = viewModel)
    }


}

@Composable
fun ChooseWhatToAddSection(listOfButtons: List<String>, viewModel: ExpanseCategoriesViewModel) {
    LazyRow(modifier = Modifier.padding(start = 60.dp)) {
        items(listOfButtons) { element ->
            OutlinedButton(onClick = {
                viewModel.whatToSeeState.value =
                    element.lowercase(Locale.getDefault())
            }, Modifier.padding(20.dp)) {
                Text(text = element)
            }
        }
    }
}









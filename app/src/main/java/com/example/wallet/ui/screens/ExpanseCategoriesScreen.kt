package com.example.wallet.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.wallet.model.viewmodel.transactions.ExpanseCategoriesViewModel
import com.example.wallet.model.viewmodel.transactions.ExpenseCategoriesViewModelFactory


@Composable
fun ExpanseCategoriesScreen(
    navHostController: NavHostController,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {

    val viewModel: ExpanseCategoriesViewModel = viewModel(factory = ExpenseCategoriesViewModelFactory(DataStorePreferenceRepository(
        LocalContext.current)))
    val expanseCategories = viewModel.expanseCategoriesState.value
    val accessToken = viewModel.accessToken.value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn{
            items(expanseCategories){expanseCategory->
                Text(text = "Access token: $accessToken")
            }
        }

    }


}





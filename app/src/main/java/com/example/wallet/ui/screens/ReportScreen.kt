package com.example.wallet.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.viewmodel.transactions.ReportViewModel
import com.example.wallet.model.viewmodel.transactions.ReportViewModelFactory

@Composable
fun ReportScreen (navController: NavHostController){

    val viewModel: ReportViewModel = viewModel(
        factory = ReportViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    )
}
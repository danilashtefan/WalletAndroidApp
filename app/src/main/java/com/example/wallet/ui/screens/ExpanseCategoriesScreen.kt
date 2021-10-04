package com.example.wallet.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.viewmodel.transactions.ExpanseCategoriesViewModel


@Composable
fun ExpanseCategoriesScreen(navHostController: NavHostController) {
    val viewModel: ExpanseCategoriesViewModel = viewModel()
    val expanseCategories = viewModel.expanseCategoriesState.value

    Column() {
        LazyColumn{
            items(expanseCategories){expanseCategory->
                Text(text = expanseCategory.expanseCategoryName)
            }
        }
    }
}
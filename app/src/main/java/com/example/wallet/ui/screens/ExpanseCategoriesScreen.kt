package com.example.wallet.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.helpers.DateFormatter
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem
import com.example.wallet.model.viewmodel.transactions.AddViewModel
import com.example.wallet.model.viewmodel.transactions.ExpanseCategoriesViewModel
import com.example.wallet.model.viewmodel.transactions.ExpansesViewModel
import com.example.wallet.model.viewmodel.transactions.ExpenseCategoriesViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun ExpanseCategoriesScreen(
    navHostController: NavHostController,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {

    val viewModel: ExpanseCategoriesViewModel = viewModel(
        factory = ExpenseCategoriesViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    )
    val listOfButtons = listOf<String>("Category", "Wallet")
    val expanseCategories = viewModel.expanseCategoriesState.value
    val accessToken = viewModel.accessToken.value
    var dataLoaded = viewModel.dataLoaded.value
    val transactionsCategories = viewModel.transactionCetegoriesState.value
    val transactionsWallet = viewModel.transactionWalletsState.value

    if (!dataLoaded) {
        return;
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        LogoSection(pictureSize = 93)
        ChooseWhatToSeeSection(listOfButtons = listOfButtons, viewModel = viewModel)
        DetailsSection(viewModel)
    }


}

@Composable
fun ChooseWhatToSeeSection(listOfButtons: List<String>, viewModel: ExpanseCategoriesViewModel) {
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

@Composable
fun DetailsSection(viewModel: ExpanseCategoriesViewModel) {
    when (viewModel.whatToSeeState.value) {
        "category" -> CategoriesListSection(viewModel)
        "wallet" -> WalletsListSection(viewModel)
    }
}

@Composable
fun CategoriesListSection(
    viewModel: ExpanseCategoriesViewModel,
    expanseCategories:
) {


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(expanseCategories) { expanse ->
            val expanseId = expanse.id
            ReusableCategoryAndRow(
                categoryIcon = expanse.categoryIcon,
                categoryName = expanse.categoryName,
                type = expanse.type,
                editClickAction = {

                    //navController.navigate("transactionDetails/$expanseId")
                },
                deleteClickAction = {
                    Log.d("INFO", "Delete button pressed")
                })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalletsListSection(viewModel: ExpanseCategoriesViewModel) {
    Text(text = "HELLO WALLET")
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ReusableCategoryAndRow(
    categoryIcon: String,
    categoryName: String,
    type: String,
    editClickAction: () -> Unit,
    deleteClickAction: () -> Unit
) {

    Card(
        backgroundColor = Color.White,
        modifier = Modifier.padding(6.dp)
    ) {
        var expanded by remember { mutableStateOf(false) }
        Column(Modifier.clickable { expanded = !expanded }) {
            Row(
                modifier = Modifier
                    .height(68.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(Modifier.width(12.dp))
                Column(Modifier) {
                    CategoryImage(categoryIcon, 30)
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
                        text = sign,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 3.dp)
                    )

                    Text(
                        text = amount.toString(),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = currency,
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
                Column() {
                    Text(text = "Location: " + location)
                    Text(text = "Date: " + date)
                    Text(text = "Comment: " + comments)
                    Text(text = "Type: " + type)
                    Row() {
                        OutlinedButton(onClick = { editClickAction.invoke() }) {
                            Text(text = "Edit")
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        OutlinedButton(onClick = { deleteClickAction.invoke() }) {
                            Text(text = "Delete", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowImage(categoryIcon: String, size: Int) {
    Emoji(emojiCode = categoryIcon, size = size)
}







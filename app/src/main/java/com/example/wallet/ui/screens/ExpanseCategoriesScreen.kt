package com.example.wallet.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.R
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import com.example.wallet.model.viewmodel.transactions.ExpanseCategoriesViewModel
import com.example.wallet.model.viewmodel.transactions.ExpenseCategoriesViewModelFactory
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
    var dataLoaded = viewModel.dataLoaded.value
    val transactionsCategories = viewModel.transactionCetegoriesState.value
    val transactionsWallets = viewModel.transactionWalletsState.value

    if (!dataLoaded) {
        return;
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        var showCategoryAlertDialog = viewModel.showCategoryAlertDialog.value
        var showWalletAlertDialog = viewModel.showWalletAlertDialog.value
        if (showCategoryAlertDialog) {
            TwoButtonAlertDialogComponent(
                onDismiss = { viewModel.showCategoryAlertDialog.value = false },
                bodyText = viewModel.dialogText.value,
                dismissButtonText = "CANCEL",
                confirmButtonText = "CONFIRM",
                onConfirm = {
                    viewModel.showCategoryAlertDialog.value = false
                    viewModel.deleteCategory(viewModel.categoryToDelete.value)
                }
            )
        }
        if(showWalletAlertDialog){
            TwoButtonAlertDialogComponent(
                onDismiss = { viewModel.showWalletAlertDialog.value = false },
                bodyText = viewModel.dialogText.value,
                dismissButtonText = "CANCEL",
                confirmButtonText = "CONFIRM",
                onConfirm = {
                    viewModel.showCategoryAlertDialog.value = false
                    viewModel.deleteWallet(viewModel.walletToDelete.value)}
            )
        }
        LogoSection(pictureSize = 93)
        ChooseWhatToSeeSection(listOfButtons = listOfButtons, viewModel = viewModel)
        DetailsSection(
            viewModel,
            transactionsCategories,
            transactionsWallets,
            navController = navHostController
        )
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
fun DetailsSection(
    viewModel: ExpanseCategoriesViewModel,
    transactionsCategories: List<SecondAllExpenseCategoriesResponseItem>,
    transactionWallets: List<SecondAllWalletsResponseItem>,
    navController: NavHostController

) {
    when (viewModel.whatToSeeState.value) {
        "category" -> CategoriesListSection(
            viewModel,
            transactionsCategories,
            navController = navController
        )
        "wallet" -> WalletsListSection(viewModel, transactionWallets, navController)
    }
}

@Composable
fun CategoriesListSection(
    viewModel: ExpanseCategoriesViewModel,
    transactionsCategories: List<SecondAllExpenseCategoriesResponseItem>,
    navController: NavHostController
) {


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(transactionsCategories) { category ->
            val categoryId = category.id
            ReusableCategoryAndWalletRow(
                route = "categoryStatistics/$categoryId",
                icon = category.icon,
                name = category.expanseCategoryName,
                type = category.type,
                id = categoryId,
                editClickAction = {
                    navController.navigate("categoriesDetails/$categoryId")
                },
                deleteClickAction = {
                    Log.d("INFO", "Delete button pressed")
                    viewModel.categoryToDelete.value = category
                    viewModel.deleteCategoryDialogShow()
                },
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalletsListSection(
    viewModel: ExpanseCategoriesViewModel,
    transactionsWallets: List<SecondAllWalletsResponseItem>,
    navController: NavHostController
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(transactionsWallets) { wallet ->
            val walletId = wallet.id
            ReusableCategoryAndWalletRow(
                icon = wallet.icon,
                route = "walletStatistics/$walletId",
                name = wallet.walletName,
                type = wallet.currency,
                id = walletId,
                editClickAction = {
                    navController.navigate("walletsDetails/$walletId")
                },
                deleteClickAction = {
                    Log.d("INFO", "Delete button pressed")
                    viewModel.walletToDelete.value = wallet
                    viewModel.deleteWalletDialogShow()
                },
                navController = navController
            )
        }
    }
}

@Composable
fun TwoButtonAlertDialogComponent(
    onDismiss: () -> Unit,
    bodyText: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Wallet", color = Color.White) },

        text = { Text(bodyText, color = Color.White) },

        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(confirmButtonText, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {

                Text(dismissButtonText, color = Color.White)
            }
        },
        backgroundColor = colorResource(id = R.color.purple_200),
        contentColor = Color.White
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReusableCategoryAndWalletRow(
    icon: String,
    name: String,
    type: String,
    id: Int,
    route: String,
    editClickAction: () -> Unit,
    deleteClickAction: () -> Unit,
    navController: NavHostController
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
                    CategoryImage(icon, 30)
                    //Spacer(Modifier.width(15.dp)
                }
                Spacer(Modifier.weight(0.2f))
                Column(Modifier) {
                    Row(Modifier) {
                        Text("Name: ")
                        Text("" + name, fontWeight = FontWeight.Bold)
                    }
                    Row(Modifier) {
                        Text("Type: ")
                        Text("" + type, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.weight(0.3f))
                Spacer(Modifier.width(16.dp))
                IconButton(onClick = { navController.navigate(route) }) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(24.dp),
                    )
                }

            }

            AnimatedVisibility(visible = expanded) {
                Column() {
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







package com.example.wallet.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem
import com.example.wallet.model.viewmodel.transactions.TransactionCategoriesDetailsViewModel
import com.example.wallet.model.viewmodel.transactions.TransactionCategoriesDetailsViewModelFactory
import com.example.wallet.model.viewmodel.transactions.TransactionDetailsViewModel
import com.example.wallet.model.viewmodel.transactions.TransactionDetailsViewModelFactory

@Composable
fun TransactionCategoriesDetailsScreen(
    navController: NavHostController,
    categoryId: Int,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {
    val viewModel: TransactionCategoriesDetailsViewModel = viewModel(
        factory = TransactionCategoriesDetailsViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    ) //ViewModel is bound to a composable
    viewModel.setCategoryId(categoryId)
    var dataLoaded = viewModel.dataLoaded.value
    val nameFieldName = "name"
    val typeFieldName = "type"
    val fieldsOnTheScreen = arrayListOf<String>(
        nameFieldName,
        typeFieldName
    )
    var category = viewModel.category.value

    if (dataLoaded === false) {
        return;
    }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        LogoTransactionDetailsSection()
        CategoriesDetailsImageSection(category)
//        EditableFieldTransactionDetails(
//            padding = 100,
//            field = nameFieldName,
//            labelText = "Name",
//            value = transaction.name,
//            viewModel = viewModel
//        )
//        EditableFieldTransactionDetails(
//            padding = 20,
//            field = amountFieldName,
//            labelText = "Amount",
//            value = transaction.amount,
//            viewModel = viewModel
//        )
//        EditableFieldTransactionDetails(
//            padding = 20,
//            field = typeFieldName,
//            labelText = "Type",
//            value = transaction.type,
//            viewModel = viewModel
//        )
//        //InfoRow(20,"Category")
//        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
//            Text(text = "Category: ")
//            CategorySelctorTransactionDetails(
//                padding = 20,
//                labelText = transaction.categoryName,
//                optionsList = transactionsCategories,
//                viewModel = viewModel,
//                transactionId = transaction.id
//            )
//        }
//
//        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
//            Text(text = "Wallet: ")
//
//            WalletSelctorTransactionDetails(
//                padding = 20,
//                labelText = transaction.walletName,
//                optionsList = transactionsWallet,
//                viewModel = viewModel,
//                transactionId = transactionId
//            )
//
//        }
//        EditableFieldTransactionDetails(
//            20,
//            field = dateFieldName,
//            labelText = "Date",
//            value = transaction.date,
//            viewModel = viewModel
//        )
//        EditableFieldTransactionDetails(
//            20,
//            field = commentsFieldName,
//            labelText = "Comments",
//            value = transaction.comments,
//            viewModel = viewModel
//        )
//        EditableFieldTransactionDetails(
//            20,
//            field = locationFieldName,
//            labelText = "Location",
//            value = transaction.location,
//            viewModel = viewModel
//        )
//        Spacer(modifier = Modifier.size(20.dp))
//        SaveButtonTransactionDetails(navController, fieldsOnTheScreen, viewModel)
////        Spacer(modifier = Modifier.size(20.dp))
////        DeleteButtonTransactionDetails(
////            navController,
////            expenseId = transactionId,
////            viewModel = viewModel
////        )

    }
}


@Composable
fun CategoriesDetailsImageSection(category: SecondAllExpenseCategoriesResponseItem) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            shape = CircleShape,
            border = BorderStroke(width = 2.dp, color = Color.White),
            modifier = Modifier.size(150.dp),
            elevation = 5.dp
        ) {
            CategoryImage(category.icon, 50)
        }
    }
}
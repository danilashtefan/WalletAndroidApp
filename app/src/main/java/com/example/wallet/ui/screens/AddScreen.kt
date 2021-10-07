package com.example.wallet.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.response.transactions.Wallet
import com.example.wallet.model.viewmodel.transactions.AddViewModel
import java.util.*

@Composable
fun AddScreen(navController: NavHostController) {
    val viewModel: AddViewModel = viewModel()
    val listOfButtons = listOf<String>("Transaction", "Category", "Wallet")
    var dataLoaded = viewModel.dataLoaded.value
    if (dataLoaded === false) {
        return;
    }
    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        LogoSection(pictureSize = 90)
        ChooseWhatToAddSection(listOfButtons, viewModel)
        AddingSection(viewModel)
    }
}

@Composable
fun AddingSection(viewModel: AddViewModel) {
    when (viewModel.whatToAddstate.value) {
        "" -> TransactionAddSection(viewModel)
        "transaction" -> TransactionAddSection(viewModel)
        "category" -> CategoryAddSection()
        "wallet" -> WalletAddSection()
    }
}

@Composable
fun WalletAddSection() {
    TODO("Not yet implemented")
}

@Composable
fun CategoryAddSection() {
    TODO("Not yet implemented")
}

@Composable
fun TransactionAddSection(viewModel: AddViewModel) {

    val nameFieldName = "name"
    val amountFieldName = "amount"
    val typeFieldName = "type"
    val dateFieldName = "date"
    val commentsFieldName = "comments"
    val locationFieldName = "location"
    val categoryFieldName = "categoryName"
    val walletFieldName = "walletName"
    val fieldsOnTheScreen = arrayListOf<String>(
        nameFieldName,
        amountFieldName,
        typeFieldName,
        dateFieldName,
        commentsFieldName,
        locationFieldName,
        categoryFieldName,
        walletFieldName
    )


    Column(Modifier.verticalScroll(rememberScrollState())) {
        ImageSection()
        EditableFieldTransactionAdd(
            padding = 30,
            field = nameFieldName,
            labelText = "Name",
            value = "",
            viewModel = viewModel,
        )
        EditableFieldTransactionAdd(
            padding = 20,
            field = amountFieldName,
            labelText = "Amount",
            value = "",
            viewModel = viewModel,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )


        TypeSelectorTransactionAdd(
            padding = 20,
            labelText = viewModel.typeFieldTemporaryValueBeforeSavingtoDB,
            optionsList = listOf("Expense", "Income"),
            viewModel = viewModel
        )


        CategorySelectorTransactionAdd(
            padding = 20,
            labelText = viewModel.categoryNameFieldTemporaryValueBeforeSavingtoDB,
            optionsList = viewModel.transactionCetegoriesState.value,
            viewModel = viewModel
        )

        WalletSelectorTransactionAdd(padding = 20,
            labelText = viewModel.walletNameFieldTemporaryValueBeforeSavingtoDB ,
            optionsList = viewModel.transactionWalletsState.value,
            viewModel = viewModel )


        EditableFieldTransactionAdd(
            padding = 20,
            field = dateFieldName,
            labelText = "Date",
            value = "",
            viewModel = viewModel
        )

        EditableFieldTransactionAdd(
            padding = 20,
            field = commentsFieldName,
            labelText = "Comments",
            value = "",
            viewModel = viewModel
        )

        EditableFieldTransactionAdd(
            padding = 20,
            field = locationFieldName,
            labelText = "Location",
            value = "",
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.size(20.dp))
        SaveButtonTransactionAdd(fieldsOnTheScreen = fieldsOnTheScreen, viewModel =viewModel )

    }
}

@Composable
private fun SaveButtonTransactionAdd(
    fieldsOnTheScreen: ArrayList<String>,
    viewModel: AddViewModel
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(modifier = Modifier.padding(bottom = 20.dp), onClick = {
            viewModel.addTransactionToDb()
        }) {
            Text(text = "Add transaction")
        }
    }
}
@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun WalletSelectorTransactionAdd(
    padding: Int,
    labelText: String?,
    optionsList: List<Wallet>,
    enabled: Boolean = true,
    viewModel: AddViewModel,
) {
    Column() {
        var expanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .padding(top = padding.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            OutlinedButton(onClick = { expanded = !expanded }, enabled = enabled) {
                if (labelText != null) {
                    Text(text = labelText)
                }
            }
        }
        AnimatedVisibility(visible = expanded) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                for (option in optionsList) {
                    Card(onClick = {
                        viewModel.updateWalletLinkValueBeforeSavingToDB(option)
                    }, modifier = Modifier.padding(7.dp)) {
                        Text(option.walletName)
                    }
                }

            }
        }
    }

}
@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun CategorySelectorTransactionAdd(
    padding: Int,
    labelText: String?,
    optionsList: List<ExpanseCategory>,
    enabled: Boolean = true,
    viewModel: AddViewModel,
) {
    Column() {
        var expanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .padding(top = padding.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            OutlinedButton(onClick = { expanded = !expanded }, enabled = enabled) {
                if (labelText != null) {
                    Text(text = labelText)
                }
            }
        }
        AnimatedVisibility(visible = expanded) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                for (option in optionsList) {
                    Card(onClick = {
                        viewModel.updateCategoryLinkValueBeforeSavingToDB(option)
                    }, modifier = Modifier.padding(7.dp)) {
                        Text(option.expanseCategoryName)
                    }
                }

            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun TypeSelectorTransactionAdd(
    padding: Int,
    labelText: String?,
    optionsList: List<String>,
    enabled: Boolean = true,
    viewModel: AddViewModel,
) {

    Column() {
        var expanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .padding(top = padding.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            OutlinedButton(onClick = { expanded = !expanded }, enabled = enabled) {
                if (labelText != null) {
                    Text(text = labelText)
                }
            }
        }
        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                for (option in optionsList) {
                    Card(onClick = {
                        viewModel.updateTemporaryFieldValueBeforeSavingToDB("type", option)
                    }, modifier = Modifier.padding(7.dp)) {
                        Text(option)
                    }
                }

            }
        }
    }

}


@Composable
fun EditableFieldTransactionAdd(
    padding: Int,
    field: String,
    labelText: String,
    value: Any?,
    viewModel: AddViewModel,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
) {
    Row(
        modifier = Modifier
            .padding(top = padding.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        var text = value.toString()
        if (text === null) {
            text = ""
        }
        val textState = remember { mutableStateOf(TextFieldValue(text)) }

        TextField(
            value = textState.value,
            enabled = enabled,
            onValueChange = {
                textState.value = it
                viewModel.updateTemporaryFieldValueBeforeSavingToDB(field, textState.value.text)
            },
            label = { Text(labelText) },
            keyboardOptions = keyboardOptions
        )
        Spacer(modifier = Modifier.size(20.dp))

    }
}


@Composable
fun ChooseWhatToAddSection(listOfButtons: List<String>, viewModel: AddViewModel) {
    LazyRow(modifier = Modifier.padding(start = 10.dp)) {
        items(listOfButtons) { element ->
            OutlinedButton(onClick = {
                viewModel.whatToAddstate.value =
                    element.lowercase(Locale.getDefault())
            }, Modifier.padding(20.dp)) {
                Text(text = element)
            }
        }
    }
}

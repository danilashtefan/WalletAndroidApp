package com.example.wallet.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.model.response.transactions.Wallet
import com.example.wallet.model.viewmodel.transactions.AddViewModel
import com.example.wallet.model.viewmodel.transactions.TransactionDetailsViewModel
import java.util.*

@Composable
fun AddScreen(navController: NavHostController) {
    val viewModel: AddViewModel = viewModel()
    val listOfButtons = listOf<String>("Transaction", "Category", "Wallet")

    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        LogoSection()
        ChooseWhatToAddSection(listOfButtons, viewModel)
        AddingSection(viewModel)
    }
}

@Composable
fun AddingSection(viewModel: AddViewModel) {
    when (viewModel.whatToAddstate.value) {
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


    Column(Modifier.verticalScroll(rememberScrollState())) {
        ImageSection()
        EditableFieldTransactionAdd(
            padding = 100,
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

        TypeSelectorTransactionAdd()


    }
}

@Composable
fun TypeSelectorTransactionAdd(

) {

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

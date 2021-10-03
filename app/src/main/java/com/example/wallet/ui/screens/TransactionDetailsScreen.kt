package com.example.wallet.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.R
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.viewmodel.transactions.TransactionDetailsViewModel

@Composable
fun TransactionDetailsScreen(transactionId: Int) {
    val viewModel: TransactionDetailsViewModel = viewModel() //ViewModel is bound to a composable
    viewModel.setTransactionId(transactionId)
    var dataLoaded = viewModel.dataLoaded.value
    var transaction = viewModel.transaction.value
    val transactionsCategories = viewModel.transactionCetegoriesState.value
    val nameFieldName = "name"
    val amountFieldName = "amount"
    val typeFieldName = "type"
    val dateFieldName = "date"
    val commentsFieldName = "comments"
    val locationFieldName = "location"
    val categoryFieldName = "categoryName"
    val fieldsOnTheScreen = arrayListOf<String>(
        nameFieldName,
        amountFieldName,
        typeFieldName,
        dateFieldName,
        commentsFieldName,
        locationFieldName,
        categoryFieldName
    )

    if (dataLoaded === false) {
        return;
    }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Image(
                painter = painterResource(id = R.drawable.wallet_no_background_cropped),
                contentDescription = "",
                modifier = Modifier
                    .size(70.dp)
                    .padding(top = 10.dp, end = 10.dp)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Card(
                shape = CircleShape,
                border = BorderStroke(width = 2.dp, color = Color.White),
                modifier = Modifier.size(150.dp),
                elevation = 5.dp
            ) {
                CategoryImage()
            }
        }
        EditableField(
            padding = 100,
            field = nameFieldName,
            labelText = "Name",
            value = transaction.name,
            viewModel = viewModel
        )
        EditableField(
            padding = 20,
            field = amountFieldName,
            labelText = "Amount",
            value = transaction.amount,
            viewModel = viewModel
        )
        EditableField(
            padding = 20,
            field = typeFieldName,
            labelText = "Type",
            value = transaction.type,
            viewModel = viewModel
        )
        //InfoRow(20,"Category")
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Category: ")
            CategorySelctor(
                padding = 20,
                labelText = transaction.categoryName,
                transactionsCategories,
                viewModel = viewModel,
                transactionId = transaction.id
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Wallet: ")

         /*TODO: Add wallet selector*/

        }
        EditableField(
            20,
            field = dateFieldName,
            labelText = "Date",
            value=transaction.date,
            viewModel = viewModel
        )
        EditableField(
            20,
            field = commentsFieldName,
            labelText = "Comments",
            value = transaction.comments,
            viewModel = viewModel
        )
        EditableField(
            20,
            field = locationFieldName,
            labelText = "Location",
           value=transaction.location,
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.size(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            OutlinedButton(modifier = Modifier.padding(bottom = 20.dp),onClick = {
                for(field in fieldsOnTheScreen){
                   /*TODO ADD VIEWMODEL UPDATE FIELD METHOD FINISH FOR WALLET AND CATEGORY*/
                    viewModel.updateField(field,viewModel.getFieldToUpdateInDB(field))
                }
                 viewModel.updateTransactionInDb()
            }) {
                Text(text = "Save the changes")
            }
        }

    }
}

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
private fun CategorySelctor(
    padding: Int,
    labelText: String,
    optionsList: List<ExpanseCategory>,
    enabled: Boolean = true,
    viewModel: TransactionDetailsViewModel,
    transactionId: Int
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
                Text(text = labelText)
            }
        }
        AnimatedVisibility(visible = expanded) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                for (option in optionsList) {
                    Card(onClick = {
                        //viewModel.chooseCategory(option)
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
private fun WalletSelctor(
    padding: Int,
    labelText: String,
    optionsList: List<ExpanseCategory>,
    enabled: Boolean = true,
    viewModel: TransactionDetailsViewModel,
    transactionId: Int
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
                Text(text = labelText)
            }
        }
        AnimatedVisibility(visible = expanded) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                for (option in optionsList) {
                    Card(onClick = {
                        //viewModel.chooseCategory(option)
                        viewModel.updateCategoryLinkValueBeforeSavingToDB(option)
                    }, modifier = Modifier.padding(7.dp)) {
                        Text(option.expanseCategoryName)
                    }
                }

            }
        }
    }

}



@Composable
private fun EditableField(
    padding: Int,
    field: String,
    labelText: String,
    value: Any?,
    viewModel: TransactionDetailsViewModel,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .padding(top = padding.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        var keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)

        when (value) {
            is Int -> keyboardOptions =
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        }

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
              //  viewModel.updateField(field, textState.value.text)
                viewModel.updateTemporaryFieldValueBeforeSavingToDB(field, textState.value.text)

            },
            label = { Text(labelText) },
            keyboardOptions = keyboardOptions
        )
    }
}



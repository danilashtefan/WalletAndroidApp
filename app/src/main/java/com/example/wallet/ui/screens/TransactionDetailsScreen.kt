package com.example.wallet.ui.screens

import android.text.Editable
import android.util.Log
import android.widget.CalendarView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.R
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.ExpanseCategory
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import com.example.wallet.model.response.transactions.Wallet
import com.example.wallet.model.viewmodel.transactions.AddViewModel
import com.example.wallet.model.viewmodel.transactions.TransactionDetailsViewModel
import com.example.wallet.model.viewmodel.transactions.TransactionDetailsViewModelFactory
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.util.*
import kotlin.collections.ArrayList

@Composable
fun TransactionDetailsScreen(
    navController: NavHostController,
    transactionId: Int,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {
    val viewModel: TransactionDetailsViewModel = viewModel(
        factory = TransactionDetailsViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    ) //ViewModel is bound to a composable
    viewModel.setTransactionId(transactionId)
    var dataLoaded = viewModel.dataLoaded.value
    var transaction = viewModel.transaction.value
    val transactionsCategories = viewModel.transactionCetegoriesState.value
    val transactionsWallet = viewModel.transactionWalletsState.value
    val nameFieldName = "name"
    val amountFieldName = "amount"
    val typeFieldName = "type"
    val dateFieldName = "date"
    val commentsFieldName = "comments"
    val locationFieldName = "location"
    val categoryFieldName = "categoryName"
    val walletFieldName = "walletName"
    val categoryIcon = "categoryIcon"
    val fieldsOnTheScreen = arrayListOf<String>(
        nameFieldName,
        amountFieldName,
        typeFieldName,
        dateFieldName,
        commentsFieldName,
        locationFieldName,
        categoryFieldName,
        walletFieldName,
        categoryIcon
    )


    if (dataLoaded === false) {
        return;
    }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        var showIncorrectDataAlertDialog = viewModel.showIncorrectDataAlertDialog.value
        if (showIncorrectDataAlertDialog) {
            OneButtonAlertDialogComponent(
                onDismiss = { viewModel.incorrectDataDialogClose() },
                bodyText = { Text(viewModel.incorrectDataAlertDialogText, color = Color.White) },
                buttonText = "DISMISS"
            )
        }
        LogoTransactionDetailsSection()
        ImageSection(transaction)
        TypeOfElementToAddOrEditText("Edit transaction")
        EditableFieldTransactionDetails(
            padding = 50,
            field = nameFieldName,
            labelText = "Name",
            value = transaction.name,
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.notes),
                    contentDescription = "wallet",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            },
            viewModel = viewModel
        )
        EditableFieldTransactionDetails(
            padding = 20,
            field = amountFieldName,
            labelText = "Amount",
            value = transaction.amount,
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.wallet),
                    contentDescription = "wallet",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            },
            viewModel = viewModel
        )
//        EditableFieldTransactionDetails(
//            padding = 20,
//            field = typeFieldName,
//            labelText = "Type",
//            value = transaction.type,
//            viewModel = viewModel
//        )
        TypeSelectorTransactionDetails(
            padding = 20,
            labelText = viewModel.typeFieldTemporaryValueBeforeSavingtoDB.value,
            optionsList = listOf("Expense", "Income"),
            viewModel = viewModel,
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.category),
                    contentDescription = "wallet",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        )
        //InfoRow(20,"Category")
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            CategorySelctorTransactionDetails(
                padding = 20,
                labelText = viewModel.categoryNameFieldTemporaryValueBeforeSavingtoDB.value,
                optionsList = transactionsCategories,
                viewModel = viewModel,
                transactionId = transaction.id,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.infography),
                        contentDescription = "wallet",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                }
            )

        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            WalletSelctorTransactionDetails(
                padding = 20,
                labelText = viewModel.walletNameFieldTemporaryValueBeforeSavingtoDB.value,
                optionsList = transactionsWallet,
                viewModel = viewModel,
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.money),
                        contentDescription = "wallet",
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                },
                transactionId = transactionId,

                )

        }
//        EditableFieldTransactionDetails(
//            20,
//            field = dateFieldName,
//            labelText = "Date",
//            value = transaction.date,
//            viewModel = viewModel
//        )

        DatePicker(viewModel = viewModel, padding = 20, leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "wallet",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        })

        EditableFieldTransactionDetails(
            20,
            field = commentsFieldName,
            labelText = "Comments",
            value = transaction.comments,
            viewModel = viewModel,
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.thinking),
                    contentDescription = "groceries",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        )
        EditableFieldLocationTransactionDetails(
            20,
            field = locationFieldName,
            labelText = "Location",
            value = transaction.location,
            viewModel = viewModel,
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "groceries",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        )
        Spacer(modifier = Modifier.size(20.dp))
        SaveButtonTransactionDetails(navController, fieldsOnTheScreen, viewModel)
    }
}

@Composable
private fun SaveButtonTransactionDetails(
    navController: NavHostController,
    fieldsOnTheScreen: ArrayList<String>,
    viewModel: TransactionDetailsViewModel
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(modifier = Modifier.padding(bottom = 20.dp), onClick = {
            for (field in fieldsOnTheScreen) {
                viewModel.updateField(field, viewModel.getFieldToUpdateInDB(field))
            }
            val editResult = viewModel.updateTransactionInDb()
            Thread.sleep(500)
            if (editResult) {
                navController.navigate("expanses")
            }

        }) {
            Text(text = "Save the changes")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DatePicker(
    viewModel: TransactionDetailsViewModel,
    padding: Int,
    leadingIcon: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = padding.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = {
            viewModel.expandedCalendar.value = !viewModel.expandedCalendar.value
        }, modifier = Modifier.width(200.dp)) {
            Column(
                Modifier
                    .weight(1F)
                    .wrapContentSize(Alignment.CenterStart)
            ) {
                leadingIcon()
            }
            Column(
                Modifier
                    .weight(4F)
            ) {
                viewModel.datePicked.value?.let { Text(text = it) }

            }
        }

    }
    Row(horizontalArrangement = Arrangement.Center) {
        AnimatedVisibility(visible = viewModel.expandedCalendar.value) {
            EditCalendarDatePicker(viewModel)
        }

    }
}

@Composable
private fun EditCalendarDatePicker(viewModel: TransactionDetailsViewModel) {
    AndroidView(
        { CalendarView(it) },
        modifier = Modifier.wrapContentWidth(),
        update = { views ->
            views.setOnDateChangeListener { calendarView, year, month, day ->
                viewModel.datePicked.value =
                    year.toString() + "-" + (month + 1).toString() + "-" + day.toString()
                viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                    "date",
                    viewModel.datePicked.value
                )
            }
        }
    )
}


@Composable
private fun DeleteButtonTransactionDetails(
    navController: NavHostController,
    expenseId: Int,
    viewModel: TransactionDetailsViewModel
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(modifier = Modifier.padding(bottom = 20.dp), onClick = {
            viewModel.deleteTransaction(expenseId)
            Thread.sleep(500)
            navController.navigate("expanses")
        }) {
            Text(text = "Delete transaction", color = Color.Red)
        }
    }
}


@Composable
fun LogoTransactionDetailsSection() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Image(
            painter = painterResource(id = R.drawable.wallet_no_background_cropped),
            contentDescription = "",
            modifier = Modifier
                .size(70.dp)
                .padding(top = 10.dp, end = 10.dp)
        )
    }
}

@Composable
fun ImageSection(transaction: SecondAllExpensesItem) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            shape = CircleShape,
            border = BorderStroke(width = 2.dp, color = Color.White),
            modifier = Modifier.size(150.dp),
            elevation = 5.dp
        ) {
            CategoryImage(transaction.categoryIcon, 50)
        }
    }
}


@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
private fun CategorySelctorTransactionDetails(
    padding: Int,
    labelText: String,
    optionsList: List<SecondAllExpenseCategoriesResponseItem>,
    enabled: Boolean = true,
    viewModel: TransactionDetailsViewModel,
    transactionId: Int,
    leadingIcon: @Composable () -> Unit
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
            OutlinedButton(
                onClick = { expanded = !expanded },
                enabled = enabled,
                modifier = Modifier.width(200.dp)
            ) {
                Column(
                    Modifier
                        .weight(1F)
                        .wrapContentSize(Alignment.CenterStart)
                ) {
                    leadingIcon()
                }
                Column(
                    Modifier
                        .weight(4F)
                ) {
                    if (labelText != null) {
                        Text(
                            text = "Category: $labelText",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

            }
        }
        AnimatedVisibility(visible = expanded) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                for (option in optionsList) {
                    Card(onClick = {
                        viewModel.updateCategoryLinkValueBeforeSavingToDB(option)
                    }, modifier = Modifier.padding(7.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.padding(3.dp))
                            CategoryImage(option.icon, 30)
                            Text(option.expanseCategoryName)
                            Spacer(modifier = Modifier.padding(3.dp))
                        }
                    }
                }

            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun TypeSelectorTransactionDetails(
    padding: Int,
    labelText: String?,
    optionsList: List<String>,
    enabled: Boolean = true,
    viewModel: TransactionDetailsViewModel,
    leadingIcon: @Composable () -> Unit
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

            OutlinedButton(
                onClick = { expanded = !expanded },
                enabled = enabled,
                modifier = Modifier.width(200.dp)
            ) {

                Column(
                    Modifier
                        .weight(1F)
                        .wrapContentSize(Alignment.CenterStart)
                ) {
                    leadingIcon()
                }
                Column(
                    Modifier
                        .weight(4F)
                ) {
                    if (labelText != null) {
                        if (labelText != null) {
                            Text(text = "Type: $labelText", overflow = TextOverflow.Ellipsis)
                        }
                    }
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.padding(3.dp))
                            if(option.equals("Expense")){
                                CategoryImage("\uD83D\uDED1", 30)

                            }else{
                                CategoryImage("\uD83D\uDFE2", 30)
                            }
                            Text(option)
                            Spacer(modifier = Modifier.padding(3.dp))
                        }
                    }
                }

            }
        }
    }

}


@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
private fun WalletSelctorTransactionDetails(
    padding: Int,
    labelText: String,
    optionsList: List<SecondAllWalletsResponseItem>,
    enabled: Boolean = true,
    viewModel: TransactionDetailsViewModel,
    transactionId: Int,
    leadingIcon: @Composable () -> Unit
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

            OutlinedButton(
                onClick = { expanded = !expanded },
                enabled = enabled,
                modifier = Modifier.width(200.dp)
            ) {

                Column(
                    Modifier
                        .weight(1F)
                        .wrapContentSize(Alignment.CenterStart)
                ) {
                    leadingIcon()
                }
                Column(
                    Modifier
                        .weight(4F)
                ) {
                    if (labelText != null) {
                        Text(
                            text = "Wallet: $labelText",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

            }
        }
        AnimatedVisibility(visible = expanded) {
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                for (option in optionsList) {
                    Card(onClick = {
                        viewModel.updateWalletLinkValueBeforeSavingToDB(option)
                    }, modifier = Modifier.padding(7.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.padding(3.dp))
                            CategoryImage(option.icon, 30)
                            Text(option.walletName)
                            Spacer(modifier = Modifier.padding(3.dp))
                        }
                    }
                }

            }
        }
    }

}

@Composable
private fun EditableFieldLocationTransactionDetails(
    padding: Int,
    field: String,
    labelText: String,
    value: Any?,
    viewModel: TransactionDetailsViewModel,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    leadingIcon: @Composable () -> Unit
) {
    val listOfPlaces = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME)
    var context = LocalContext.current
    val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, listOfPlaces).build(
        context
    )
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            var place = Autocomplete.getPlaceFromIntent(it.data)
            viewModel.updateLocation(place.address)
            viewModel.updateTemporaryFieldValueBeforeSavingToDB(field, place.address)
            Log.d("INFO", "Address: ${place.address}")
        }
    Row(
        modifier = Modifier
            .padding(top = padding.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = {
            launcher.launch(intent)
        }, modifier = Modifier.width(200.dp)) {

            Column(
                Modifier
                    .weight(1F)
                    .wrapContentSize(Alignment.CenterStart)
            ) {
                leadingIcon()
            }
            Column(
                Modifier
                    .weight(4F)
            ) {
                var buttonText = viewModel.locationState.value
                Text("$buttonText", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

        }

    }

}

@Composable
private fun EditableFieldTransactionDetails(
    padding: Int,
    field: String,
    labelText: String,
    value: Any?,
    viewModel: TransactionDetailsViewModel,
    enabled: Boolean = true,
    leadingIcon: @Composable () -> Unit
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
            text = "0"
        }
        val textState = remember { mutableStateOf(TextFieldValue(text)) }
        var textForUpdate = textState.value.text

        TextField(
            value = textState.value,
            enabled = enabled,
            onValueChange = {
                textState.value = it
                if (textState.value.text.equals("") && field.equals("amount")) {
                    textForUpdate = "0"
                } else {
                    textForUpdate = textState.value.text
                }
                viewModel.updateTemporaryFieldValueBeforeSavingToDB(field, textForUpdate)
            },
            leadingIcon = {
                leadingIcon()
            },
            textStyle = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            ),
            label = {
                Text(
                    labelText, color = Color.White,
                    fontSize = 15.sp
                )
            },
            keyboardOptions = keyboardOptions
        )
    }
}






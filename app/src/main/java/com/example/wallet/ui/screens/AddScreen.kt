package com.example.wallet.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.R
import com.example.wallet.helpers.EmojiProvider
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import com.example.wallet.model.viewmodel.transactions.AddViewModel
import com.example.wallet.model.viewmodel.transactions.AddViewModelFactory
import com.example.wallet.model.viewmodel.transactions.ExpansesViewModel
import com.example.wallet.ui.MainActivity
import com.example.wallet.ui.theme.PurpleBasic
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.runBlocking
import java.util.*

@Composable
fun AddScreen(
    navController: NavHostController,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {
    val viewModel: AddViewModel =
        viewModel(factory = AddViewModelFactory(DataStorePreferenceRepository(LocalContext.current)))
    val listOfButtons = listOf<String>("Transaction", "Category", "Wallet")
    var dataLoaded = viewModel.dataLoaded.value
    if (dataLoaded === false) {
        return
    }
    Column(
        modifier = Modifier
            .background(Color(0xFFBB87E4))
    ) {
        var showIncorrectDataAlertDialog = viewModel.showIncorrectDataAlertDialog.value
        if (showIncorrectDataAlertDialog) {
            OneButtonAlertDialogComponent(
                onDismiss = { viewModel.incorrectDataDialogClose() },
                bodyText = { Text(viewModel.incorrectDataAlertDialogText, color = Color.White) },
                buttonText = "DISMISS"
            )
        }
        LogoSection(pictureSize = 90)
        ChooseWhatToAddSection(listOfButtons, viewModel)
        AddingSection(viewModel, navController)
    }
}

@Composable
fun AddingSection(viewModel: AddViewModel, navController: NavHostController) {
    when (viewModel.whatToAddstate.value) {
        "" -> TransactionAddSection(viewModel, navController)
        "transaction" -> TransactionAddSection(viewModel, navController)
        "category" -> CategoryAddSection(viewModel, navController, leadingIconName = {
            Image(
                painter = painterResource(id = R.drawable.notes),
                contentDescription = "wallet",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        },
            leadingIconType = {
                Image(
                    painter = painterResource(id = R.drawable.category),
                    contentDescription = "wallet",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            })
        "wallet" -> WalletAddSection(viewModel, navController, leadingIconName = {
            Image(
                painter = painterResource(id = R.drawable.notes),
                contentDescription = "wallet",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        },

            leadingIconType = {
                Image(
                    painter = painterResource(id = R.drawable.category),
                    contentDescription = "wallet",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalletAddSection(
    viewModel: AddViewModel,
    navController: NavHostController,
    leadingIconName: @Composable () -> Unit,
    leadingIconType: @Composable () -> Unit
) {
    var emoji = viewModel.iconWalletFieldTemporaryValueBeforeSavingtoDB
    var name = viewModel.nameWalletFieldTemporaryValueBeforeSavingtoDB
    var currency = viewModel.currencyWalletFieldTemporaryValueBeforeSavingtoDB

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.background(
                color = PurpleBasic
            )
        ) {
            TypeOfElementToAddOrEditText("Add Wallet")
            Spacer(modifier = Modifier.padding(bottom = 50.dp))
            var emojis = EmojiProvider.emojis
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(35.dp))

                Spacer(modifier = Modifier.height(30.dp))
                var text1 = ""
                if (text1 === null) {
                    text1 = ""
                }
                val textState1 = remember { mutableStateOf(TextFieldValue(text1)) }

                TextField(
                    value = textState1.value,
                    singleLine = true,
                    leadingIcon = { leadingIconName() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = {
                        textState1.value = it
                        viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                            "nameWallet",
                            textState1.value.text
                        )
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),

                    label = {
                        Text(
                            "Name", color = Color.White,
                            fontSize = 15.sp
                        )
                    },
                    maxLines = 1,

                    )

                var text2 = ""
                if (text2 === null) {
                    text2 = ""
                }
                val textState2 = remember { mutableStateOf(TextFieldValue(text1)) }
                TextField(
                    value = textState2.value,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    leadingIcon = { leadingIconType() },
                    onValueChange = {
                        textState2.value = it
                        viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                            "currencyWallet",
                            textState2.value.text
                        )
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                    label = {
                        Text(
                            "Type", color = Color.White,
                            fontSize = 15.sp
                        )
                    },
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(30.dp))
                LazyVerticalGrid(
                    cells = GridCells.Fixed(4),
                    modifier = Modifier
                        .height(220.dp)
                        .padding(bottom = 20.dp)
                ) {
                    items(emojis) { emoji ->
                        Text(
                            emoji,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.clickable(onClick = {
                                viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                                    "iconWallet",
                                    emoji
                                )
                            })
                        )
                    }
                }
                SaveButtonWalletAdd(viewModel = viewModel, navController)
            }
        }

    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryAddSection(
    viewModel: AddViewModel,
    navController: NavHostController,
    leadingIconName: @Composable () -> Unit,
    leadingIconType: @Composable () -> Unit
) {
    var emoji = viewModel.iconCategoryFieldTemporaryValueBeforeSavingtoDB
    var name = viewModel.nameCategoryFieldTemporaryValueBeforeSavingtoDB
    var type = viewModel.typeCategoryFieldTemporaryValueBeforeSavingtoDB

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.background(
                color = PurpleBasic
            )
        ) {
            TypeOfElementToAddOrEditText("Add Category")
            Spacer(modifier = Modifier.padding(bottom = 50.dp))
            var emojis = EmojiProvider.emojis
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(35.dp))
//                Box(Modifier.fillMaxWidth()) {
//                    Canvas(modifier = Modifier
//                        .size(50.dp)
//                        .align(Alignment.Center), onDraw = {
//                        drawCircle(color = Color.White)
//                    })
//                    if (emoji != null) {
//                        Text(
//                            text = emoji,
//                            fontSize = 60.sp,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//                    }
//                }
                Spacer(modifier = Modifier.height(30.dp))
                var text1 = ""
                if (text1 === null) {
                    text1 = ""
                }
                val textState1 = remember { mutableStateOf(TextFieldValue(text1)) }

                TextField(
                    value = textState1.value,
                    singleLine = true,
                    leadingIcon = { leadingIconName() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = {
                        textState1.value = it
                        viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                            "nameCategory",
                            textState1.value.text
                        )
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    ),
                    label = {
                        Text(
                            "Name", color = Color.White,
                            fontSize = 15.sp
                        )
                    },
                    maxLines = 1,
                )

                var text2 = ""
                if (text2 === null) {
                    text2 = ""
                }
                val textState2 = remember { mutableStateOf(TextFieldValue(text1)) }
                TextField(
                    value = textState2.value,
                    singleLine = true,
                    leadingIcon = { leadingIconType() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    onValueChange = {
                        textState2.value = it
                        viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                            "typeCategory",
                            textState2.value.text
                        )
                    },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    ),
                    label = {
                        Text(
                            "Type", color = Color.White,
                            fontSize = 15.sp
                        )
                    },
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(30.dp))
                LazyVerticalGrid(
                    cells = GridCells.Fixed(4),
                    modifier = Modifier
                        .height(220.dp)
                        .padding(bottom = 20.dp)
                ) {
                    items(emojis) { emoji ->
                        Text(
                            emoji,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.clickable(onClick = {
                                viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                                    "iconCategory",
                                    emoji
                                )
                            })
                        )
                    }
                }
                SaveButtonCategoryAdd(viewModel = viewModel, navController)
            }
        }

    }

}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransactionAddSection(viewModel: AddViewModel, navController: NavHostController) {

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

    val location = viewModel.locationState
    TypeOfElementToAddOrEditText("Add Transaction")
    Column(
        Modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(bottom = 50.dp))
        EditableFieldTransactionAdd(
            padding = 0,
            field = nameFieldName,
            labelText = "Name",
            value = "",
            viewModel = viewModel,
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.notes),
                    contentDescription = "email",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        )
        EditableFieldTransactionAdd(
            padding = 20,
            field = amountFieldName,
            labelText = "Amount",
            value = "",
            viewModel = viewModel,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.money),
                    contentDescription = "wallet",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        )


        TypeSelectorTransactionAdd(
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


        CategorySelectorTransactionAdd(
            padding = 20,
            labelText = viewModel.categoryNameFieldTemporaryValueBeforeSavingtoDB.value,
            optionsList = viewModel.transactionCetegoriesState.value,
            viewModel = viewModel,
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

        WalletSelectorTransactionAdd(
            padding = 20,
            labelText = viewModel.walletNameFieldTemporaryValueBeforeSavingtoDB.value,
            optionsList = viewModel.transactionWalletsState.value,
            viewModel = viewModel,
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.wallet),
                    contentDescription = "wallet",
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
            }
        )

        DatePicker(viewModel = viewModel, padding = 20, leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "wallet",
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
            )
        })

        EditableFieldTransactionAdd(
            padding = 20,
            field = commentsFieldName,
            labelText = "Comments",
            value = "",
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

        EditableFieldLocation(
            padding = 20,
            field = locationFieldName,
            labelText = "Location",
            value = "",
            viewModel = viewModel,
            location = location.value,
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
        SaveButtonTransactionAdd(
            fieldsOnTheScreen = fieldsOnTheScreen,
            viewModel = viewModel,
            navController = navController
        )

    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DatePicker(viewModel: AddViewModel, padding: Int, leadingIcon: @Composable () -> Unit) {
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
                Text(
                    text = viewModel.datePicked.value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

    }
    Row(horizontalArrangement = Arrangement.Center) {
        AnimatedVisibility(visible = viewModel.expandedCalendar.value) {
            AddCalendarDatePicker(viewModel)
        }

    }
}

@Composable
private fun AddCalendarDatePicker(viewModel: AddViewModel) {
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
fun TypeOfElementToAddOrEditText(text: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = text, style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.White
            )
        )
    }
}

@Composable
private fun SaveButtonTransactionAdd(
    fieldsOnTheScreen: ArrayList<String>,
    viewModel: AddViewModel,
    navController: NavHostController
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(modifier = Modifier.padding(bottom = 20.dp), onClick = {
            runBlocking {
                val addResult = viewModel.addTransactionToDb()
                Thread.sleep(1000)
                if (addResult) {
                    navController.navigate("expanses")
                }
            }
        }) {
            Text(text = "Add transaction")
        }
    }
}

@Composable
private fun SaveButtonCategoryAdd(
    viewModel: AddViewModel, navController: NavHostController
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(onClick = {
            runBlocking {
                viewModel.addCategoryToDb()
                navController.navigate("expanses")
            }
        }) {
            Text(text = "Add category")
        }
    }
}

@Composable
private fun SaveButtonWalletAdd(
    viewModel: AddViewModel,
    navController: NavHostController
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(onClick = {
            runBlocking {
                viewModel.addWalletToDb()
                navController.navigate("expanses")
            }
        }) {
            Text(text = "Add wallet")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun WalletSelectorTransactionAdd(
    padding: Int,
    labelText: String?,
    optionsList: List<SecondAllWalletsResponseItem>,
    enabled: Boolean = true,
    viewModel: AddViewModel,
    leadingIcon: @Composable () -> Unit
) {
    Column {
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

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun CategorySelectorTransactionAdd(
    padding: Int,
    labelText: String?,
    optionsList: List<SecondAllExpenseCategoriesResponseItem>,
    enabled: Boolean = true,
    viewModel: AddViewModel,
    leadingIcon: @Composable () -> Unit
) {
    Column {
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
fun TypeSelectorTransactionAdd(
    padding: Int,
    labelText: String?,
    optionsList: List<String>,
    enabled: Boolean = true,
    viewModel: AddViewModel,
    leadingIcon: @Composable () -> Unit
) {

    Column {
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
                            text = "Type: $labelText",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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
                            if (option.equals("Expense")) {
                                CategoryImage("\uD83D\uDED1", 30)

                            } else {
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

@Composable
fun EditableFieldLocation(
    padding: Int,
    field: String,
    labelText: String,
    value: Any?,
    viewModel: AddViewModel,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    location: String,
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
                Text(
                    text = "${viewModel.locationFieldTemporaryValueBeforeSavingtoDB.value}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    leadingIcon: @Composable () -> Unit
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
            leadingIcon = {
                leadingIcon()
            },
            label = {
                Text(
                    labelText, color = Color.White,
                    fontSize = 15.sp
                )
            },
            textStyle = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            ),
            keyboardOptions = keyboardOptions,
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

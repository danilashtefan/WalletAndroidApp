package com.example.wallet.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.R
import com.example.wallet.helpers.EmojiProvider
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpenseCategoriesResponseItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllWalletsResponseItem
import com.example.wallet.model.viewmodel.transactions.TransactionCategoriesDetailsViewModel
import com.example.wallet.model.viewmodel.transactions.TransactionCategoriesDetailsViewModelFactory
import com.example.wallet.model.viewmodel.transactions.WalletsDetailsViewModel
import com.example.wallet.model.viewmodel.transactions.WalletsDetailsViewModelFactory
import com.example.wallet.ui.theme.PurpleBasic

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalletsDetailsScreen(
    navController: NavHostController,
    walletId: Int,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {
    val viewModel: WalletsDetailsViewModel = viewModel(
        factory = WalletsDetailsViewModelFactory(
            DataStorePreferenceRepository(
                LocalContext.current
            )
        )
    ) //ViewModel is bound to a composable
    viewModel.setWalletId(walletId)
    var dataLoaded = viewModel.dataLoaded.value
    var wallet = viewModel.wallet.value

    if (dataLoaded === false) {
        return
    }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        LogoTransactionDetailsSection()
        WalletsDetailsImageSection(wallet)
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.background(
                    color = PurpleBasic
                )
            ) {

                TypeOfElementToAddOrEditText("Edit wallet")
                Spacer(modifier = Modifier.padding(bottom = 50.dp))
                var emojis = EmojiProvider.emojis
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(35.dp))
                    Spacer(modifier = Modifier.height(30.dp))
                    var text1 = wallet.walletName
                    if (text1 === null) {
                        text1 = ""
                    }
                    val textState1 = remember { mutableStateOf(TextFieldValue(text1)) }

                    TextField(
                        value = textState1.value,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.notes),
                                contentDescription = "wallet",
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                            )
                        },
                        onValueChange = {
                            textState1.value = it
                            viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                                "name",
                                textState1.value.text
                            )
                        },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        ),
                        label = { Text("Name",color = Color.White,
                            fontSize = 15.sp)},
                        maxLines = 1,
                    )

                    var text2 = wallet.currency
                    if (text2 === null) {
                        text2 = ""
                    }
                    val textState2 = remember { mutableStateOf(TextFieldValue(text2)) }
                    TextField(
                        value = textState2.value,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        leadingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.category),
                                contentDescription = "wallet",
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                            )
                        },
                        onValueChange = {
                            textState2.value = it
                            viewModel.updateTemporaryFieldValueBeforeSavingToDB(
                                "type",
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
                                        "icon",
                                        emoji
                                    )
                                })
                            )
                        }
                    }
                    SaveButtonWalletEdit(viewModel = viewModel, navController = navController)
                }


            }
        }
    }
}

@Composable
private fun SaveButtonWalletEdit(
    viewModel: WalletsDetailsViewModel,
    navController: NavHostController

) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        OutlinedButton(onClick = {
            viewModel.editWalletInDb()
            Thread.sleep(500)
            navController.navigate("expanses")
        }) {
            Text(text = "Edit wallet")
        }
    }
}

@Composable
fun WalletsDetailsImageSection(wallet: SecondAllWalletsResponseItem) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Card(
            shape = CircleShape,
            border = BorderStroke(width = 2.dp, color = Color.White),
            modifier = Modifier.size(70.dp),
            elevation = 5.dp
        ) {
            CategoryImage(wallet.icon, 50)
        }
    }
}
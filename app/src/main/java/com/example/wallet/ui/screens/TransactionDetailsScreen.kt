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
import com.example.wallet.model.viewmodel.transactions.TransactionDetailsViewModel

@Composable
fun TransactionDetailsScreen(transactionId: Int) {
   val viewModel: TransactionDetailsViewModel = viewModel() //ViewModel is bound to a composable
   viewModel.setTransactionId(transactionId)
   var dataLoaded = viewModel.dataLoaded.value
   var transaction = viewModel.expense.value
   val transactionsCategories = viewModel.transactionCetegoriesStateNames.value

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
      EditableField(padding = 100, field = "amount",labelText = "Amount",value=transaction.amount.toString(), viewModel = viewModel )
      EditableField(padding = 20, field = "type",labelText = "Type", value= transaction.type, viewModel = viewModel)
      //InfoRow(20,"Category")
      InfoSelctor(padding = 20,labelText = transaction.categoryName, transactionsCategories, viewModel = viewModel, transactionId = transaction.id)
      EditableField(20,field = "date",labelText = "Date",transaction.date, viewModel = viewModel)
      EditableField(20,field = "comments",labelText = "Comments", transaction.comments, viewModel = viewModel)
      EditableField(20,field = "location",labelText = "Location", transaction.location, viewModel = viewModel)

   }
   }

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
private fun InfoSelctor(padding:Int, labelText: String, optionsList: List<String>,  enabled: Boolean = true, viewModel: TransactionDetailsViewModel, transactionId: Int) {
   Column() {
      var expanded by remember { mutableStateOf(false) }
      Row(
         modifier = Modifier
            .padding(top = padding.dp)
            .fillMaxWidth(),
         verticalAlignment = Alignment.CenterVertically,
         horizontalArrangement = Arrangement.Center
      ){

         OutlinedButton(onClick = {expanded = !expanded}, enabled = enabled) {
            Text(text = labelText)
         }
      }
      AnimatedVisibility(visible = expanded ) {
      Row(modifier = Modifier.horizontalScroll(rememberScrollState())){
         for (option in optionsList) {
            Card(onClick = {
               viewModel.chooseCategory(option)}, modifier = Modifier.padding(7.dp)) {
               Text(option)
            }
         }

      }
      }
   }

}


@Composable
private fun EditableField(padding:Int, field:String, labelText: String, value: String?, viewModel: TransactionDetailsViewModel, enabled: Boolean = true) {

   Row(
      modifier = Modifier
         .padding(top = padding.dp)
         .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
   ) {
      var text = value
      if (text === null) {
         text = ""
      }
      val textState = remember { mutableStateOf(TextFieldValue(text)) }
      var keyboardOptions =  KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)

      if(field =="amount"){
         keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
      }

      TextField(
         value = textState.value,
         enabled=enabled,
         onValueChange = {
            textState.value = it
               viewModel.updateField(field, textState.value.text)

         },
         label = { Text(labelText) },
         keyboardOptions = keyboardOptions
      )
   }
}



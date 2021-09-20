package com.example.wallet.ui.screens

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wallet.R
import com.example.wallet.model.Expanse
import com.example.wallet.model.classesFromResponse.Transaction
import com.example.wallet.model.viewmodel.transactions.ExpansesViewModel
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
      InfoTextField(padding = 100, labelText = "Amount",value=transaction.amount.toString() )
      InfoTextField(padding = 20, labelText = "Type", value= transaction.type)
      //InfoRow(20,"Category")
      InfoSelctor(padding = 20, labelText = transaction.categoryName, transactionsCategories, viewModel = viewModel, transactionId = transaction.id)
      InfoTextField(20,labelText = "Date",transaction.date.toString())
      InfoTextField(20,labelText = "Comments", transaction.comments.toString())
      InfoTextField(20,labelText = "Location", transaction.location.toString())

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
private fun InfoTextField(padding:Int, labelText: String, value: String ,enabled: Boolean = true) {
   if (labelText === null || value === null) {
      return;
   }

   Row(
      modifier = Modifier
         .padding(top = padding.dp)
         .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
   ) {
      //var text by rememberSaveable { mutableStateOf("") }
      var text = value
      TextField(
         value = text,
         enabled=enabled,
         onValueChange = {
            text = it
         },
         label = { Text(labelText) }
      )
   }
}



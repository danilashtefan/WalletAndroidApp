package com.example.wallet.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.R
import com.example.wallet.model.viewmodel.ExpanseCategoriesViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun AddTransactionScreen() {
   Column() {
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
      InfoRow(100,"Category")
      InfoRow(20,"Date")
      InfoRow(20,"Notes")
      InfoRow(20,"Location")

   }
   }

@Composable
private fun InfoRow(padding:Int, labelText:String) {
   Row(
      modifier = Modifier
         .padding(top = padding.dp)
         .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
   ) {
      var text by rememberSaveable { mutableStateOf("") }
      TextField(
         value = text,
         onValueChange = {
            text = it
         },
         label = { Text(labelText) }
      )
   }
}



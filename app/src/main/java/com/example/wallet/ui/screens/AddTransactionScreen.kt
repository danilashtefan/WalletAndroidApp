package com.example.wallet.ui.screens

import android.widget.CalendarView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.example.wallet.R

@Composable
fun AddTransactionScreen(transactionId:Int) {
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
      var categoryLabelString : String by remember { mutableStateOf("Category")}
      InfoTextField(padding = 100, labelText = "Amount" )
      InfoTextField(padding = 20, labelText = "Type" )
      //InfoRow(20,"Category")
      InfoSelctor(padding = 20, labelText = categoryLabelString)
      InfoTextField(20,"Date")
      InfoTextField(20,"Notes")
      InfoTextField(20,"Location")

   }
   }

@OptIn(ExperimentalAnimationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
private fun InfoSelctor(padding:Int, labelText:String, enabled: Boolean = true, ) {
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
      Row(){
         Card(onClick ={} ) {
            var value = "Shopping"
            Text(value)
         }

      }
      }
   }

}




@Composable
private fun InfoTextField(padding:Int, labelText:String, enabled: Boolean = true) {
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
         enabled=enabled,
         onValueChange = {
            text = it
         },
         label = { Text(labelText) }
      )
   }
}



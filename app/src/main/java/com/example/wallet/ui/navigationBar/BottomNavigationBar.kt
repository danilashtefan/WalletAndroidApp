package com.example.wallet.ui.navigationBar

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wallet.ui.theme.Blue1000
import com.example.wallet.ui.theme.Purple1000

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavigationItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(modifier = modifier, backgroundColor = Blue1000, elevation = 5.dp) {

        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(selected = selected, onClick = { onItemClick(item) }, icon = {
              Column(horizontalAlignment = CenterHorizontally) {
                  Icon(imageVector = item.icon, contentDescription = item.name)
                  if(selected){
                      Text(text = item.name, textAlign = TextAlign.Center, fontSize = 10.sp)
                  }
                  
              }

            }, selectedContentColor = Color.White, unselectedContentColor = Purple1000)
        }
    }


}
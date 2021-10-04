package com.example.wallet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Money
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.wallet.model.classesFromResponse.Transaction
import com.example.wallet.ui.navigationBar.BottomNavigationBar
import com.example.wallet.ui.navigationBar.BottomNavigationItem
import com.example.wallet.ui.screens.ExpanseCategoriesScreen
import com.example.wallet.ui.screens.TransactionDetailsScreen
import com.example.wallet.ui.screens.ExpansesScreen
import com.example.wallet.ui.theme.WalletTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletTheme {
                val navController = rememberNavController()
                    Scaffold(
                        bottomBar = { BottomNavigationBar(
                            items = listOf(
                                BottomNavigationItem("Expenses",
                                route = "expanses",
                                icon = Icons.Default.Money),
                                BottomNavigationItem("Categories",
                                    route = "categories",
                                    icon = Icons.Default.Book)
                            ),
                            navController = navController ,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )}
                    ) {innerPadding->innerPadding.calculateBottomPadding()
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFBB87E4))
                            .padding(innerPadding)) {
                            UsersApplication(navController)
                        }
                    }

            }

        }
    }
}

@Composable
fun UsersApplication(navController: NavHostController){
    NavHost(navController = navController, startDestination ="expanses"){
        composable("expanses"){
            ExpansesScreen(navController)
        }
        
        composable("categories"){
            ExpanseCategoriesScreen(navHostController = navController)
        }

        composable("transactionDetails/{transactionId}",
        arguments = listOf(navArgument("transactionId"){
            type = NavType.IntType
        })){
            navBackStackEntry->
            navBackStackEntry.arguments?.let {
                TransactionDetailsScreen(transactionId = it.getInt("transactionId"))
            }
        }
    }
}



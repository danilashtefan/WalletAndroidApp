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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Money
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.wallet.BuildConfig
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.ui.navigationBar.BottomNavigationBar
import com.example.wallet.ui.navigationBar.BottomNavigationItem
import com.example.wallet.ui.screens.*
import com.example.wallet.ui.theme.WalletTheme
import com.google.android.libraries.places.api.Places
import java.io.File
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(applicationContext,"AIzaSyBU6eLqByvGm88Ez8UxQR6NNVzfis4rudI")
        setContent {
            WalletTheme {
                val navController = rememberNavController()
                    Scaffold(
                        bottomBar = { BottomNavigationBar(
                            items = listOf(
                                BottomNavigationItem("Expenses",
                                route = "expanses",
                                icon = Icons.Default.Money),
                                BottomNavigationItem("Add",
                                    route = "add",
                                    icon = Icons.Default.Add
                                ),
                                BottomNavigationItem("Categories",
                                    route = "categories",
                                    icon = Icons.Default.Book)

                            ),
                            navController = navController ,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )}
                    ) {innerPadding->innerPadding.calculateTopPadding()
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
    NavHost(navController = navController, startDestination ="login"){

        composable("login"){
            LoginScreen(navController, DataStorePreferenceRepository(LocalContext.current))
        }

        composable("expanses"){
            ExpansesScreen(navController, DataStorePreferenceRepository(LocalContext.current))
        }
        
        composable("categories"){
            ExpanseCategoriesScreen(navHostController = navController, DataStorePreferenceRepository(LocalContext.current))
        }

        composable("add"){
          AddScreen(navController = navController, DataStorePreferenceRepository(LocalContext.current))
        }

        composable("categoryStatistics/{categoryId}",
        arguments = listOf(navArgument("categoryId"){
            type = NavType.IntType
        })){
            navBackStackEntry->
        navBackStackEntry.arguments?.let {
            CategoryStatisticsScreen(navController,categoryId = it.getInt("categoryId"), DataStorePreferenceRepository(LocalContext.current))
        }
    }

        composable("walletStatistics/{walletId}",
            arguments = listOf(navArgument("walletId"){
                type = NavType.IntType
            })){
                navBackStackEntry->
            navBackStackEntry.arguments?.let {
                WalletStatisticsScreen(navController,walletId = it.getInt("walletId"), DataStorePreferenceRepository(LocalContext.current))
            }
        }

        composable("transactionDetails/{transactionId}",
        arguments = listOf(navArgument("transactionId"){
            type = NavType.IntType
        })){
            navBackStackEntry->
            navBackStackEntry.arguments?.let {
                TransactionDetailsScreen(navController,transactionId = it.getInt("transactionId"), DataStorePreferenceRepository(LocalContext.current))
            }
        }

        composable("categoriesDetails/{categoryId}",
            arguments = listOf(navArgument("categoryId"){
                type = NavType.IntType
            })){
                navBackStackEntry->
            navBackStackEntry.arguments?.let {
                TransactionCategoriesDetailsScreen(navController,categoryId = it.getInt("categoryId"), DataStorePreferenceRepository(LocalContext.current))
            }
        }
        composable("walletsDetails/{walletId}",
            arguments = listOf(navArgument("walletId"){
                type = NavType.IntType
            })){
                navBackStackEntry->
            navBackStackEntry.arguments?.let {
                WalletsDetailsScreen(navController,walletId = it.getInt("walletId"), DataStorePreferenceRepository(LocalContext.current))
            }
        }

    }
}



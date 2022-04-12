package com.example.wallet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
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
        Places.initialize(applicationContext, "AIzaSyBU6eLqByvGm88Ez8UxQR6NNVzfis4rudI")
        setContent {
            WalletTheme {
                val navController = rememberNavController()
                val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                when (navBackStackEntry?.destination?.route) {
                    "login" -> {
                        // Show BottomBar
                        bottomBarState.value = false
                    }
                    else -> bottomBarState.value = true

                }
                Scaffold(
                    bottomBar = {
                        BottomBar(navController = navController, bottomBarState = bottomBarState)
                    }
                ) { innerPadding ->
                    innerPadding.calculateTopPadding()
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFBB87E4))
                            .padding(innerPadding)
                    ) {
                        UsersApplication(navController = navController)
                    }
                }

            }

        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBar(navController: NavHostController, bottomBarState: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = bottomBarState.value,
        content = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavigationItem(
                        "Transactions",
                        route = "expanses",
                        icon = Icons.Default.Money
                    ),
                    BottomNavigationItem(
                        "Add",
                        route = "add",
                        icon = Icons.Default.Add
                    ),
                    BottomNavigationItem(
                        "Statistics",
                        route = "statistics",
                        icon = Icons.Default.Book
                    ),
                    BottomNavigationItem(
                        "Report",
                        route = "report",
                        icon = Icons.Filled.PieChart
                    )
                ),

                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        })
}

@Composable
fun UsersApplication(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController, DataStorePreferenceRepository(LocalContext.current))
        }

        composable("expanses") {
            ExpansesScreen(navController, DataStorePreferenceRepository(LocalContext.current))
        }

        composable("statistics") {
            ExpanseCategoriesScreen(
                navHostController = navController,
                DataStorePreferenceRepository(LocalContext.current)
            )
        }

        composable("report") {
            ReportScreen(navController = navController)
        }


        composable("add") {
            AddScreen(
                navController = navController,
                DataStorePreferenceRepository(LocalContext.current)
            )
        }

        composable(
            "categoryStatistics/{categoryId}/{categoryName}/{categoryIcon}",
            arguments = listOf(navArgument("categoryId") {
                type = NavType.IntType
            },
                navArgument("categoryName") {
                    type = NavType.StringType
                },
                navArgument("categoryIcon") {
                    type = NavType.StringType
                })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let {
                CategoryStatisticsScreen(
                    navController,
                    categoryId = it.getInt("categoryId"),
                    categoryName = it.getString("categoryName")!!,
                    categoryIcon = it.getString("categoryIcon")!!,
                    dataStorePreferenceRepository = DataStorePreferenceRepository(LocalContext.current)
                )
            }
        }

        composable(
            "walletStatistics/{walletId}/{walletName}/{walletIcon}",
            arguments = listOf(navArgument("walletId") {
                type = NavType.IntType
            },
                navArgument("walletName") {
                    type = androidx.navigation.NavType.StringType
                },
                navArgument("walletIcon") {
                    type = NavType.StringType
                })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let {
                WalletStatisticsScreen(
                    navController,
                    walletId = it.getInt("walletId"),
                    walletName = it.getString("walletName")!!,
                    walletIcon = it.getString("walletIcon")!!,
                    DataStorePreferenceRepository(LocalContext.current)
                )
            }
        }

        composable(
            "transactionDetails/{transactionId}",
            arguments = listOf(navArgument("transactionId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let {
                TransactionDetailsScreen(
                    navController,
                    transactionId = it.getInt("transactionId"),
                    DataStorePreferenceRepository(LocalContext.current)
                )
            }
        }

        composable(
            "categoriesDetails/{categoryId}",
            arguments = listOf(navArgument("categoryId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let {
                TransactionCategoriesDetailsScreen(
                    navController,
                    categoryId = it.getInt("categoryId"),
                    DataStorePreferenceRepository(LocalContext.current)
                )
            }
        }
        composable(
            "walletsDetails/{walletId}",
            arguments = listOf(navArgument("walletId") {
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.let {
                WalletsDetailsScreen(
                    navController,
                    walletId = it.getInt("walletId"),
                    DataStorePreferenceRepository(LocalContext.current)
                )
            }
        }

    }
}



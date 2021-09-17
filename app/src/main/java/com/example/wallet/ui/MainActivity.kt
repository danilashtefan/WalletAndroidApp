package com.example.wallet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wallet.ui.screens.AddTransactionScreen
import com.example.wallet.ui.screens.ExpanseCategoriesScreen
import com.example.wallet.ui.screens.ExpansesScreen
import com.example.wallet.ui.theme.WalletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WalletTheme {
                Box(modifier = Modifier.fillMaxSize()
                    .background(Color(0xFFBB87E4))){
                    //AddTransactionScreen()
                    UsersApplication()
                }
            }

        }
    }
}

@Composable
fun UsersApplication(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination ="expanses"){
        composable("expanses"){
            ExpansesScreen(navController)
        }

        composable("expanseCategories"){
            ExpanseCategoriesScreen(navController)
        }
    }
}


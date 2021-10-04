package com.example.wallet.ui.navigationBar

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem (
val name: String,
val route: String,
val icon: ImageVector,
val badgeCount: Int = 0
)
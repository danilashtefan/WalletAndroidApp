package com.example.wallet.model

import android.graphics.Color
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem

data class CategoryWrapperWithColor(
    val transaction: SecondAllExpensesItem,
    val color: androidx.compose.ui.graphics.Color
)
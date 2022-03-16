package com.example.wallet.model


import com.example.wallet.model.response.transactions.SecondAPI.TopExpenseCategoryWithAmountResponse

data class CategoryWrapperWithColor(
    val category: TopExpenseCategoryWithAmountResponse,
    val color: androidx.compose.ui.graphics.Color
)
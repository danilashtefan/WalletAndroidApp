package com.example.wallet.model

import com.example.wallet.model.response.transactions.SecondAPI.TopExpenseCategoryWithAmountResponse
import com.example.wallet.model.response.transactions.SecondAPI.TopWalletWithAmountResponse

data class WalletWrapperWithColor(
    val wallet: TopWalletWithAmountResponse,
    val color: androidx.compose.ui.graphics.Color
)
package com.example.wallet.model.response.transactions.SecondAPI

data class TopWalletWithAmountResponse(
    val expenseAmount: Int = 0,
    val incomeAmount: Int = 0,
    val wallet: Wallet = Wallet()
)
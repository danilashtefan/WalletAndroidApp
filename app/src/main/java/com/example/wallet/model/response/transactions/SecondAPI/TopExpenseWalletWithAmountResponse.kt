package com.example.wallet.model.response.transactions.SecondAPI

data class TopWalletWithAmountResponse(
    val amount: Int = 0,
    val wallet: Wallet = Wallet()
)
package com.example.wallet.model.response

data class SingleTransactionWalletResponse(
    val id: Int,
    val walletName: String,
    val currency: String
)

data class SingleWallet(
    val href: String
)
package com.example.wallet.model.response

data class SingleTransactionWalletResponse(
    val _links: Links,
    val id: Int,
    val walletName: String,
    val currency: String
)

data class SingleWallet(
    val href: String
)
data class SingleWalletSelf(
    val href: String
)

data class SingleWalletLinks(
    val currency: Currency,
    val expanses: Expanses,
    val self: Self,
    val wallet: SingleWallet
)

data class SingleWalletExpanses(
    val href: String
)
data class Currency(
    val href: String
)
package com.example.wallet.model.response

data class SingleExpenseWalletResponse(
    val _links: Links,
    val id: Int,
    val walletName: String
)

data class Wallet(
    val href: String
)
data class SingleWalletSelf(
    val href: String
)

data class SingleWalletLinks(
    val currency: Currency,
    val expanses: Expanses,
    val self: Self,
    val wallet: Wallet
)

data class SingleWalletExpanses(
    val href: String
)
data class Currency(
    val href: String
)
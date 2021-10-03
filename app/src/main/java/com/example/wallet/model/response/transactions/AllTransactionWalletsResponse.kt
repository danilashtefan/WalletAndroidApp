package com.example.wallet.model.response.transactions

data class AllTransactionWalletsResponse(
    val _embedded: Embedded,
    val _links: LinksX,
    val page: Page
)

data class WalletX(
    val href: String
)

data class Wallet(
    val _links: Links? = null,
    val id: Int = 0,
    val walletName: String = ""
)

data class SelfX(
    val href: String
)

data class Self(
    val href: String
)

data class Profile(
    val href: String
)

data class Page(
    val number: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)

data class LinksX(
    val profile: Profile,
    val self: SelfX
)

data class Links(
    val currency: Currency,
    val expanses: Expanses,
    val self: Self,
    val wallet: WalletX
)

data class Expanses(
    val href: String
)

data class Embedded(
    val wallets: List<Wallet>
)

data class Currency(
    val href: String
)
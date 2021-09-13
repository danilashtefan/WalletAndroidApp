package com.example.wallet.model

data class AllExpansesResponse(
    val _embedded: Embedded,
    val _links: LinksX,
    val page: Page
)

data class Category(
    val href: String
)

data class Embedded(
    val expanses: List<Expanse>
)

data class Expanse(
    val _links: Links,
    val amount: Int,
    val comments: Any,
    val date: String,
    val id: Int,
    val location: Any,
    val name: String,
    val photoUrl: String,
    var categoryName: String
)

data class ExpanseX(
    val href: String
)
data class Links(
    val category: Category,
    val expanse: ExpanseX,
    val self: Self,
    val wallet: Wallet
)

data class LinksX(
    val profile: Profile,
    val search: Search,
    val self: SelfX
)

data class Page(
    val number: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)

data class Profile(
    val href: String
)

data class Search(
    val href: String
)

data class Self(
    val href: String
)

data class SelfX(
    val href: String
)

data class Wallet(
    val href: String
)
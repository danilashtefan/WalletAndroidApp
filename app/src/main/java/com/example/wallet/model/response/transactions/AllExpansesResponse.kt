package com.example.wallet.model

data class AllExpansesResponse(
    val _embedded: Embedded = Embedded(expanses = emptyList()),
    val _links: LinksX = LinksX(),
    val page: Page = Page()
)

data class Category(
    val href: String = ""
)

data class Embedded(
    val expanses: List<Expanse>
)

data class Expanse(
    val _links: Links? = null,
    val amount: Int = 0,
    val comments: String = "",
    val date: String = "",
    val id: Int = 0,
    val location: String = "",
    val name: String = "",
    val photoUrl: String = "",
    var categoryName: String = "",
    val type: String = ""
)

data class ExpanseX(
    val href: String = ""
)
data class Links(
    val category: Category,
    val expanse: ExpanseX,
    val self: Self,
    val wallet: Wallet
)

data class LinksX(
    val profile: Profile = Profile(),
    val search: Search = Search(),
    val self: SelfX = SelfX()
)

data class Page(
    val number: Int = 0,
    val size: Int = 0,
    val totalElements: Int = 0,
    val totalPages: Int = 0
)

data class Profile(
    val href: String = ""
)

data class Search(
    val href: String = ""
)

data class Self(
    val href: String = ""
)

data class SelfX(
    val href: String = ""
)

data class Wallet(
    val href: String = ""
)
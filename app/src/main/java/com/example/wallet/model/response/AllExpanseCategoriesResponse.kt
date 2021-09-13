package com.example.wallet.model.response

data class AllExpanseCategoriesResponse(
    val _embedded: Embedded,
    val _links: LinksX,
    val page: Page
)
data class Embedded(
    val expanseCategories: List<ExpanseCategory>
)
data class ExpanseCategory(
    val _links: Links,
    val expanseCategoryName: String,
    val id: Int
)

data class ExpanseCategoryX(
    val href: String
)

data class Expanses(
    val href: String
)

data class Links(
    val expanseCategory: ExpanseCategoryX,
    val expanses: Expanses,
    val self: Self
)

data class LinksX(
    val profile: Profile,
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

data class Self(
    val href: String
)

data class SelfX(
    val href: String
)
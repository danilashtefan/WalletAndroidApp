package com.example.wallet.model.response.transactions.SecondAPI

class SecondAllExpensesResponse : ArrayList<SecondAllExpensesItem>()

data class Category(
    val expanseCategoryName: String ="",
    val icon: String ="",
    val id: Int = 0,
    val type: String = ""
)

data class SecondAllExpensesItem(
    var amount: Int = 0,
    val category: Category = Category(),
    var comments: String = "",
    var date: String ="",
    val id: Int = 0,
    var categoryName: String = "",
    var categoryId: Int = 0,
    var walletName: String = "",
    var walletId: Int = 0,
    var categoryIcon: String = "",
    var location: String = "",
    var name: String= "",
    var photoUrl: String= "",
    var type: String= "",
    var username: String= "",
    var wallet: Wallet = Wallet()
)

data class Wallet(
    val currency: String= "",
    val id: Int = 0,
    val walletName: String= ""
)
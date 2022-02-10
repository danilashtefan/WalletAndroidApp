package com.example.wallet.model.response.transactions.SecondAPI

class SecondAllExpensesResponse : ArrayList<SecondAllExpensesItem>()

data class Category(
    val expanseCategoryName: String,
    val icon: String,
    val id: Int,
    val type: String
)

data class SecondAllExpensesItem(
    val amount: Int,
    val category: Category,
    val comments: Any,
    val date: String,
    val id: Int,
    val location: Any,
    val name: String,
    val photoUrl: String,
    val type: String,
    val username: String,
    val wallet: Wallet
)

data class Wallet(
    val currency: String,
    val id: Int,
    val walletName: String
)
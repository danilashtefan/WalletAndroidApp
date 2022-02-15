package com.example.wallet.model.response.transactions.SecondAPI

class SecondAllExpenseCategoriesResponse : ArrayList<SecondAllExpenseCategoriesResponseItem>()

data class SecondAllExpenseCategoriesResponseItem(
    val expanseCategoryName: String = "",
    val icon: String = "💸",
    val id: Int = 0,
    val type: String = "",
    val username: String =""
)
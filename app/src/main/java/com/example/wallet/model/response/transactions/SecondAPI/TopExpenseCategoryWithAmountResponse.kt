package com.example.wallet.model.response.transactions.SecondAPI

data class TopExpenseCategoryWithAmountResponse(
    val amount: Int = 0,
    val category: Category = Category()
)
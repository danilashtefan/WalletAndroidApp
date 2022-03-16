package com.example.wallet.model.response.transactions.SecondAPI

data class TopExpenseCategoryWithAmountResponse(
    val expenseAmount: Int = 0,
    val incomeAmount: Int = 0,
    val category: Category = Category()
)
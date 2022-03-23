package com.example.wallet.model.response.transactions.SecondAPI

data class TopExpenseCategoryWithAmountResponse(
    var expenseAmount: Int = 0,
    var incomeAmount: Int = 0,
    val category: Category = Category(),
    var expenses: List<SecondAllExpensesItem> = ArrayList<SecondAllExpensesItem>()
)
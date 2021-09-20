package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.Expanse
import java.lang.Exception

object ExpansesRepository {
    private val service: WalletWebService = WalletWebService()
    var expense = AllExpansesResponse()
    init {}

    suspend fun getExpanses(): AllExpansesResponse {
        expense = service.getExpanses()
        return expense
    }

    fun getExpense(expenseId: Int): Expanse {
        for(expense in expense._embedded.expanses) {
            if(expense.id === expenseId) {
                return expense
            }
        }
        throw Exception("No expense found!")
    }

    fun updateExpenseCategory(category: String, transactionId: Int): Expanse {
        for(expense in expense._embedded.expanses) {
            if (expense.id === transactionId) {
                expense.categoryName = category;
                return expense;
            }
        }
        throw Exception("No expense found!");
    }

}
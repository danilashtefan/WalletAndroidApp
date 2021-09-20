package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.Expanse
import java.lang.Exception

object ExpansesRepository {
    private val service: WalletWebService = WalletWebService()
    var expense = AllExpansesResponse()

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
        return updateField("categoryName", category, transactionId)
    }

    fun updateField(field: String, value: String, transactionId: Int): Expanse {
        for(expense in expense._embedded.expanses) {
            if (expense.id === transactionId) {
                when (field) {
                    "amount" -> expense.amount = value.toInt()
                    "comments" -> expense.comments = value
                    "date" -> expense.date = value
                    "location" -> expense.location = value
                    "name" -> expense.name = value
                    "photoUrl" -> expense.photoUrl = value
                    "categoryName" -> expense.categoryName = value
                    "type" -> expense.type = value
                }
                return expense;
            }
        }
        throw Exception("No expense found!");
    }

}
package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.AllExpansesResponse
import com.example.wallet.model.Expanse
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesItem
import com.example.wallet.model.response.transactions.SecondAPI.SecondAllExpensesResponse
import com.example.wallet.requests.AddOrEditTransactionRequest
import java.lang.Exception

object TransactionsRepository {
    private val service: WalletWebService = WalletWebService()
    var expense = AllExpansesResponse()
    var expenseFiltered = SecondAllExpensesResponse()

    suspend fun getFilteredExpanses(authToken: String?): SecondAllExpensesResponse {
        expenseFiltered = service.getFilteredExpanses(authToken)
        return expenseFiltered
    }

    suspend fun getCategoryFilteredExpenses(authToken: String?, categoryId: Int):SecondAllExpensesResponse{
        expenseFiltered = service.getCategoryFilteredExpenses(authToken, categoryId)
        return expenseFiltered
    }

    suspend fun getWalletFilteredExpenses(authToken: String?, walletId: Int):SecondAllExpensesResponse{
        expenseFiltered = service.getWalletFilteredExpenses(authToken, walletId)
        return expenseFiltered
    }

    fun getExpense(expenseId: Int): SecondAllExpensesItem {
        for(expense in expenseFiltered) {
            if(expense.id === expenseId) {
                return expense
            }
        }
        throw Exception("No expense found!")
    }

    fun updateExpenseCategory(category: String, transactionId: Int): SecondAllExpensesItem {
        return updateField("categoryName", category, transactionId)
    }
    fun updateField(field: String, value: String?, transactionId: Int): SecondAllExpensesItem {
        for(expense in expenseFiltered) {
            if (expense.id === transactionId) {
                when (field) {
                    "amount" -> if (value != null) {
                        expense.amount = value.toInt()
                    }
                    "comments" -> expense.comments = value.toString()
                    "date" -> expense.date = value.toString()
                    "location" -> expense.location = value.toString()
                    "name" -> expense.name = value.toString()
                    "photoUrl" -> expense.photoUrl = value.toString()
                    "categoryName" -> expense.categoryName = value.toString()
                    "type" -> expense.type = value.toString()
                    "walletName"->expense.walletName = value.toString()
                    "categoryIcon"->expense.categoryIcon = value.toString()
                }

                //PATCH All the changes to the Database when user wants them to be saved
                return expense;
            }
        }
        throw Exception("No expense found!");
    }


    suspend fun updateTransactionInDb(transactionId: Int, transactionData: AddOrEditTransactionRequest, authToken: String):Expanse{
        return service.updateTransactionInDb(transactionId,transactionData, authToken)
    }

    suspend fun addTransactionToDb(transactionData:AddOrEditTransactionRequest) {
        return service.addTransactionToDb(transactionData)

    }

    suspend fun deleteTransaction(expenseId: Int, authToken: String) {
        return service.deleteTransactionFromDb(expenseId, authToken)
    }


}
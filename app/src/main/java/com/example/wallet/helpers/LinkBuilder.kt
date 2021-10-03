package com.example.wallet.helpers

object LinkBuilder {


    fun buildCategoryLinkForAddingToExpanse(categoryId: Int): String{
        val categoryBaseUrl = "http://localhost:8080/api/category/"
        var categoryUrl = categoryBaseUrl + categoryId.toString()
        return categoryUrl
    }

    fun buildWalletLinkForAddingToExpanse(walletId: Int): String{
        val walletBaseUrl = "http://localhost:8080/api/wallet/"
        var walletUrl = walletBaseUrl + walletId.toString()
        return walletUrl
    }

}
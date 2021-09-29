package com.example.wallet.helpers

object LinkBuilder {


    fun buildCategoryLinkForAddingToExpanse(categoryId: Int): String{
        val categoryBaseUrl = "http://localhost:8080/api/category/"
        var categoryUrl = categoryBaseUrl + categoryId.toString()
        return categoryUrl
    }


}
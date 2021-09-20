package com.example.wallet.model.classesFromResponse

data class Transaction (
    var amount: Int? = 0,
    var comments: String? = "",
    var date: String? = "",
    var location: String? = "",
    var type: String = "",
    var categoryName: String = "",
    var id: Int = 0
)
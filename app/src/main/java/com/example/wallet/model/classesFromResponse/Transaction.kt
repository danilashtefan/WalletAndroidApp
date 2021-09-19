package com.example.wallet.model.classesFromResponse

data class Transaction (
    var amount: Int?,
    var comments: Any?,
    var date: String?,
    var location: Any?,
    var type: String,
    var categoryName: String
)
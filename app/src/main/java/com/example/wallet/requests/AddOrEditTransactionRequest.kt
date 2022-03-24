package com.example.wallet.requests

data class AddOrEditTransactionRequest (
    var name: String? = null,
    var amount: Int? = 0,
    var type:String? = null,
    var date: String? = null,
    var comments: String? = null,
    var location: String? = null,
    var wallet: String? = "1",
    var category: String? = "2",
    var photoUrl: String? = null,
    var username:String?="jack"
)

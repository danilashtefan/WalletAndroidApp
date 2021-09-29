package com.example.wallet.requests

data class EditExpenseRequest (
    var name: String? = null,
    var amount: Int? = 0,
    var type:String? = null,
    var date: String? = null,
    var comments: String? = null,
    var location: String? = null,
    /*TODO: Figure out how to pass repository and wallet. For now these are the defaults values*/
    var wallet: String? = "http://localhost:8080/api/wallet/1",
    var category: String? = "http://localhost:8080/api/category/2",
    var photoUrl: String? = null
)

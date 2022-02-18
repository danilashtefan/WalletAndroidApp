package com.example.wallet.requests

data class AddOrEditWalletRequest(
    var walletName: String? = null,
    var currency: String? = null,
    var icon: String? = null,
    var username:String = "jack"
)
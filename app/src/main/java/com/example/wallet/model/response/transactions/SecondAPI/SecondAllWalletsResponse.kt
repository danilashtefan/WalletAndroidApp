package com.example.wallet.model.response.transactions.SecondAPI

class SecondAllWalletsResponse : ArrayList<SecondAllWalletsResponseItem>()

data class SecondAllWalletsResponseItem(
    val currency: String = "",
    val id: Int = 0,
    val username: String = "",
    val walletName: String = "",
    val icon:String = "\uD83D\uDCB0"
)
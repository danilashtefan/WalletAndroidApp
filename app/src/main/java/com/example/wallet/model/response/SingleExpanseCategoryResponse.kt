package com.example.wallet.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SingleExpanseCategoryResponse(
    val expanseCategoryName: String,
    val id: Int,
    val type: String,
    val icon:String
)


package com.example.wallet.requests

data class AddOrEditCategoryRequest(
    var expanseCategoryName: String? = null,
    var type: String? = null,
    var icon: String? = null,
    var username:String = ""
)

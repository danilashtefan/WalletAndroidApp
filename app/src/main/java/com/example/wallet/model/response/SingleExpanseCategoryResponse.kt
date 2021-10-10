package com.example.wallet.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SingleExpanseCategoryResponse(
    @SerializedName("_links")
    val _links: SingleLinks,
    val name: String,
    val id: Int,
    val type: String,
    val icon:String
)

data class SingleLinks(
    @SerializedName("expanseCategory")
    val expanseCategory: SingleExpanseCategory,

    @SerializedName("expanses")
    val expanses: Expanses,

    @SerializedName("self")
    val self: Self
)

data class SingleExpanseCategory(
    val href: String
)

data class SingleExpanses(
    val href: String
)

data class SingleSelf(
    val href: String
)
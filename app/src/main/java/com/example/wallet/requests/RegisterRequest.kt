package com.example.wallet.requests

data class RegisterRequest(
    val name: String? = null,
    val username: String? = null,
    val password: String? = null
)
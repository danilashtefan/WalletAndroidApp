package com.example.wallet.model.response.login

data class RegisterResponse(
    val id: Int,
    val name: String,
    val password: String,
    val roles: List<Role>,
    val username: String
)

data class Role(
    val id: Int,
    val name: String
)
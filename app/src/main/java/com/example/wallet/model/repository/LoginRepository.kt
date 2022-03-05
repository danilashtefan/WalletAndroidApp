package com.example.wallet.model.repository

import com.example.wallet.api.WalletWebService
import com.example.wallet.model.response.login.LoginResponse
import com.example.wallet.model.response.login.RegisterResponse
import com.example.wallet.requests.LoginRequest
import com.example.wallet.requests.RegisterRequest

class LoginRepository {
    private val service: WalletWebService = WalletWebService()


    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return service.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return service.register(registerRequest)
    }
}
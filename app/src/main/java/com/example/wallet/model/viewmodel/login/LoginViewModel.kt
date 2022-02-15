package com.example.wallet.model.viewmodel.login

import android.content.Context
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.lifecycle.*
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.LoginRepository
import com.example.wallet.requests.LoginRequest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.prefs.Preferences


class LoginViewModel(private val dataStorePreferenceRepository: DataStorePreferenceRepository): ViewModel() {
    private val loginRepository: LoginRepository = LoginRepository()
    lateinit var username:String
    lateinit var password:String
    private val _accessToken = MutableLiveData("")
    val accessToken: LiveData<String> = _accessToken

    private var authResult: Boolean = false
    init {
        Log.d("INFO:", "Login ViewModel initialized")

    }

    fun login(loginRequest: LoginRequest): String {

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while loging in: $exception")
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            var tokens = loginRepository.login(loginRequest)
            dataStorePreferenceRepository.setTokens(tokens.access_token, tokens.refresh_token)
            dataStorePreferenceRepository.setUsername(username)
            Log.d("INFO","Access token is: ${accessToken.value}")
            Log.d("INFO","Username: $username")
            if(!tokens.access_token.equals("")) {
                authResult = true
            }

        }
        //Sleep is to wait for the server's response about the authentication
        Thread.sleep(500)
        if(authResult) {
            return "Success"
        }
        return "Failure"
    }

    fun updateViewModelFieldState(field: String, value: String) {
        when (field) {
            "username" -> username = value
            "password" -> password = value

        }

    }
}

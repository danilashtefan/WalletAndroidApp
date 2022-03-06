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
import com.example.wallet.helpers.Strings
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.repository.LoginRepository
import com.example.wallet.requests.LoginRequest
import com.example.wallet.requests.RegisterRequest
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
    private val _accessToken = MutableLiveData(Strings.SUCCESSFUL_REGISTRATION)
    val accessToken: LiveData<String> = _accessToken

    private var authResult: Boolean = false
    private var registerResult: String = ""
    var showAlertDialog = mutableStateOf(false)
    var dialogText = mutableStateOf("")
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
        Thread.sleep(1000)
        if(authResult) {
            return "Success"
        }
        return "Failure"
    }

    fun register(registerRequest: RegisterRequest){
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("EXCEPTION", "Thread exception while register in: $exception")
            dialogText.value = "Connection error! Please, try again!"
            registerResult = Strings.CONNECTION_ERROR
            showAlertDialog.value = true
        }

        viewModelScope.launch(handler + Dispatchers.IO) {
            var response = loginRepository.register(registerRequest)
            when(response.username){
                Strings.FAILED_REGISTRATION -> registerResult = Strings.FAILED_REGISTRATION
                null -> registerResult = Strings.CONNECTION_ERROR
                else -> registerResult = Strings.SUCCESSFUL_REGISTRATION
            }
        }
        Thread.sleep(1000)
        when(registerResult) {
            Strings.SUCCESSFUL_REGISTRATION -> dialogText.value = "Congratulations! User is registered"
            Strings.FAILED_REGISTRATION -> dialogText.value = "Registration failed! Please, provide another username"
        }
        showAlertDialog.value = true

    }

    fun updateViewModelFieldState(field: String, value: String) {
        when (field) {
            "username" -> username = value
            "password" -> password = value

        }

    }
}

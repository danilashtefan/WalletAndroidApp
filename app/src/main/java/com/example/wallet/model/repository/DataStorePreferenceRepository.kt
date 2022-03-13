package com.example.wallet.model.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.io.IOException


class DataStorePreferenceRepository(private val context: Context) {

    private val accessTokenDefault = "default"
    private val refreshTokenDefault = "default"
    private val usernameDefault = "defaultUsername"
    private val budgetDefault = "10"

    companion object{
        val Context.dataStore by preferencesDataStore("app_preferences")
        val PREF_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val PREF_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val PREF_USERNAME = stringPreferencesKey("username")
        val PREF_BUDGET_SET = stringPreferencesKey("budget_set")

        private var INSTANCE: DataStorePreferenceRepository? = null

        fun getInstance(context: Context): DataStorePreferenceRepository{
            return INSTANCE ?: synchronized(this){
                INSTANCE?.let{
                    return it
                }
                val instance = DataStorePreferenceRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }

    //setValue
    suspend fun setTokens(accessToken: String, refreshToken: String){
        context.dataStore.edit { preferences ->
            preferences[PREF_ACCESS_TOKEN] = accessToken
            preferences[PREF_REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun setBudget(budget: String){
        context.dataStore.edit { preferences ->
            preferences[PREF_BUDGET_SET] = budget
        }

    }

    suspend fun setUsername(username: String){
        context.dataStore.edit { preferences ->
            preferences[PREF_USERNAME] = username
        }

    }

    val getAccessToken: Flow<String> = context.dataStore.data
        .map{preferences -> preferences[PREF_ACCESS_TOKEN] ?: accessTokenDefault
        }
    val getRefreshToken: Flow<String> = context.dataStore.data
        .map{preferences -> preferences[PREF_REFRESH_TOKEN] ?: refreshTokenDefault
        }

    val getUsername: Flow<String> = context.dataStore.data.map {
            preferences -> preferences[PREF_USERNAME] ?: usernameDefault
    }

    val getBudget: Flow<String> = context.dataStore.data.map {
            preferences -> preferences[PREF_BUDGET_SET] ?: budgetDefault
    }
    suspend fun<T> getToken() :
            Flow<String> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val result = preferences[PREF_ACCESS_TOKEN]?: accessTokenDefault
        result
    }
}
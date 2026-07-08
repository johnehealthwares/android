package com.rxsoft.mobile.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.tokenStore by preferencesDataStore(name = "auth_tokens")

class TokenManager(private val context: Context) {

    private val accessTokenKey = stringPreferencesKey(Constants.ACCESS_TOKEN_KEY)
    private val refreshTokenKey = stringPreferencesKey(Constants.REFRESH_TOKEN_KEY)

    val accessToken: Flow<String?> = context.tokenStore.data.map { it[accessTokenKey] }
    val refreshToken: Flow<String?> = context.tokenStore.data.map { it[refreshTokenKey] }

    suspend fun saveTokens(access: String, refresh: String) {
        context.tokenStore.edit { prefs ->
            prefs[accessTokenKey] = access
            prefs[refreshTokenKey] = refresh
        }
    }

    suspend fun clearTokens() {
        context.tokenStore.edit { prefs ->
            prefs.remove(accessTokenKey)
            prefs.remove(refreshTokenKey)
        }
    }
}

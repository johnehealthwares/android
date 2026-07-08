package com.rxsoft.mobile.util

import android.content.Context
import android.content.SharedPreferences
import com.rxsoft.mobile.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ServerUrlManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences
        get() = context.getSharedPreferences("server_config", Context.MODE_PRIVATE)

    companion object {
        private const val KEY = "api_base_url"
    }

    fun getUrl(): String {
        return prefs.getString(KEY, null) ?: BuildConfig.API_BASE_URL
    }

    fun setUrl(url: String) {
        prefs.edit().putString(KEY, url).commit()
    }
}

package com.rxsoft.mobile.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rxsoft.mobile.data.repository.AuthRepository
import com.rxsoft.mobile.util.PosConfigManager
import com.rxsoft.mobile.util.ServerUrlManager
import com.rxsoft.mobile.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val serverUrlManager: ServerUrlManager,
    val posConfigManager: PosConfigManager
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val loginState: StateFlow<UiState<Unit>> = _loginState.asStateFlow()

    private val _serverUrl = MutableStateFlow(serverUrlManager.getUrl())
    val serverUrl: StateFlow<String> = _serverUrl.asStateFlow()

    init {
        viewModelScope.launch {
            Log.d("AuthViewModel", "Checking login state on app start")
            val loggedIn = authRepository.isLoggedIn()
            Log.d("AuthViewModel", "Initial login state: $loggedIn")
            _isLoggedIn.value = loggedIn
        }
    }

    fun updateServerUrl(url: String) {
        _serverUrl.value = url
    }

    fun saveServerUrl() {
        serverUrlManager.setUrl(_serverUrl.value)
    }

    fun login(username: String, password: String) {
        Log.d("AuthViewModel", "Login requested for user: $username")
        viewModelScope.launch {
            _loginState.value = UiState.Loading
            authRepository.login(username, password)
                .onSuccess {
                    Log.d("AuthViewModel", "Login succeeded, setting isLoggedIn=true")
                    posConfigManager.loadConfig()
                    _loginState.value = UiState.Success(Unit)
                }
                .onFailure { e ->
                    Log.e("AuthViewModel", "Login failed: ${e.message}")
                    _loginState.value = UiState.Error(e.message ?: "Login failed")
                }
        }
    }

    fun onLoginSuccess() {
        _isLoggedIn.value = true
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _isLoggedIn.value = false
        }
    }
}

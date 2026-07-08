package com.rxsoft.mobile.ui.settings

import androidx.lifecycle.ViewModel
import com.rxsoft.mobile.util.PosConfigManager
import com.rxsoft.mobile.util.ServerUrlManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val serverUrlManager: ServerUrlManager,
    private val moduleConfig: ModuleConfig,
    val posConfigManager: PosConfigManager
) : ViewModel() {

    private val _serverUrl = MutableStateFlow(serverUrlManager.getUrl())
    val serverUrl: StateFlow<String> = _serverUrl.asStateFlow()

    val activeModules: StateFlow<Set<AppModule>> = moduleConfig.activeModules

    fun updateServerUrl(url: String) {
        _serverUrl.value = url
    }

    fun saveServerUrl(url: String) {
        serverUrlManager.setUrl(url)
        _serverUrl.value = url
    }

    fun toggleModule(module: AppModule) {
        val current = moduleConfig.activeModules.value.toMutableSet<AppModule>()
        if (current.contains(module)) {
            current.remove(module)
        } else {
            current.add(module)
        }
        moduleConfig.setActiveModules(current)
    }
}

package com.topjohnwu.magisk.ui.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.topjohnwu.magisk.core.Config
import com.topjohnwu.magisk.core.di.ServiceLocator
import com.topjohnwu.magisk.core.model.module.GithubRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ModuleRepoViewModel : ViewModel() {

    private val _modules = MutableStateFlow<List<GithubRepo>>(emptyList())
    val modules = _modules.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _hasError = MutableStateFlow(false)
    val hasError = _hasError.asStateFlow()

    private val _installStatus = MutableStateFlow<InstallStatus>(InstallStatus.Idle)
    val installStatus = _installStatus.asStateFlow()

    fun installModule(module: GithubRepo) {
        viewModelScope.launch {
            _installStatus.value = InstallStatus.Installing
            try {
                val release = ServiceLocator.networkService.fetchLatestModuleRelease(module.owner.login, module.name)
                val subject = com.topjohnwu.magisk.core.download.Subject.Module(module.name, release.zipballUrl)
                com.topjohnwu.magisk.core.download.DownloadEngine.start(ServiceLocator.deContext, subject)
                _installStatus.value = InstallStatus.Success
            } catch (e: Exception) {
                _installStatus.value = InstallStatus.Error
            }
        }
    }

    fun fetchModules() {
        viewModelScope.launch {
            _isLoading.value = true
            _hasError.value = false
            try {
                val json = ServiceLocator.networkService.fetchString(Config.moduleRepoUrl)
                val type = object : TypeToken<List<GithubRepo>>() {}.type
                val modules = Gson().fromJson<List<GithubRepo>>(json, type)
                val jobs = modules.map { module ->
                    async {
                        try {
                            val repoDetails = ServiceLocator.networkService.fetchRepoDetails(module.owner.login, module.name)
                            val prop = ServiceLocator.networkService.fetchModuleProp(module.owner.login, module.name, repoDetails.defaultBranch)
                            prop.lines().forEach { line ->
                                val parts = line.split("=")
                                if (parts.size == 2) {
                                    when (parts[0]) {
                                        "version" -> module.version = parts[1]
                                        "versionCode" -> module.versionCode = parts[1].toInt()
                                        "author" -> module.author = parts[1]
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // Ignore errors for individual modules
                        }
                    }
                }
                jobs.awaitAll()
                _modules.value = modules
            } catch (e: Exception) {
                _hasError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}

package com.topjohnwu.magisk.ui.module

sealed class InstallStatus {
    object Idle : InstallStatus()
    object Installing : InstallStatus()
    object Success : InstallStatus()
    object Error : InstallStatus()
}

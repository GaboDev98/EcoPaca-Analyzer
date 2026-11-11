package com.gabodev.ecopacaanalyzer.auth

// Implementaciones por plataforma (androidMain, iosMain, desktopMain)
internal expect suspend fun doLoginFirebase(email: String, password: String): Result<String>
internal expect suspend fun doSignUpFirebase(email: String, password: String): Result<String>
internal expect suspend fun doLogoutFirebase(): Result<Unit>
internal expect suspend fun doResetPasswordFirebase(email: String): Result<Unit>

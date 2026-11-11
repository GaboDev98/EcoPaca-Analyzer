package com.gabodev.iotdevicemonitor.auth

// Implementaciones por plataforma (androidMain, iosMain, desktopMain)
internal expect suspend fun doLoginFirebase(email: String, password: String): Result<String>
internal expect suspend fun doSignUpFirebase(email: String, password: String): Result<String>
internal expect suspend fun doLogoutFirebase(): Result<Unit>
internal expect suspend fun doResetPasswordFirebase(email: String): Result<Unit>

/** Usuario con sesión persistida por Firebase, o null si no hay sesión. */
internal data class SessionUser(val uid: String, val email: String?)
internal expect fun currentSessionUser(): SessionUser?

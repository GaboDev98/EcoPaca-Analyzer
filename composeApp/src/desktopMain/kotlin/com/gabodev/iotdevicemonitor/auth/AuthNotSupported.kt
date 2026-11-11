package com.gabodev.iotdevicemonitor.auth

// TODO: implementar Firebase Auth en esta plataforma (p. ej. con GitLive firebase-auth)
private fun <T> notSupported(): Result<T> =
    Result.failure(UnsupportedOperationException("Autenticación no disponible en esta plataforma"))

internal actual suspend fun doLoginFirebase(email: String, password: String): Result<String> = notSupported()
internal actual suspend fun doSignUpFirebase(email: String, password: String): Result<String> = notSupported()
internal actual suspend fun doLogoutFirebase(): Result<Unit> = notSupported()
internal actual suspend fun doResetPasswordFirebase(email: String): Result<Unit> = notSupported()
internal actual fun currentSessionUser(): SessionUser? = null

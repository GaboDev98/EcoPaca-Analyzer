package com.gabodev.iotdevicemonitor.auth

private val authManager by lazy { FirebaseAuthManager() }

internal actual suspend fun doLoginFirebase(email: String, password: String): Result<String> =
    authManager.login(email, password)

internal actual suspend fun doSignUpFirebase(email: String, password: String): Result<String> =
    authManager.signUp(email, password)

internal actual suspend fun doLogoutFirebase(): Result<Unit> =
    authManager.logout()

internal actual suspend fun doResetPasswordFirebase(email: String): Result<Unit> =
    authManager.resetPassword(email)

internal actual fun currentSessionUser(): SessionUser? =
    authManager.getCurrentUserId()?.let { SessionUser(it, authManager.getCurrentUserEmail()) }

package com.gabodev.iotdevicemonitor.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()

    suspend fun login(email: String, password: String): Result<String> = runAuth("No pudimos iniciar sesión") {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result.user?.uid ?: error("No user ID returned")
    }

    suspend fun signUp(email: String, password: String): Result<String> = runAuth("No pudimos crear tu cuenta") {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: error("No user ID returned")
        user.sendEmailVerification().await()
        user.uid
    }

    suspend fun logout(): Result<Unit> = runAuth("No pudimos cerrar sesión") { auth.signOut() }

    suspend fun resetPassword(email: String): Result<Unit> = runAuth<Unit>("No pudimos enviar el email") {
        auth.sendPasswordResetEmail(email).await()
    }

    /**
     * Play account-deletion requirement: removes the user's devices (and their readings)
     * and then the Firebase Auth account. Firebase only deletes accounts with a recent
     * login, so we always re-authenticate with the password first.
     */
    suspend fun deleteAccount(password: String): Result<Unit> = runAuth<Unit>(
        fallback = "No pudimos eliminar tu cuenta",
        messages = mapOf(
            "ERROR_WRONG_PASSWORD" to "Contraseña incorrecta",
            "ERROR_INVALID_CREDENTIAL" to "Contraseña incorrecta",
        ),
    ) {
        val user = auth.currentUser ?: error("No signed-in user")
        val email = user.email ?: error("Account without email")
        user.reauthenticate(EmailAuthProvider.getCredential(email, password)).await()

        val devicesRef = FirebaseDatabase.getInstance().getReference("devices")
        val owned = devicesRef.orderByChild("owner").equalTo(user.uid).get().await()
        val removals = owned.children.mapNotNull { it.key }.associateWith { null }
        if (removals.isNotEmpty()) devicesRef.updateChildren(removals).await()

        user.delete().await()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
    fun getCurrentUserEmail(): String? = auth.currentUser?.email
    fun isUserEmailVerified(): Boolean = auth.currentUser?.isEmailVerified ?: false
    fun isAuthenticated(): Boolean = auth.currentUser != null

    private suspend fun <T> runAuth(
        fallback: String,
        messages: Map<String, String> = emptyMap(),
        block: suspend () -> T,
    ): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: FirebaseAuthException) {
        Result.failure(Exception(messages[e.errorCode] ?: getFirebaseErrorMessage(e.errorCode)))
    } catch (e: FirebaseNetworkException) {
        Result.failure(Exception("Sin conexión. Revisa tu internet e inténtalo de nuevo."))
    } catch (e: Exception) {
        // Never surface raw SDK messages (often English, technical) to the user.
        Result.failure(Exception("$fallback. Inténtalo de nuevo en unos minutos."))
    }

    // Mapear códigos de error de Firebase a mensajes en español
    private fun getFirebaseErrorMessage(errorCode: String): String = when (errorCode) {
        "ERROR_INVALID_EMAIL" -> "Email inválido"
        "ERROR_WRONG_PASSWORD", "ERROR_INVALID_CREDENTIAL" -> "Email o contraseña incorrectos"
        "ERROR_USER_NOT_FOUND" -> "Usuario no encontrado"
        "ERROR_USER_DISABLED" -> "Usuario deshabilitado"
        "ERROR_EMAIL_ALREADY_IN_USE" -> "Email ya registrado"
        "ERROR_WEAK_PASSWORD" -> "Contraseña muy débil (mín. 6 caracteres)"
        "ERROR_NETWORK_REQUEST_FAILED" -> "Error de conexión. Verifica tu internet"
        "ERROR_TOO_MANY_REQUESTS" -> "Demasiados intentos. Intenta más tarde"
        else -> "Error de autenticación: $errorCode"
    }
}

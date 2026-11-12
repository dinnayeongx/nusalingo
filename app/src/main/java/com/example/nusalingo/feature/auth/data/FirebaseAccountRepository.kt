package com.example.nusalingo.feature.auth.data

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

interface AccountRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun saveOnboarding(
        displayName: String,
        selectedScript: String,
        experience: String,
        learningGoals: List<String>,
    ): Result<Unit>
}

class FirebaseAccountRepository : AccountRepository {
    override suspend fun login(email: String, password: String): Result<Unit> =
        execute {
            firebaseAuth().signInWithEmailAndPassword(email, password).await()
            Unit
        }

    override suspend fun register(email: String, password: String): Result<Unit> =
        execute {
            firebaseAuth().createUserWithEmailAndPassword(email, password).await()
            Unit
        }

    override suspend fun saveOnboarding(
        displayName: String,
        selectedScript: String,
        experience: String,
        learningGoals: List<String>,
    ): Result<Unit> {
        val goals = learningGoals.toSet()
        if (
            displayName.trim().length !in 2..50 ||
            displayName.any(Char::isISOControl) ||
            displayName.none(Char::isLetter) ||
            selectedScript !in scripts ||
            experience !in experiences ||
            goals.isEmpty() ||
            goals.any { it !in allowedGoals }
        ) {
            return Result.failure(IllegalArgumentException("Data onboarding tidak valid."))
        }

        return execute {
            val user = firebaseAuth().currentUser
                ?: throw IllegalStateException("Sesi akun tidak ditemukan. Silakan masuk kembali.")
            val email = user.email
                ?: throw IllegalStateException("Email akun tidak tersedia. Silakan masuk kembali.")
            val userDocument = firebaseFirestore().collection("users").document(user.uid)
            val existingProfile = userDocument.get().await()
            val profile = mapOf(
                "displayName" to displayName.trim(),
                "email" to email,
                "selectedScript" to selectedScript,
                "experience" to experience,
                "learningGoals" to learningGoals.distinct(),
                "onboardingCompleted" to true,
            )
            val fields = if (existingProfile.exists()) {
                profile + ("updatedAt" to FieldValue.serverTimestamp())
            } else {
                profile + ("createdAt" to FieldValue.serverTimestamp())
            }
            userDocument.set(fields, SetOptions.merge()).await()
            Unit
        }
    }

    private suspend fun <T> execute(action: suspend () -> T): Result<T> =
        try {
            Result.success(action())
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseFirestoreException) {
            Result.failure(IllegalStateException(firestoreErrorMessage(e.code.name), e))
        } catch (e: FirebaseException) {
            Result.failure(IllegalStateException(firebaseErrorMessage(e), e))
        } catch (e: IllegalStateException) {
            Result.failure(e)
        }

    private fun firebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance(configuredFirebaseApp())

    private fun firebaseFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance(configuredFirebaseApp())

    private fun configuredFirebaseApp(): FirebaseApp =
        try {
            FirebaseApp.getInstance()
        } catch (e: IllegalStateException) {
            throw IllegalStateException(
                "Firebase belum dikonfigurasi. Tambahkan app/google-services.json dari Firebase Console.",
                e
            )
        }

    private companion object {
        val scripts = setOf("Aksara Jawa", "Aksara Sunda", "Aksara Bali")
        val experiences = setOf(
            "Aku baru pertama kali belajar",
            "Aku tahu beberapa bentuk dasar hurufnya",
            "Aku bisa membaca kata-kata sederhana",
            "Aku bisa membaca dan menulis kalimat dasar",
            "Aku sudah lancar membaca dan menulis",
        )
        val allowedGoals = setOf(
            "Hanya untuk bersenang-senang",
            "Mendukung pelajaran sekolah",
            "Mendukung pekerjaan / karier",
            "Mengenal budaya & leluhur",
            "Mengisi waktu luang",
            "Lainnya",
        )
    }
}

private fun firebaseErrorMessage(error: FirebaseException): String {
    val authErrorCode = when (error) {
        is FirebaseAuthUserCollisionException -> "ERROR_EMAIL_ALREADY_IN_USE"
        is FirebaseAuthWeakPasswordException -> "ERROR_WEAK_PASSWORD"
        is FirebaseAuthInvalidCredentialsException -> "ERROR_INVALID_CREDENTIAL"
        else -> (error as? FirebaseAuthException)?.errorCode
    }
    return firebaseAuthErrorMessage(authErrorCode, error is FirebaseNetworkException)
}

internal fun firebaseAuthErrorMessage(errorCode: String?, isNetworkError: Boolean): String =
    when {
        errorCode == "ERROR_EMAIL_ALREADY_IN_USE" ->
            "Email ini sudah terdaftar. Silakan masuk."
        errorCode == "ERROR_WEAK_PASSWORD" ->
            "Kata sandi belum memenuhi persyaratan."
        errorCode == "ERROR_INVALID_CREDENTIAL" || errorCode == "ERROR_INVALID_EMAIL" ->
            "Email atau kata sandi tidak valid."
        isNetworkError || errorCode == "ERROR_NETWORK_REQUEST_FAILED" ->
            "Koneksi internet bermasalah. Periksa jaringan lalu coba lagi."
        errorCode == "ERROR_OPERATION_NOT_ALLOWED" ->
            "Pendaftaran Email/Password belum aktif. Hubungi pengelola aplikasi."
        errorCode == "ERROR_TOO_MANY_REQUESTS" ->
            "Terlalu banyak percobaan. Coba lagi beberapa saat nanti."
        else -> "Layanan akun tidak tersedia. Coba lagi nanti."
    }

internal fun firestoreErrorMessage(code: String): String = when (code) {
    "PERMISSION_DENIED" ->
        "Firebase menolak penyimpanan. Pastikan firestore.rules sudah dipublikasikan di Firebase Console."
    "UNAVAILABLE" ->
        "Firestore tidak dapat dijangkau. Periksa koneksi internet lalu coba lagi."
    "FAILED_PRECONDITION" ->
        "Cloud Firestore belum siap. Buat atau aktifkan database Cloud Firestore di Firebase Console."
    "UNAUTHENTICATED" ->
        "Sesi akun tidak aktif. Silakan masuk kembali lalu coba lagi."
    else ->
        "Gagal menyimpan pilihan ke Firestore ($code). Coba lagi."
}

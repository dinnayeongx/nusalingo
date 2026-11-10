package com.example.nusalingo.feature.auth.data

import android.util.Patterns
import kotlinx.coroutines.delay

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun sendResetLink(email: String): Result<Unit>
    suspend fun resetPassword(newPassword: String): Result<Unit>
}

class FakeAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }

    override suspend fun sendResetLink(email: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }

    override suspend fun resetPassword(newPassword: String): Result<Unit> {
        delay(1000)
        return Result.success(Unit)
    }
}

object AuthValidator {
    fun email(value: String): String? = when {
        value.isBlank() -> "Email tidak boleh kosong."
        !Patterns.EMAIL_ADDRESS.matcher(value.trim()).matches() -> "Format email tidak valid."
        else -> null
    }

    fun loginPassword(value: String): String? = when {
        value.isBlank() -> "Kata sandi tidak boleh kosong."
        value.length < 8 -> "Minimal 8 karakter."
        else -> null
    }

    fun newPassword(value: String): String? = when {
        value.length < 8 -> "Minimal 8 karakter."
        !value.any { it.isLetter() } -> "Harus mengandung huruf."
        !value.any { it.isDigit() } -> "Harus mengandung angka."
        else -> null
    }

    fun confirmPassword(password: String, confirm: String): String? = when {
        confirm.isEmpty() -> null
        password != confirm -> "Kata sandi tidak sama."
        else -> null
    }
}
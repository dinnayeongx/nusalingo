package com.example.nusalingo.feature.auth.data

data class AccountLoginResult(
    val displayName: String,
    val onboardingCompleted: Boolean,
)

data class OnboardingProgress(
    val step: Int = 0,
    val selectedScript: String? = null,
    val experience: String? = null,
    val learningGoals: Set<String> = emptySet(),
)

interface AccountRepository {
    suspend fun login(email: String, password: String): Result<AccountLoginResult>
    suspend fun register(email: String, password: String, displayName: String): Result<Unit>
    suspend fun loadOnboardingProgress(): Result<OnboardingProgress?>
    suspend fun saveOnboardingProgress(progress: OnboardingProgress): Result<Unit>
    suspend fun saveOnboarding(
        displayName: String,
        selectedScript: String,
        experience: String,
        learningGoals: List<String>,
    ): Result<Unit>
}

interface AuthRepository {
    suspend fun sendResetLink(email: String): Result<Unit>
}

object AuthValidator {
    private val emailPattern = Regex(
        "^[A-Za-z0-9.!#\$%&'*+/=?^_`{|}~-]+@" +
            "(?:[A-Za-z0-9](?:[A-Za-z0-9-]{0,61}[A-Za-z0-9])?\\.)+[A-Za-z]{2,63}$"
    )

    fun email(value: String): String? {
        val normalized = value.trim()
        val localPart = normalized.substringBefore('@')
        return when {
            normalized.isBlank() -> "Email tidak boleh kosong."
            normalized.length > 254 ||
                localPart.length > 64 ||
                localPart.startsWith('.') ||
                localPart.endsWith('.') ||
                ".." in localPart ||
                !emailPattern.matches(normalized) -> "Format email tidak valid."
            else -> null
        }
    }

    fun registrationName(value: String): String? {
        val name = value.trim()
        return when {
            name.isEmpty() -> "Nama tidak boleh kosong."
            name.length !in 2..50 -> "Nama harus terdiri dari 2–50 karakter."
            name.any { !(it.isLetter() || it.isWhitespace() || it == '-' || it == '\'') } ->
                "Nama hanya boleh berisi huruf, spasi, tanda hubung, atau apostrof."
            name.none { it.isLetter() } -> "Nama harus mengandung huruf."
            else -> null
        }
    }

    fun registrationPassword(value: String): String? = when {
        value.length < 8 -> "Gunakan minimal 8 karakter."
        value.length > 128 -> "Kata sandi maksimal 128 karakter."
        value.any { it.isWhitespace() } -> "Kata sandi tidak boleh mengandung spasi."
        !value.any { it.isUpperCase() } -> "Tambahkan minimal satu huruf besar."
        !value.any { it.isLowerCase() } -> "Tambahkan minimal satu huruf kecil."
        !value.any { it.isDigit() } -> "Tambahkan minimal satu angka."
        !value.any { !it.isLetterOrDigit() } -> "Tambahkan minimal satu simbol."
        else -> null
    }

    fun registrationConfirmation(password: String, confirm: String): String? = when {
        confirm.isEmpty() -> "Konfirmasi kata sandi wajib diisi."
        password != confirm -> "Kata sandi tidak sama."
        else -> null
    }
    fun loginPassword(value: String): String? = when {
        value.isBlank() -> "Kata sandi tidak boleh kosong."
        value.length < 8 -> "Minimal 8 karakter."
        else -> null
    }

}
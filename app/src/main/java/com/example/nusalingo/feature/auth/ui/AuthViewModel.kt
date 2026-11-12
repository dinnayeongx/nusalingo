package com.example.nusalingo.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nusalingo.feature.auth.data.AccountRepository
import com.example.nusalingo.feature.auth.data.AuthRepository
import com.example.nusalingo.feature.auth.data.AuthValidator
import com.example.nusalingo.feature.auth.data.FirebaseAccountRepository
import com.example.nusalingo.feature.auth.data.FakeAuthRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
}

class LoginViewModel(
    private val repository: AccountRepository = FirebaseAccountRepository(),
) : ViewModel()
{
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onEmailChange(value: String) = _state.update { it.copy(email = value, emailError = null, errorMessage = null) }

    fun onPasswordChange(value: String) = _state.update { it.copy(password = value, passwordError = null, errorMessage = null) }

    fun login() {
        val s = _state.value
        if (s.isLoading) return

        val emailError = AuthValidator.email(s.email)
        val passError = AuthValidator.loginPassword(s.password)
        if (emailError != null || passError != null) {
            _state.update { it.copy(emailError = emailError, passwordError = passError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            repository.login(s.email.trim(), s.password)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(LoginEvent.LoginSuccess)
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Gagal masuk. Coba lagi.")
                    }
                }
        }
    }
}

data class ForgotPasswordUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val resendSeconds: Int = 0,
) {
    val canResend get() = resendSeconds == 0 && !isLoading
}

sealed interface ForgotPasswordEvent {
    data object LinkSent : ForgotPasswordEvent
}

class ForgotPasswordViewModel(
    private val repository: AuthRepository = FakeAuthRepository(),
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordUiState())
    val state: StateFlow<ForgotPasswordUiState> = _state.asStateFlow()

    private val _events = Channel<ForgotPasswordEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var cooldownJob: Job? = null

    fun onEmailChange(value: String) = _state.update { it.copy(email = value, emailError = null, errorMessage = null) }

    fun sendLink(isResend: Boolean = false) {
        val s = _state.value
        if (s.isLoading || (isResend && !s.canResend)) return

        val error = AuthValidator.email(s.email)
        if (error != null) {
            _state.update { it.copy(emailError = error) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            repository.sendResetLink(s.email.trim())
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    startCooldown()
                    if (!isResend) _events.send(ForgotPasswordEvent.LinkSent)
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Gagal mengirim tautan.")
                    }
                }
        }
    }

    private fun startCooldown(seconds: Int = 60) {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            for (s in seconds downTo 1) {
                _state.update { it.copy(resendSeconds = s) }
                delay(1000)
            }
            _state.update { it.copy(resendSeconds = 0) }
        }
    }
}

data class ResetPasswordUiState(
    val password: String = "",
    val confirm: String = "",
    val passwordError: String? = null,
    val confirmError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
) {
    val canSubmit: Boolean
        get() = AuthValidator.newPassword(password) == null &&
                confirm.isNotEmpty() && password == confirm
}

sealed interface ResetPasswordEvent {
    data object Success : ResetPasswordEvent
}

class ResetPasswordViewModel(
    private val repository: AuthRepository = FakeAuthRepository(),
) : ViewModel() {

    private val _state = MutableStateFlow(ResetPasswordUiState())
    val state: StateFlow<ResetPasswordUiState> = _state.asStateFlow()

    private val _events = Channel<ResetPasswordEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onPasswordChange(value: String) = _state.update {
        it.copy(
            password = value,
            passwordError = null,
            confirmError = AuthValidator.confirmPassword(value, it.confirm),
            errorMessage = null
        )
    }

    fun onConfirmChange(value: String) = _state.update {
        it.copy(
            confirm = value,
            confirmError = AuthValidator.confirmPassword(it.password, value),
            errorMessage = null
        )
    }

    fun submit() {
        val s = _state.value
        if (s.isLoading) return

        val passError = AuthValidator.newPassword(s.password)
        val confirmError = if (s.password != s.confirm) "Kata sandi tidak sama." else null
        if (passError != null || confirmError != null) {
            _state.update { it.copy(passwordError = passError, confirmError = confirmError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            repository.resetPassword(s.password)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(ResetPasswordEvent.Success)
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Gagal menyimpan kata sandi.")
                    }
                }
        }
    }
}
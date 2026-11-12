package com.example.nusalingo.feature.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nusalingo.feature.auth.data.AccountRepository
import com.example.nusalingo.feature.auth.data.AuthValidator
import com.example.nusalingo.feature.auth.data.FirebaseAccountRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptedTerms: Boolean = false,
    val nameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface RegisterEvent {
    data class Registered(val displayName: String) : RegisterEvent
}

class RegisterViewModel(
    private val repository: AccountRepository = FirebaseAccountRepository(),
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    private val _events = Channel<RegisterEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onNameChange(value: String) = _state.update {
        it.copy(name = value, nameError = null, errorMessage = null)
    }

    fun onEmailChange(value: String) = _state.update {
        it.copy(email = value, emailError = null, errorMessage = null)
    }

    fun onPasswordChange(value: String) = _state.update {
        it.copy(
            password = value,
            passwordError = null,
            confirmPasswordError = if (it.confirmPassword.isNotEmpty() && value != it.confirmPassword) {
                "Kata sandi tidak sama."
            } else {
                null
            },
            errorMessage = null,
        )
    }

    fun onConfirmPasswordChange(value: String) = _state.update {
        it.copy(
            confirmPassword = value,
            confirmPasswordError = if (value.isNotEmpty() && value != it.password) {
                "Kata sandi tidak sama."
            } else {
                null
            },
            errorMessage = null,
        )
    }

    fun onAcceptedTermsChange(value: Boolean) = _state.update {
        it.copy(acceptedTerms = value, termsError = null)
    }

    fun register() {
        val current = _state.value
        if (current.isLoading) return

        val name = current.name.trim()
        val email = current.email.trim().lowercase()
        val nameError = AuthValidator.registrationName(name)
        val emailError = AuthValidator.email(email)
        val passwordError = AuthValidator.registrationPassword(current.password)
        val confirmationError = AuthValidator.registrationConfirmation(
            current.password,
            current.confirmPassword,
        )
        val termsError = if (current.acceptedTerms) null else {
            "Setujui ketentuan penggunaan dan kebijakan privasi untuk melanjutkan."
        }

        if (
            nameError != null ||
            emailError != null ||
            passwordError != null ||
            confirmationError != null ||
            termsError != null
        ) {
            _state.update {
                it.copy(
                    nameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmationError,
                    termsError = termsError,
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            repository.register(email, current.password, name)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _events.send(RegisterEvent.Registered(name))
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Gagal membuat akun. Coba lagi.",
                        )
                    }
                }
        }
    }
}

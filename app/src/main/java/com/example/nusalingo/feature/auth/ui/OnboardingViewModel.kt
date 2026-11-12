package com.example.nusalingo.feature.auth.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import com.example.nusalingo.feature.auth.data.AccountRepository
import com.example.nusalingo.feature.auth.data.FirebaseAccountRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val step: Int = 0,
    val selectedScript: String? = null,
    val experience: String? = null,
    val learningGoals: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface OnboardingEvent {
    data object Completed : OnboardingEvent
}

class OnboardingViewModel(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val displayName: String = savedStateHandle["displayName"] ?: ""
    private val repository: AccountRepository = FirebaseAccountRepository()
    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    private val _events = Channel<OnboardingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun selectScript(value: String) = _state.update {
        it.copy(selectedScript = value, errorMessage = null)
    }

    fun selectExperience(value: String) = _state.update {
        it.copy(experience = value, errorMessage = null)
    }

    fun toggleGoal(value: String) = _state.update {
        val goals = it.learningGoals.toMutableSet()
        if (!goals.add(value)) goals.remove(value)
        it.copy(learningGoals = goals, errorMessage = null)
    }

    fun back() {
        _state.update {
            if (it.step > 0 && !it.isLoading) it.copy(step = it.step - 1, errorMessage = null)
            else it
        }
    }

    fun continueOnboarding() {
        val current = _state.value
        if (current.isLoading) return

        when (current.step) {
            0 -> {
                if (current.selectedScript == null) {
                    _state.update { it.copy(errorMessage = "Pilih aksara yang ingin kamu pelajari.") }
                } else {
                    _state.update { it.copy(step = 1, errorMessage = null) }
                }
            }

            1 -> {
                if (current.experience == null) {
                    _state.update { it.copy(errorMessage = "Pilih tingkat pengalamanmu.") }
                } else {
                    _state.update { it.copy(step = 2, errorMessage = null) }
                }
            }

            2 -> save(current)
            3 -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch { _events.send(OnboardingEvent.Completed) }
            }
        }
    }

    private fun save(current: OnboardingUiState) {
        val script = current.selectedScript ?: return
        val experience = current.experience ?: return
        if (current.learningGoals.isEmpty()) {
            _state.update { it.copy(errorMessage = "Pilih minimal satu tujuan belajar.") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            repository.saveOnboarding(
                displayName = displayName,
                selectedScript = script,
                experience = experience,
                learningGoals = current.learningGoals.toList(),
            )
                .onSuccess {
                    _state.update {
                        it.copy(isLoading = false, step = 3, errorMessage = null)
                    }
                }
                .onFailure { error ->
                    Log.w(LOG_TAG, "Onboarding preferences could not be synced to Firestore.", error)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Tidak dapat menyimpan pilihan. Periksa koneksi lalu coba lagi.",
                        )
                    }
                }
        }
    }

    private companion object {
        const val LOG_TAG = "OnboardingViewModel"
    }

}

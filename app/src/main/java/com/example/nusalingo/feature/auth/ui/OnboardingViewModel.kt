package com.example.nusalingo.feature.auth.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import com.example.nusalingo.feature.auth.data.AccountRepository
import com.example.nusalingo.feature.auth.data.FirebaseAccountRepository
import com.example.nusalingo.feature.auth.data.OnboardingProgress
import kotlinx.coroutines.Job
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
    val isLoading: Boolean = true,
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
    private var progressSaveJob: Job? = null

    private val _events = Channel<OnboardingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.loadOnboardingProgress()
                .onSuccess { progress ->
                    _state.update {
                        it.copy(
                            step = progress?.step ?: 0,
                            selectedScript = progress?.selectedScript,
                            experience = progress?.experience,
                            learningGoals = progress?.learningGoals.orEmpty(),
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { error ->
                    Log.w(LOG_TAG, "Onboarding progress could not be restored.", error)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Progres sebelumnya tidak dapat dipulihkan. Kamu bisa mulai lagi.",
                        )
                    }
                }
        }
    }

    fun selectScript(value: String) = updateProgress {
        it.copy(selectedScript = value, errorMessage = null)
    }

    fun selectExperience(value: String) = updateProgress {
        it.copy(experience = value, errorMessage = null)
    }

    fun toggleGoal(value: String) = updateProgress {
        val goals = it.learningGoals.toMutableSet()
        if (!goals.add(value)) goals.remove(value)
        it.copy(learningGoals = goals, errorMessage = null)
    }

    fun back() {
        if (_state.value.step > 0) {
            updateProgress { it.copy(step = it.step - 1, errorMessage = null) }
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
                    persistProgress()
                }
            }

            1 -> {
                if (current.experience == null) {
                    _state.update { it.copy(errorMessage = "Pilih tingkat pengalamanmu.") }
                } else {
                    _state.update { it.copy(step = 2, errorMessage = null) }
                    persistProgress()
                }
            }

            2 -> save(current)
            3 -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch { _events.send(OnboardingEvent.Completed) }
            }
        }
    }

    private fun updateProgress(transform: (OnboardingUiState) -> OnboardingUiState) {
        if (_state.value.isLoading) return
        _state.update(transform)
        persistProgress()
    }

    private fun persistProgress() {
        val current = _state.value
        progressSaveJob?.cancel()
        progressSaveJob = viewModelScope.launch {
            repository.saveOnboardingProgress(
                OnboardingProgress(
                    step = current.step,
                    selectedScript = current.selectedScript,
                    experience = current.experience,
                    learningGoals = current.learningGoals,
                )
            ).onFailure { error ->
                Log.w(LOG_TAG, "Onboarding progress could not be saved.", error)
                _state.update {
                    it.copy(errorMessage = "Progres onboarding tidak dapat disimpan di perangkat.")
                }
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
            progressSaveJob?.join()
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

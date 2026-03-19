package com.phantomclone.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantomclone.data.model.DeviceFingerprint
import com.phantomclone.data.model.Profile
import com.phantomclone.data.model.ProxyConfig
import com.phantomclone.data.repository.ProfileRepository
import com.phantomclone.fingerprint.FingerprintGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Profile Detail / Edit screen.
 *
 * Loads a profile by ID (or creates a new one), handles edits,
 * and persists changes to the repository.
 */
@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val fingerprintGenerator: FingerprintGenerator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /** -1L means "create new profile". */
    private val profileId: Long = savedStateHandle["profileId"] ?: -1L

    private val _uiState = MutableStateFlow(ProfileDetailUiState())
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    init {
        if (profileId != -1L) {
            loadProfile(profileId)
        } else {
            // New profile — pre-generate a fingerprint
            val fingerprint = fingerprintGenerator.generate()
            _uiState.update {
                it.copy(
                    profile = Profile(fingerprint = fingerprint),
                    isNew = true
                )
            }
        }
    }

    private fun loadProfile(id: Long) {
        viewModelScope.launch {
            val profile = repository.getProfile(id)
            if (profile != null) {
                _uiState.update { it.copy(profile = profile, isNew = false) }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(profile = it.profile.copy(name = name)) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(profile = it.profile.copy(notes = notes)) }
    }

    fun onProxyConfigChange(proxyConfig: ProxyConfig) {
        _uiState.update { it.copy(profile = it.profile.copy(proxyConfig = proxyConfig)) }
    }

    /** Regenerate the fingerprint for the current profile. */
    fun regenerateFingerprint() {
        val newFingerprint = fingerprintGenerator.generate()
        _uiState.update { it.copy(profile = it.profile.copy(fingerprint = newFingerprint)) }
    }

    /** Save the profile to the repository. */
    fun save(onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            val profile = _uiState.value.profile
            if (profile.name.isBlank()) {
                _uiState.update { it.copy(nameError = true) }
                return@launch
            }
            repository.saveProfile(profile)
            _uiState.update { it.copy(saved = true) }
            onSaved()
        }
    }

    fun clearNameError() {
        _uiState.update { it.copy(nameError = false) }
    }
}

/**
 * UI state for the Profile Detail screen.
 */
data class ProfileDetailUiState(
    val profile: Profile = Profile(),
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val nameError: Boolean = false
)

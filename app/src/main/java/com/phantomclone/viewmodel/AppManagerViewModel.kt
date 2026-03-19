package com.phantomclone.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantomclone.data.model.InstalledApp
import com.phantomclone.data.model.Profile
import com.phantomclone.data.repository.ProfileRepository
import com.phantomclone.engine.VirtualEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the App Manager screen (apps installed in a specific profile).
 */
@HiltViewModel
class AppManagerViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val virtualEngine: VirtualEngine,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val profileId: Long = savedStateHandle["profileId"] ?: -1L

    val apps: StateFlow<List<InstalledApp>> = if (profileId != -1L) {
        repository.observeApps(profileId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    } else {
        MutableStateFlow(emptyList())
    }

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile: StateFlow<Profile?> = _profile.asStateFlow()

    init {
        if (profileId != -1L) {
            viewModelScope.launch {
                _profile.value = repository.getProfile(profileId)
            }
        }
    }

    /** Install an APK file into the current profile. */
    fun installApk(apkPath: String) {
        viewModelScope.launch {
            virtualEngine.installApk(profileId, apkPath)
        }
    }

    /** Remove an app from the current profile. */
    fun removeApp(packageName: String) {
        viewModelScope.launch {
            virtualEngine.removeApp(profileId, packageName)
        }
    }
}

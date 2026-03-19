package com.phantomclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantomclone.data.model.Profile
import com.phantomclone.data.repository.ProfileRepository
import com.phantomclone.fingerprint.FingerprintGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Dashboard (profile list) screen.
 *
 * Manages the list of profiles with search/filter support.
 */
@HiltViewModel
class ProfileListViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val fingerprintGenerator: FingerprintGenerator
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val profiles: StateFlow<List<Profile>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) repository.observeProfiles()
            else repository.searchProfiles(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    /** Update the search query. */
    fun onSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /** Delete a profile and all its associated data. */
    fun deleteProfile(id: Long) {
        viewModelScope.launch {
            repository.deleteProfile(id)
        }
    }

    /** Create a new profile with a generated fingerprint and a default name. */
    fun createNewProfile(name: String = "Profile ${System.currentTimeMillis() % 10000}") {
        viewModelScope.launch {
            val fingerprint = fingerprintGenerator.generate()
            val profile = Profile(
                name = name,
                fingerprint = fingerprint
            )
            repository.saveProfile(profile)
        }
    }
}

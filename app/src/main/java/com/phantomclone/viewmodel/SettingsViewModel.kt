package com.phantomclone.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantomclone.data.model.Profile
import com.phantomclone.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for the Settings screen.
 *
 * Handles theme preferences, profile import/export, and app info.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val json: Json,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _exportResult = MutableStateFlow<String?>(null)
    val exportResult: StateFlow<String?> = _exportResult.asStateFlow()

    private val _importResult = MutableStateFlow<ImportResult?>(null)
    val importResult: StateFlow<ImportResult?> = _importResult.asStateFlow()

    /** Export all profiles to a JSON string. */
    fun exportAllProfiles() {
        viewModelScope.launch {
            try {
                val profiles = repository.observeProfiles().first()
                val jsonStr = json.encodeToString(profiles)

                // Write to app's external files directory
                val exportDir = File(context.getExternalFilesDir(null), "exports")
                exportDir.mkdirs()
                val file = File(exportDir, "phantom_clone_profiles_${System.currentTimeMillis()}.json")
                file.writeText(jsonStr)

                _exportResult.value = file.absolutePath
            } catch (e: Exception) {
                _exportResult.value = null
            }
        }
    }

    /** Import profiles from a JSON file path. */
    fun importProfiles(filePath: String) {
        viewModelScope.launch {
            try {
                val content = File(filePath).readText()
                val profiles = json.decodeFromString<List<Profile>>(content)
                var imported = 0
                profiles.forEach { profile ->
                    // Import with new ID to avoid conflicts
                    repository.saveProfile(profile.copy(id = 0))
                    imported++
                }
                _importResult.value = ImportResult(success = true, count = imported)
            } catch (e: Exception) {
                _importResult.value = ImportResult(success = false, count = 0, error = e.message)
            }
        }
    }

    fun clearExportResult() { _exportResult.value = null }
    fun clearImportResult() { _importResult.value = null }
}

data class ImportResult(
    val success: Boolean,
    val count: Int,
    val error: String? = null
)

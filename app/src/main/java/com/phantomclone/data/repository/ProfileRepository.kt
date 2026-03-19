package com.phantomclone.data.repository

import com.phantomclone.data.database.dao.InstalledAppDao
import com.phantomclone.data.database.dao.ProfileDao
import com.phantomclone.data.database.entity.InstalledAppEntity
import com.phantomclone.data.database.entity.ProfileEntity
import com.phantomclone.data.model.DeviceFingerprint
import com.phantomclone.data.model.InstalledApp
import com.phantomclone.data.model.Profile
import com.phantomclone.data.model.ProxyConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that abstracts the [ProfileDao] and [InstalledAppDao] from the ViewModels.
 *
 * Handles serialization/deserialization of complex fields (fingerprint, proxyConfig)
 * using kotlinx.serialization.
 */
@Singleton
class ProfileRepository @Inject constructor(
    private val profileDao: ProfileDao,
    private val installedAppDao: InstalledAppDao,
    private val json: Json
) {

    // ------------------------------------------------------------------ Profiles

    /** Observe all profiles as domain [Profile] objects. */
    fun observeProfiles(): Flow<List<Profile>> =
        profileDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    /** Observe profiles matching a search query. */
    fun searchProfiles(query: String): Flow<List<Profile>> =
        profileDao.search(query).map { entities -> entities.map { it.toDomain() } }

    /** Get a single profile by [id]. Returns null if not found. */
    suspend fun getProfile(id: Long): Profile? = profileDao.getById(id)?.toDomain()

    /** Create or update a profile. Returns the assigned ID. */
    suspend fun saveProfile(profile: Profile): Long {
        val entity = profile.toEntity()
        return profileDao.insert(entity)
    }

    /** Delete a profile and all its installed apps. */
    suspend fun deleteProfile(id: Long) {
        profileDao.deleteById(id)
        installedAppDao.deleteByProfile(id)
    }

    /** Mark a profile as last-used right now. */
    suspend fun markUsed(id: Long) {
        profileDao.updateLastUsed(id, System.currentTimeMillis())
    }

    /** Set the active state of a profile. */
    suspend fun setActive(id: Long, active: Boolean) {
        profileDao.updateActive(id, active)
    }

    // ------------------------------------------------------------------ Apps

    /** Observe installed apps for a profile. */
    fun observeApps(profileId: Long): Flow<List<InstalledApp>> =
        installedAppDao.observeByProfile(profileId).map { entities ->
            entities.map { it.toDomain() }
        }

    /** Record a newly installed app in a profile. */
    suspend fun addApp(app: InstalledApp): Long =
        installedAppDao.insert(app.toEntity())

    /** Remove an installed app record by packageName + profileId. */
    suspend fun removeApp(profileId: Long, packageName: String) =
        installedAppDao.deleteByPackageName(profileId, packageName)

    /** Remove an installed app record. */
    suspend fun removeApp(id: Long) = installedAppDao.deleteById(id)

    // ------------------------------------------------------------------ Conversion helpers

    private fun ProfileEntity.toDomain(): Profile = Profile(
        id = id,
        name = name,
        fingerprint = runCatching { json.decodeFromString<DeviceFingerprint>(fingerprintJson) }
            .getOrDefault(DeviceFingerprint()),
        proxyConfig = runCatching { json.decodeFromString<ProxyConfig>(proxyConfigJson) }
            .getOrDefault(ProxyConfig()),
        notes = notes,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt,
        isActive = isActive
    )

    private fun Profile.toEntity(): ProfileEntity = ProfileEntity(
        id = id,
        name = name,
        fingerprintJson = json.encodeToString(fingerprint),
        proxyConfigJson = json.encodeToString(proxyConfig),
        notes = notes,
        createdAt = createdAt,
        lastUsedAt = lastUsedAt,
        isActive = isActive
    )

    private fun InstalledAppEntity.toDomain(): InstalledApp = InstalledApp(
        packageName = packageName,
        appName = appName,
        apkPath = apkPath,
        profileId = profileId,
        installedAt = installedAt,
        versionName = versionName,
        versionCode = versionCode
    )

    private fun InstalledApp.toEntity(): InstalledAppEntity = InstalledAppEntity(
        profileId = profileId,
        packageName = packageName,
        appName = appName,
        apkPath = apkPath,
        installedAt = installedAt,
        versionName = versionName,
        versionCode = versionCode
    )
}

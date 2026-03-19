package com.phantomclone.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a PhantomClone profile stored in the database.
 *
 * Device fingerprint and proxy config are stored as JSON strings.
 */
@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    /** JSON-serialized [com.phantomclone.data.model.DeviceFingerprint]. */
    val fingerprintJson: String,
    /** JSON-serialized [com.phantomclone.data.model.ProxyConfig]. */
    val proxyConfigJson: String,
    val notes: String,
    val createdAt: Long,
    val lastUsedAt: Long?,
    val isActive: Boolean
)

/**
 * Room entity for apps installed inside a virtual profile.
 */
@Entity(tableName = "installed_apps")
data class InstalledAppEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val profileId: Long,
    val packageName: String,
    val appName: String,
    val apkPath: String,
    val installedAt: Long,
    val versionName: String,
    val versionCode: Long
)

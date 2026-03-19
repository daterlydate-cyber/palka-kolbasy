package com.phantomclone.engine

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages isolated sandbox storage directories for each profile.
 *
 * Each profile gets its own dedicated directory structure:
 * ```
 * files/profiles/{profileId}/
 *   ├── apps/          ← APK files for installed virtual apps
 *   ├── data/          ← App data (mirrors /data/data/{package}/)
 *   │   └── {packageName}/
 *   │       ├── shared_prefs/
 *   │       ├── databases/
 *   │       └── cache/
 *   └── external/      ← External storage simulation
 * ```
 *
 * This isolation ensures that:
 * - Different profiles cannot access each other's data.
 * - Clearing a profile only affects its own directory.
 * - Apps running in one profile cannot detect apps in another.
 */
@Singleton
class SandboxManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Returns the root sandbox directory for a profile. Creates it if absent. */
    fun getProfileRoot(profileId: Long): File =
        File(context.filesDir, "profiles/$profileId").also { it.mkdirs() }

    /** Returns the directory where APK files for a profile are stored. */
    fun getAppsDir(profileId: Long): File =
        File(getProfileRoot(profileId), "apps").also { it.mkdirs() }

    /** Returns the data directory for a specific app inside a profile. */
    fun getAppDataDir(profileId: Long, packageName: String): File =
        File(getProfileRoot(profileId), "data/$packageName").also { it.mkdirs() }

    /** Returns the SharedPreferences directory for a specific app inside a profile. */
    fun getSharedPrefsDir(profileId: Long, packageName: String): File =
        File(getAppDataDir(profileId, packageName), "shared_prefs").also { it.mkdirs() }

    /** Returns the databases directory for a specific app inside a profile. */
    fun getDatabasesDir(profileId: Long, packageName: String): File =
        File(getAppDataDir(profileId, packageName), "databases").also { it.mkdirs() }

    /** Returns the cache directory for a specific app inside a profile. */
    fun getCacheDir(profileId: Long, packageName: String): File =
        File(getAppDataDir(profileId, packageName), "cache").also { it.mkdirs() }

    /** Returns the simulated external storage directory for a profile. */
    fun getExternalDir(profileId: Long): File =
        File(getProfileRoot(profileId), "external").also { it.mkdirs() }

    /**
     * Delete all sandbox data for the given profile.
     * Called when a profile is deleted by the user.
     */
    fun deleteProfile(profileId: Long) {
        getProfileRoot(profileId).deleteRecursively()
    }

    /**
     * Copy an APK into the profile's apps directory.
     *
     * @param profileId Target profile ID.
     * @param sourceApkPath Absolute path to the source APK.
     * @param packageName Package name used as the file name.
     * @return Destination [File] inside the sandbox.
     */
    fun copyApkToSandbox(profileId: Long, sourceApkPath: String, packageName: String): File {
        val dest = File(getAppsDir(profileId), "$packageName.apk")
        File(sourceApkPath).copyTo(dest, overwrite = true)
        return dest
    }

    /**
     * Returns total storage size used by a profile sandbox in bytes.
     */
    fun getProfileStorageUsed(profileId: Long): Long =
        getProfileRoot(profileId).walkBottomUp().sumOf { it.length() }
}

package com.phantomclone.engine

import android.content.Context
import com.phantomclone.data.model.DeviceFingerprint
import com.phantomclone.data.model.InstalledApp
import com.phantomclone.data.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Phase 1 implementation of [VirtualEngine].
 *
 * In Phase 1, this acts as a foundation/stub that demonstrates the architecture.
 * Full APK virtualization (class loading, component proxying, hook installation)
 * is planned for Phase 2.
 *
 * See [VirtualEngine] interface documentation for architectural details.
 */
@Singleton
class VirtualEngineImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sandboxManager: SandboxManager,
    private val repository: ProfileRepository,
    private val systemPropertyHook: SystemPropertyHook,
    private val proxyManager: ProxyManager
) : VirtualEngine {

    override suspend fun installApk(profileId: Long, apkPath: String): InstalledApp? {
        return try {
            // Phase 1: Copy the APK to the sandbox directory.
            // Phase 2 will add: APK parsing, component registration, DEX loading.
            val apkFile = File(apkPath)
            if (!apkFile.exists()) return null

            // Extract basic app info from the APK via PackageManager
            val packageInfo = context.packageManager.getPackageArchiveInfo(apkPath, 0)
                ?: return null

            val packageName = packageInfo.packageName
            val versionName = packageInfo.versionName ?: "1.0"
            val versionCode = packageInfo.longVersionCode
            val appName = packageInfo.applicationInfo?.let {
                it.loadLabel(context.packageManager).toString()
            } ?: packageName

            // Copy APK into sandbox
            sandboxManager.copyApkToSandbox(profileId, apkPath, packageName)

            val app = InstalledApp(
                packageName = packageName,
                appName = appName,
                apkPath = sandboxManager.getAppsDir(profileId)
                    .resolve("$packageName.apk").absolutePath,
                profileId = profileId,
                versionName = versionName,
                versionCode = versionCode
            )

            repository.addApp(app)
            app
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun launchApp(
        profileId: Long,
        packageName: String,
        fingerprint: DeviceFingerprint
    ) {
        // Phase 1 stub: Configure the system property hook and proxy for this session.
        // Phase 2 will add: actual app launch via stub activity, classloader injection.

        // Apply fingerprint spoofing context
        systemPropertyHook.applyFingerprint(fingerprint)

        // Configure proxy for this profile
        val profile = repository.getProfile(profileId) ?: return
        proxyManager.configureForProfile(profile)

        // Mark profile as used
        repository.markUsed(profileId)
        repository.setActive(profileId, true)

        // TODO (Phase 2): Launch the virtual app via stub components
        // See VirtualEngine docs for the planned implementation.
    }

    override suspend fun listApps(profileId: Long): List<InstalledApp> =
        repository.observeApps(profileId).first()

    override suspend fun removeApp(profileId: Long, packageName: String) {
        // Delete the APK from sandbox storage
        sandboxManager.getAppsDir(profileId).resolve("$packageName.apk").delete()

        // Remove the data directory
        sandboxManager.getAppDataDir(profileId, packageName).deleteRecursively()

        // Remove from database
        repository.removeApp(profileId, packageName)
    }
}

package com.phantomclone.engine

import com.phantomclone.data.model.DeviceFingerprint
import com.phantomclone.data.model.InstalledApp

/**
 * Contract for the Virtual Engine — the core component responsible for
 * launching and managing cloned applications inside isolated profiles.
 *
 * ## Architecture Overview
 *
 * The Virtual Engine works by creating an isolated Android environment
 * *within* the host application process. This is conceptually similar to
 * projects like [VirtualApp](https://github.com/asLody/VirtualApp) and
 * [VirtualXposed](https://github.com/android-hacker/VirtualXposed).
 *
 * ### How real implementations work (Phase 2+):
 * 1. **APK parsing** — The engine parses the target APK's manifest to extract
 *    components (activities, services, providers, receivers).
 * 2. **Component registration** — Stub components pre-declared in the host manifest
 *    are used to proxy the target app's components.
 * 3. **ClassLoader injection** — A custom ClassLoader loads the target APK's DEX
 *    code into the current process.
 * 4. **Hook installation** — Java method hooks (via reflection / Xposed / YAHFA)
 *    intercept system API calls and redirect them to return spoofed values.
 * 5. **File system virtualization** — All I/O operations are redirected to a
 *    per-profile sandbox directory.
 *
 * Phase 1 provides the interface definition and the [SandboxManager] foundation.
 * Full hook implementation is planned for Phase 2.
 */
interface VirtualEngine {

    /**
     * Install an APK file into the given profile's virtual environment.
     *
     * @param profileId The ID of the target profile.
     * @param apkPath Absolute path to the APK file to install.
     * @return The [InstalledApp] record on success, or null on failure.
     */
    suspend fun installApk(profileId: Long, apkPath: String): InstalledApp?

    /**
     * Launch an installed app within its profile environment,
     * applying the profile's [DeviceFingerprint] spoofing.
     *
     * @param profileId The ID of the profile containing the app.
     * @param packageName The package name of the app to launch.
     * @param fingerprint The device fingerprint to spoof for this session.
     */
    suspend fun launchApp(
        profileId: Long,
        packageName: String,
        fingerprint: DeviceFingerprint
    )

    /**
     * List all apps installed in the given profile.
     *
     * @param profileId The ID of the profile.
     * @return List of [InstalledApp] records.
     */
    suspend fun listApps(profileId: Long): List<InstalledApp>

    /**
     * Remove an app from the given profile.
     *
     * @param profileId The ID of the profile.
     * @param packageName The package name of the app to remove.
     */
    suspend fun removeApp(profileId: Long, packageName: String)
}

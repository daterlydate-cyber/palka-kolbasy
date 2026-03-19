package com.phantomclone.ui.navigation

/**
 * Sealed class defining all navigation destinations in the app.
 */
sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object ProfileDetail : Screen("profile_detail/{profileId}") {
        fun createRoute(profileId: Long = -1L) = "profile_detail/$profileId"
    }
    data object FingerprintViewer : Screen("fingerprint_viewer/{profileId}") {
        fun createRoute(profileId: Long) = "fingerprint_viewer/$profileId"
    }
    data object AppManager : Screen("app_manager/{profileId}") {
        fun createRoute(profileId: Long) = "app_manager/$profileId"
    }
    data object Settings : Screen("settings")
}

/** Bottom navigation bar items. */
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val iconDescription: String
) {
    data object Profiles : BottomNavItem("dashboard", "Profiles", "Profiles icon")
    data object Apps : BottomNavItem("apps_hub", "Apps", "Apps icon")
    data object Settings : BottomNavItem("settings", "Settings", "Settings icon")
}

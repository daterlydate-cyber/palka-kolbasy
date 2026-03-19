package com.phantomclone.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.phantomclone.ui.navigation.Screen
import com.phantomclone.ui.screens.*

/**
 * Root composable that hosts the entire navigation graph.
 *
 * Implements bottom navigation between:
 * - Dashboard (profiles list)
 * - Apps Hub
 * - Settings
 *
 * Plus detail screens pushed on top:
 * - Profile Detail / Edit
 * - Fingerprint Viewer
 * - App Manager
 */
@Composable
fun PhantomCloneNavHost() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        Triple("dashboard", "Profiles", Icons.Default.AccountCircle),
        Triple("settings", "Settings", Icons.Default.Settings)
    )

    Scaffold(
        bottomBar = {
            val currentBackStack by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStack?.destination

            // Only show bottom bar on top-level destinations
            val showBottomBar = bottomNavItems.any { (route, _, _) ->
                currentDestination?.hierarchy?.any { it.route == route } == true
            }

            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = androidx.compose.ui.unit.Dp(0f)
                ) {
                    bottomNavItems.forEach { (route, label, icon) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            selected = currentDestination?.hierarchy?.any {
                                it.route == route
                            } == true,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToProfile = { profileId ->
                        navController.navigate(Screen.ProfileDetail.createRoute(profileId))
                    },
                    onNavigateToNewProfile = {
                        navController.navigate(Screen.ProfileDetail.createRoute(-1L))
                    },
                    onNavigateToAppManager = { profileId ->
                        navController.navigate(Screen.AppManager.createRoute(profileId))
                    }
                )
            }

            composable(
                route = Screen.ProfileDetail.route,
                arguments = listOf(navArgument("profileId") { type = NavType.LongType })
            ) {
                ProfileDetailScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToFingerprint = { profileId ->
                        navController.navigate(Screen.FingerprintViewer.createRoute(profileId))
                    }
                )
            }

            composable(
                route = Screen.FingerprintViewer.route,
                arguments = listOf(navArgument("profileId") { type = NavType.LongType })
            ) {
                FingerprintViewerScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.AppManager.route,
                arguments = listOf(navArgument("profileId") { type = NavType.LongType })
            ) {
                AppManagerScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

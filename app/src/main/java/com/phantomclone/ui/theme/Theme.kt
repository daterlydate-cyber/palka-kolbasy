package com.phantomclone.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Theme enum for user-selectable theme modes.
 */
enum class AppTheme { DARK, LIGHT, SYSTEM }

/**
 * Main app theme composable.
 *
 * Defaults to dark mode (cyber/hacker aesthetic) with deep purple + cyan accents.
 * Supports system-default dark mode on Android 12+ (dynamic color is disabled
 * to preserve the custom cyber palette).
 */
@Composable
fun PhantomCloneTheme(
    appTheme: AppTheme = AppTheme.DARK,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (darkTheme) PhantomDarkColorScheme else PhantomLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PhantomTypography,
        content = content
    )
}

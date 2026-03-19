package com.phantomclone.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Dark cyber/hacker palette
val CyberPurple = Color(0xFF7B2FBE)
val CyberPurpleVariant = Color(0xFF9C4DCC)
val CyberCyan = Color(0xFF00E5FF)
val CyberCyanDim = Color(0xFF00B8D4)
val DarkBackground = Color(0xFF0A0A0F)
val DarkSurface = Color(0xFF12121A)
val DarkSurfaceVariant = Color(0xFF1E1E2E)
val DarkOutline = Color(0xFF2D2D3F)
val OnDarkPrimary = Color(0xFFFFFFFF)
val OnDarkSecondary = Color(0xFFCCCCFF)
val ErrorRed = Color(0xFFCF6679)
val SuccessGreen = Color(0xFF4CAF50)
val WarningYellow = Color(0xFFFFC107)

/** Default dark color scheme with cyber aesthetic (deep purple + cyan). */
val PhantomDarkColorScheme = darkColorScheme(
    primary = CyberPurple,
    onPrimary = OnDarkPrimary,
    primaryContainer = Color(0xFF3700B3),
    onPrimaryContainer = Color(0xFFD0BCFF),
    secondary = CyberCyan,
    onSecondary = Color(0xFF003544),
    secondaryContainer = CyberCyanDim,
    onSecondaryContainer = Color(0xFF001F28),
    tertiary = Color(0xFF03DAC6),
    onTertiary = Color(0xFF003731),
    background = DarkBackground,
    onBackground = Color(0xFFE6E0E9),
    surface = DarkSurface,
    onSurface = Color(0xFFE6E0E9),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = DarkOutline,
    error = ErrorRed,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

/** Light color scheme (less used but available for system-default). */
val PhantomLightColorScheme = lightColorScheme(
    primary = CyberPurple,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF006781),
    onSecondary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

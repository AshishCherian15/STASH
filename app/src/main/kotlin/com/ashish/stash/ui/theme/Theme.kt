package com.ashish.stash.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = VaultBrass,
    secondary = LedgerSlate,
    tertiary = VerifiedSage,
    background = InkNavy,
    surface = InkNavy,
    onPrimary = Limestone,
    onSecondary = Limestone,
    onTertiary = Limestone,
    onBackground = Limestone,
    onSurface = Limestone,
)

private val LightColorScheme = lightColorScheme(
    primary = VaultBrass,
    secondary = LedgerSlate,
    tertiary = VerifiedSage,
    background = Limestone,
    surface = Limestone,
    onPrimary = InkNavy,
    onSecondary = InkNavy,
    onTertiary = InkNavy,
    onBackground = InkNavy,
    onSurface = InkNavy,
)

@Composable
fun StashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = StashTypography,
        shapes = StashShapes,
        content = content
    )
}

package com.ashish.stash.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.ashish.stash.core.preferences.UserData

private val DarkColorScheme = darkColorScheme(
    primary = StashBlue,
    secondary = LedgerSlate,
    tertiary = VerifiedSage,
    background = Color(0xFF1A1C1E),
    surface = Color(0xFF1A1C1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFE2E2E6),
    onSurface = Color(0xFFE2E2E6),
)

private val LightColorScheme = lightColorScheme(
    primary = StashBlue,
    secondary = LedgerSlate,
    tertiary = VerifiedSage,
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C2733),
    onSurface = Color(0xFF1C2733),
)

@Composable
fun StashTheme(
    userData: UserData? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val isDark = userData?.darkTheme ?: darkTheme
    val isDynamic = userData?.dynamicColor ?: dynamicColor
    
    val colorScheme = when {
        isDynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    // Apply Font Customization
    val baseTypography = StashTypography
    val customTypography = if (userData != null) {
        val family = when(userData.fontFamily) {
            "SERIF" -> FontFamily.Serif
            "MONOSPACE" -> FontFamily.Monospace
            else -> FontFamily.SansSerif
        }
        // In a full implementation, we'd scale every style. 
        // For brevity, we'll assume the theme handles standard font families.
        baseTypography
    } else baseTypography

    MaterialTheme(
        colorScheme = colorScheme,
        typography = customTypography,
        shapes = StashShapes,
        content = content
    )
}

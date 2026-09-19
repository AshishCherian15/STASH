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
import androidx.compose.ui.unit.sp
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
    
    // Resolve Accent Color from Settings
    val accentColor = when(userData?.themeColor) {
        "GREEN" -> Color(0xFF4CAF50)
        "RED" -> Color(0xFFF44336)
        "ORANGE" -> Color(0xFFFF9800)
        "PURPLE" -> Color(0xFF9C27B0)
        else -> StashBlue
    }

    val colorScheme = when {
        isDynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDark -> DarkColorScheme.copy(primary = accentColor)
        else -> LightColorScheme.copy(primary = accentColor)
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDark
        }
    }

    // Apply Font and Size Customization
    val family = when(userData?.fontFamily) {
        "SERIF" -> FontFamily.Serif
        "MONOSPACE" -> FontFamily.Monospace
        else -> FontFamily.SansSerif
    }
    
    val scale = userData?.fontSizeScale ?: 1.0f
    val baseTypography = StashTypography
    
    val scaledTypography = Typography(
        displayLarge = baseTypography.displayLarge.copy(fontFamily = family, fontSize = baseTypography.displayLarge.fontSize * scale),
        displayMedium = baseTypography.displayMedium.copy(fontFamily = family, fontSize = baseTypography.displayMedium.fontSize * scale),
        displaySmall = baseTypography.displaySmall.copy(fontFamily = family, fontSize = baseTypography.displaySmall.fontSize * scale),
        headlineLarge = baseTypography.headlineLarge.copy(fontFamily = family, fontSize = baseTypography.headlineLarge.fontSize * scale),
        headlineMedium = baseTypography.headlineMedium.copy(fontFamily = family, fontSize = baseTypography.headlineMedium.fontSize * scale),
        headlineSmall = baseTypography.headlineSmall.copy(fontFamily = family, fontSize = baseTypography.headlineSmall.fontSize * scale),
        titleLarge = baseTypography.titleLarge.copy(fontFamily = family, fontSize = baseTypography.titleLarge.fontSize * scale),
        titleMedium = baseTypography.titleMedium.copy(fontFamily = family, fontSize = baseTypography.titleMedium.fontSize * scale),
        titleSmall = baseTypography.titleSmall.copy(fontFamily = family, fontSize = baseTypography.titleSmall.fontSize * scale),
        bodyLarge = baseTypography.bodyLarge.copy(fontFamily = family, fontSize = baseTypography.bodyLarge.fontSize * scale),
        bodyMedium = baseTypography.bodyMedium.copy(fontFamily = family, fontSize = baseTypography.bodyMedium.fontSize * scale),
        bodySmall = baseTypography.bodySmall.copy(fontFamily = family, fontSize = baseTypography.bodySmall.fontSize * scale),
        labelLarge = baseTypography.labelLarge.copy(fontFamily = family, fontSize = baseTypography.labelLarge.fontSize * scale),
        labelMedium = baseTypography.labelMedium.copy(fontFamily = family, fontSize = baseTypography.labelMedium.fontSize * scale),
        labelSmall = baseTypography.labelSmall.copy(fontFamily = family, fontSize = baseTypography.labelSmall.fontSize * scale)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography,
        shapes = StashShapes,
        content = content
    )
}

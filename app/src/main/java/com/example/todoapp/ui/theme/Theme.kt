package com.example.todoapp.ui.theme

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

import androidx.compose.material3.lightColorScheme

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryNeon,
    secondary = SecondaryCyan,
    tertiary = AccentPurple,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = BorderGlassDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNeon,
    secondary = SecondaryCyan,
    tertiary = AccentPurple,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF64748B),
    outline = BorderGlassLight
)

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val targetColorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val animatedBackground by animateColorAsState(
        targetValue = targetColorScheme.background,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "backgroundColor"
    )
    val animatedSurface by animateColorAsState(
        targetValue = targetColorScheme.surface,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "surfaceColor"
    )
    val animatedSurfaceVariant by animateColorAsState(
        targetValue = targetColorScheme.surfaceVariant,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "surfaceVariantColor"
    )

    val colorScheme = targetColorScheme.copy(
        background = animatedBackground,
        surface = animatedSurface,
        surfaceVariant = animatedSurfaceVariant
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

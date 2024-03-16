package com.mukiva.navigation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val lightColorScheme = lightColorScheme()

val darkColorScheme = darkColorScheme()

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun supportDynamicColors(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun RSSTheme(content: @Composable () -> Unit) {
    val inDarkMode = isSystemInDarkTheme()

    val colors = if (supportDynamicColors()) {
        val context = LocalContext.current
        if (inDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (inDarkMode) darkColorScheme else lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = inDarkMode
        }
    }

    MaterialTheme(
        colorScheme = colors,
        typography = RssTypography.createTypography(),
        content = content
    )
}
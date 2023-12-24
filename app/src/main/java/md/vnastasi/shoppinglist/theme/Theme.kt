package md.vnastasi.shoppinglist.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Colors.Light.primary,
    onPrimary = Colors.Light.onPrimary,
    primaryContainer = Colors.Light.primaryContainer,
    onPrimaryContainer = Colors.Light.onPrimaryContainer,
    secondary = Colors.Light.secondary,
    onSecondary = Colors.Light.onSecondary,
    secondaryContainer = Colors.Light.secondaryContainer,
    onSecondaryContainer = Colors.Light.onSecondaryContainer,
    tertiary = Colors.Light.tertiary,
    onTertiary = Colors.Light.onTertiary,
    tertiaryContainer = Colors.Light.tertiaryContainer,
    onTertiaryContainer = Colors.Light.onTertiaryContainer,
    error = Colors.Light.error,
    errorContainer = Colors.Light.errorContainer,
    onError = Colors.Light.onError,
    onErrorContainer = Colors.Light.onErrorContainer,
    background = Colors.Light.background,
    onBackground = Colors.Light.onBackground,
    surface = Colors.Light.surface,
    onSurface = Colors.Light.onSurface,
    surfaceVariant = Colors.Light.surfaceVariant,
    onSurfaceVariant = Colors.Light.onSurfaceVariant,
    outline = Colors.Light.outline,
    inverseOnSurface = Colors.Light.inverseOnSurface,
    inverseSurface = Colors.Light.inverseSurface,
    inversePrimary = Colors.Light.inversePrimary,
    surfaceTint = Colors.Light.surfaceTint,
    outlineVariant = Colors.Light.outlineVariant,
    scrim = Colors.Light.scrim
)

private val DarkColorScheme = darkColorScheme(
    primary = Colors.Dark.primary,
    onPrimary = Colors.Dark.onPrimary,
    primaryContainer = Colors.Dark.primaryContainer,
    onPrimaryContainer = Colors.Dark.onPrimaryContainer,
    secondary = Colors.Dark.secondary,
    onSecondary = Colors.Dark.onSecondary,
    secondaryContainer = Colors.Dark.secondaryContainer,
    onSecondaryContainer = Colors.Dark.onSecondaryContainer,
    tertiary = Colors.Dark.tertiary,
    onTertiary = Colors.Dark.onTertiary,
    tertiaryContainer = Colors.Dark.tertiaryContainer,
    onTertiaryContainer = Colors.Dark.onTertiaryContainer,
    error = Colors.Dark.error,
    errorContainer = Colors.Dark.errorContainer,
    onError = Colors.Dark.onError,
    onErrorContainer = Colors.Dark.onErrorContainer,
    background = Colors.Dark.background,
    onBackground = Colors.Dark.onBackground,
    surface = Colors.Dark.surface,
    onSurface = Colors.Dark.onSurface,
    surfaceVariant = Colors.Dark.surfaceVariant,
    onSurfaceVariant = Colors.Dark.onSurfaceVariant,
    outline = Colors.Dark.outline,
    inverseOnSurface = Colors.Dark.inverseOnSurface,
    inverseSurface = Colors.Dark.inverseSurface,
    inversePrimary = Colors.Dark.inversePrimary,
    surfaceTint = Colors.Dark.surfaceTint,
    outlineVariant = Colors.Dark.outlineVariant,
    scrim = Colors.Dark.scrim
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicColorsAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val colorScheme = when {
        dynamicColorsAvailable && useDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        dynamicColorsAvailable && !useDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        useDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = !useDarkTheme
            insetsController.isAppearanceLightNavigationBars = !useDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

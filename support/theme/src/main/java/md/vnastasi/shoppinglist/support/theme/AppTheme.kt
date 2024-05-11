package md.vnastasi.shoppinglist.support.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Light.primary,
    onPrimary = AppColors.Light.onPrimary,
    primaryContainer = AppColors.Light.primaryContainer,
    onPrimaryContainer = AppColors.Light.onPrimaryContainer,
    secondary = AppColors.Light.secondary,
    onSecondary = AppColors.Light.onSecondary,
    secondaryContainer = AppColors.Light.secondaryContainer,
    onSecondaryContainer = AppColors.Light.onSecondaryContainer,
    tertiary = AppColors.Light.tertiary,
    onTertiary = AppColors.Light.onTertiary,
    tertiaryContainer = AppColors.Light.tertiaryContainer,
    onTertiaryContainer = AppColors.Light.onTertiaryContainer,
    error = AppColors.Light.error,
    errorContainer = AppColors.Light.errorContainer,
    onError = AppColors.Light.onError,
    onErrorContainer = AppColors.Light.onErrorContainer,
    background = AppColors.Light.background,
    onBackground = AppColors.Light.onBackground,
    surface = AppColors.Light.surface,
    onSurface = AppColors.Light.onSurface,
    surfaceVariant = AppColors.Light.surfaceVariant,
    onSurfaceVariant = AppColors.Light.onSurfaceVariant,
    outline = AppColors.Light.outline,
    inverseOnSurface = AppColors.Light.inverseOnSurface,
    inverseSurface = AppColors.Light.inverseSurface,
    inversePrimary = AppColors.Light.inversePrimary,
    surfaceTint = AppColors.Light.surfaceTint,
    outlineVariant = AppColors.Light.outlineVariant,
    scrim = AppColors.Light.scrim
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Dark.primary,
    onPrimary = AppColors.Dark.onPrimary,
    primaryContainer = AppColors.Dark.primaryContainer,
    onPrimaryContainer = AppColors.Dark.onPrimaryContainer,
    secondary = AppColors.Dark.secondary,
    onSecondary = AppColors.Dark.onSecondary,
    secondaryContainer = AppColors.Dark.secondaryContainer,
    onSecondaryContainer = AppColors.Dark.onSecondaryContainer,
    tertiary = AppColors.Dark.tertiary,
    onTertiary = AppColors.Dark.onTertiary,
    tertiaryContainer = AppColors.Dark.tertiaryContainer,
    onTertiaryContainer = AppColors.Dark.onTertiaryContainer,
    error = AppColors.Dark.error,
    errorContainer = AppColors.Dark.errorContainer,
    onError = AppColors.Dark.onError,
    onErrorContainer = AppColors.Dark.onErrorContainer,
    background = AppColors.Dark.background,
    onBackground = AppColors.Dark.onBackground,
    surface = AppColors.Dark.surface,
    onSurface = AppColors.Dark.onSurface,
    surfaceVariant = AppColors.Dark.surfaceVariant,
    onSurfaceVariant = AppColors.Dark.onSurfaceVariant,
    outline = AppColors.Dark.outline,
    inverseOnSurface = AppColors.Dark.inverseOnSurface,
    inverseSurface = AppColors.Dark.inverseSurface,
    inversePrimary = AppColors.Dark.inversePrimary,
    surfaceTint = AppColors.Dark.surfaceTint,
    outlineVariant = AppColors.Dark.outlineVariant,
    scrim = AppColors.Dark.scrim
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
